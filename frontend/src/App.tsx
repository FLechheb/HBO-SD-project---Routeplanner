import { useState, useEffect, useCallback } from "react";
import "./App.css";
import { planJourney as requestJourney, JourneyRequestError, type JourneyResponse } from "./services/journeyResponse";
import RouteForm from "./components/Routeform";
import ProfilePage from "./components/ProfilePage";
import type { Station } from "./services/stationService";
import { loadProfile, saveProfile, type TravelProfile } from "./utils/profileStorage";
import RouteResult from "./components/RouteResult";
import { saveJourney } from "./utils/journeyStorage";
import { loadJourney } from "./utils/journeyStorage";

type TimeMode = "now" | "departure" | "arrival";
type Page = "planner" | "profile" | "results";

function App() {
  const [journey, setJourney] = useState<JourneyResponse | null>(null);
  const [originName, setOriginName] = useState("");
  const [destinationName, setDestinationName] = useState("");


  useEffect(() => {
    if (!navigator.onLine) {
      const savedJourney = loadJourney();

      if (savedJourney) {
        setJourney(savedJourney.journey);
        setOriginName(savedJourney.originName);
        setDestinationName(savedJourney.destinationName);
        setCurrentPage("results");
      }
    }
  }, []);

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

  const handleResetFacilities = useCallback(() => {
    const updatedProfile: TravelProfile = {
      selectedFacilities: [],
      transferTime: profile.transferTime,
    };
    saveProfile(updatedProfile);
    setProfile(updatedProfile);
  }, [profile.transferTime]);


  const planJourney = async (event: React.FormEvent) => {
    event.preventDefault();
    if (isLoading) return;
    setRouteError("");

    if (!departure || !destination) {
      setRouteError(
          "Kies een vertrekstation en bestemming uit de zoekresultaten.",
      );
      return;
    }

    if (departure.gtfsId === destination.gtfsId) {
      setRouteError("Kies twee verschillende stations.");
      return;
    }

    if (timeMode !== "now") {
      setRouteError(
          "Reizen op een gekozen datum of tijd wordt nog niet ondersteund.",
      );
      return;
    }

    // Offline: toon de laatst opgeslagen route zonder de backend aan te roepen.
    if (!navigator.onLine) {
      const savedJourney = loadJourney();

      if (savedJourney) {
        setJourney(savedJourney.journey);
        setOriginName(savedJourney.originName);
        setDestinationName(savedJourney.destinationName);
        setCurrentPage("results");
      } else {
        setRouteError(
            "Je bent offline en er is geen opgeslagen route beschikbaar.",
        );
      }

      return;
    }

    setIsLoading(true);

    try {
      const result = await requestJourney(
          departure.gtfsId,
          destination.gtfsId,
          profile.transferTime,
          profile.selectedFacilities,
      );

      try {
        saveJourney({
          journey: result,
          originName: departure.name,
          destinationName: destination.name,
        });
      } catch {
        // De reis is opgehaald, maar lokaal opslaan is niet gelukt.
      }

      setJourney(result);
      setOriginName(departure.name);
      setDestinationName(destination.name);
      setCurrentPage("results");

    } catch (error) {
      if (error instanceof TypeError && !navigator.onLine) {
        const savedJourney = loadJourney();

        if (savedJourney) {
          setJourney(savedJourney.journey);
          setOriginName(savedJourney.originName);
          setDestinationName(savedJourney.destinationName);
          setCurrentPage("results");
        } else {
          setRouteError(
              "Je bent offline en er is geen opgeslagen route beschikbaar."
          );
        }
      } else if (error instanceof JourneyRequestError && error.status === 400) {
        setRouteError(
            "De reisvoorkeuren of stations zijn ongeldig. Controleer Mijn profiel en kies de stations opnieuw."
        );
      } else {
        setRouteError(
            "De route kon niet worden opgehaald. Probeer het opnieuw."
        );
      }
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
          onResetFacilities={handleResetFacilities}
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
                  originName={originName || departure?.name || ""}
                  destinationName={destinationName || destination?.name || ""}
            />
          )}
        </>
      )}
    </main>
  );
}

export default App;
