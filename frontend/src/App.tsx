import { useState } from "react";
import "./App.css";
import { planJourney as requestJourney, type JourneyResponse } from "./services/journeyResponse";
import RouteForm from "./components/Routeform";
import ProfilePage from "./components/ProfilePage";
import type { Station } from "./services/stationService";
import { loadProfile, saveProfile, type TravelProfile } from "./utils/profileStorage";
import RouteResult from "./components/RouteResult";

type TimeMode = "now" | "departure" | "arrival";
type Page = "planner" | "profile" | "results";

function App() {
  const [journey, setJourney] = useState<JourneyResponse | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [routeError, setRouteError] = useState("");
  const [departure, setDeparture] = useState<Station | null>(null);
  const [destination, setDestination] = useState<Station | null>(null);
  const [timeMode, setTimeMode] = useState<TimeMode>("now");
  const [date, setDate] = useState("");
  const [time, setTime] = useState("");
  const [profile, setProfile] =
    useState<TravelProfile>(loadProfile); // Hier wordt loadProfile aangeroepen 
  const [currentPage, setCurrentPage] =
    useState<Page>("planner");

  const handleSaveProfile = (
    selectedFacilities: string[],
    transferTime: number,
  ) => {
    const updatedProfile: TravelProfile = {
      selectedFacilities,
      transferTime,
    };

    setProfile(updatedProfile);
    saveProfile(updatedProfile); // Hier wordt profile gesaved 
    setCurrentPage("planner");
  };

  const planJourney = async (event: React.FormEvent) => {
    event.preventDefault();
    if (isLoading) return;
    setRouteError("");

    if (!departure || !destination) {
      setRouteError("Kies een vertrekstation en bestemming uit de zoekresultaten.");
      return;
    }

    if (departure.gtfsId === destination.gtfsId) {
      setRouteError("Kies twee verschillende stations.");
      return;
    }

    if (timeMode !== "now") {
      setRouteError("Reizen op een gekozen datum of tijd wordt nog niet ondersteund.");
      return;
    }

    setIsLoading(true);
    try {
      const result = await requestJourney(
        departure.gtfsId,
        destination.gtfsId,
      );
      setJourney(result);
      setCurrentPage("results");
    } catch {
      setRouteError("De route kon niet worden opgehaald. Probeer het opnieuw.");
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <main>
      {currentPage === "profile" ? (
        <ProfilePage
          selectedFacilities={profile.selectedFacilities}
          transferTime={profile.transferTime}
          onSave={handleSaveProfile}
          onBack={() => setCurrentPage("planner")}
        />
      ) : currentPage === "planner" ? (
        <>
          <h1>OV Reisplanner</h1>

          <p>
            Plan een reis met het openbaar vervoer op basis van jouw
            toegankelijkheidsbehoeften.
          </p>

          <RouteForm
            departure={departure}
            destination={destination}
            timeMode={timeMode}
            date={date}
            time={time}
            setDeparture={setDeparture}
            setDestination={setDestination}
            setTimeMode={setTimeMode}
            setDate={setDate}
            setTime={setTime}
            onSubmit={planJourney}
            onProfileClick={() => setCurrentPage("profile")}
          />
          {isLoading && <p role="status">Route wordt opgehaald…</p>}
          {routeError && <p role="alert">{routeError}</p>}
        </>
      ) : (
        <>
          <button
            type="button"
            onClick={() => setCurrentPage("planner")}
          >
            ← Reis aanpassen
          </button>

          {journey && (
            <RouteResult
              journey={journey}
              originName={departure?.name ?? ""}
              destinationName={destination?.name ?? ""}
            />
          )}
        </>
      )}
    </main>
  );
}

export default App;

