import { useState } from "react";
import "./App.css";
import dummyRoute from "./data/dummydata.json";
import type { DummyRoute } from "./types/route";
import RouteResult from "./components/RouteResult";
import RouteForm from "./components/RouteForm";

const route: DummyRoute = dummyRoute;

const stations = route.stops.map((stop) => stop.stopName);
console.log(stations);
type TimeMode = "now" | "departure" | "arrival";

function App() {
  const [departure, setDeparture] = useState("");
  const [destination, setDestination] = useState("");

  const [timeMode, setTimeMode] =
    useState<TimeMode>("now");

  const [date, setDate] = useState("");
  const [time, setTime] = useState("");

  const [selectedFacilities, setSelectedFacilities] =
    useState<string[]>([]);

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
    });
  };

 return (
   <main>
     {!showResults ? (
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
         />
       </>
     )}
   </main>
 );
}

export default App;