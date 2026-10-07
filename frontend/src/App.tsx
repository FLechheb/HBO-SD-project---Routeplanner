import { useState } from "react";
import "./App.css";
import dummyRoute from "./Data/dummydata.json";
import type { DummyRoute } from "./types/route.ts";
import RouteResult from "./components/RouteResult.tsx";
import RouteForm from "./components/Routeform";
import ProfilePage from "./components/ProfilePage";
import type { Station } from "./services/stationService";
import {
  loadProfile,
  saveProfile,
  type TravelProfile,
} from "./utils/profileStorage";

const route: DummyRoute = dummyRoute;

type TimeMode = "now" | "departure" | "arrival";
type Page = "planner" | "profile" | "results";

function App() {
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

  const planJourney = (event: React.FormEvent) => {
    event.preventDefault();
    setCurrentPage("results");

    console.log({
      departure,
      destination,
      timeMode,
      date,
      time,
      selectedFacilities: profile.selectedFacilities,
      transferTime: profile.transferTime,
    });
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

        </>
      ) : (
        <>
          <button
            type="button"
            onClick={() => setCurrentPage("planner")}
          >
            ← Reis aanpassen
          </button>

          <RouteResult
            route={route}
            departure={departure?.name ?? ""}
            destination={destination?.name ?? ""}
            selectedFacilities={profile.selectedFacilities}
            transferTime={profile.transferTime}
          />
        </>
      )}
    </main>
  );
}

export default App;
