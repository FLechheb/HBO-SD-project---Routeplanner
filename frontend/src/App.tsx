import { useState , useEffect } from "react";
import "./App.css";
import dummyRoute from "./Data/dummydata.json";
import type { DummyRoute } from "./types/route.ts";
import RouteResult from "./components/RouteResult.tsx";
import RouteForm from "./components/Routeform";
import type { ApiStop } from "./types/route.ts";
import { fetchStops } from "./services/stopService.ts"

const route: DummyRoute = dummyRoute;


type TimeMode = "now" | "departure" | "arrival";

function App() {
  const [departure, setDeparture] = useState("");
  const [destination, setDestination] = useState("");
  const [stops, setStops] = useState<ApiStop[]>([]);
  useEffect(() => {
    fetchStops()
        .then(setStops)
        .catch(console.error);
}, []);

  const [timeMode, setTimeMode] =
    useState<TimeMode>("now"); 

  const [date, setDate] = useState("");
  const [time, setTime] = useState("");

  const stations = stops.map((stop) => stop.name);

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