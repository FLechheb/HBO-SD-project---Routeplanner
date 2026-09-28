import { useState } from "react";
import "./App.css";
import dummyRoute from "./Data/dummydata.json";
import type { DummyRoute } from "./types/route.ts";
import RouteResult from "./components/RouteResult.tsx";
import RouteForm from "./components/Routeform";
import FacilitiesPage from "./components/FacilitiesPage";

const route: DummyRoute = dummyRoute;

const stations = route.stops.map((stop) => stop.stopName);
console.log(stations);
type TimeMode = "now" | "departure" | "arrival";

function App() {
  const [departure, setDeparture] = useState("");
  const [destination, setDestination] = useState("");
  const [showFacilities, setShowFacilities] = useState(false);

  const [timeMode, setTimeMode] =
    useState<TimeMode>("now");

  const [date, setDate] = useState("");
  const [time, setTime] = useState("");

  const [selectedFacilities, setSelectedFacilities] =
    useState<string[]>([]);

  const [transferTime, setTransferTime] = useState(0);

  const [showResults, setShowResults] =
    useState(false);

  const toggleFacility = (facilityId: string) => {
    setSelectedFacilities((current) => {
      if (current.includes(facilityId)) {
        return current.filter(
          (id) => id !== facilityId
        );
      }

      return [...current, facilityId];
    });
  };

const openFacilities = () => {
  setShowFacilities(true);
};

  const planJourney = (
    event: React.FormEvent
  ) => {
    event.preventDefault();

    setShowResults(true);

    console.log({
      departure,
      destination,
      timeMode,
      date,
      time,
      selectedFacilities,
      transferTime,
    });
  };

 return (
   <main>
     {showFacilities ? (
      <FacilitiesPage
        onBack={() => setShowFacilities(false)}
        onPlanJourney={() => {
          setShowFacilities(false);
          setShowResults(true);
        }}
        selectedFacilities={selectedFacilities}
        toggleFacility={toggleFacility}
        transferTime={transferTime}
        setTransferTime={setTransferTime}
      />
     ) : !showResults ? (
       <>
         <h1>OV Reisplanner</h1>

         <p>
           Plan een reis met het openbaar vervoer
           op basis van jouw toegankelijkheidsbehoeften.
         </p>

         <RouteForm
           stations={stations}
           departure={departure}
           destination={destination}
           timeMode={timeMode}
           date={date}
           time={time}
           selectedFacilities={selectedFacilities}
           setDeparture={setDeparture}
           setDestination={setDestination}
           setTimeMode={setTimeMode}
           setDate={setDate}
           setTime={setTime}
           toggleFacility={toggleFacility}
           onSubmit={planJourney}
           onFacilitiesClick={openFacilities}
         />
       </>
     ) : (
       <>
         <button type="button" onClick={() => setShowResults(false)}>
           ← Reis aanpassen
         </button>

         <RouteResult
           route={route}
           departure={departure}
           destination={destination}
           selectedFacilities={selectedFacilities}
           transferTime={transferTime}
         />
       </>
     )}
   </main>
 );
}

export default App;