interface RouteFormProps {
    stations: string[];
  departure: string;
  destination: string;
  timeMode: "now" | "departure" | "arrival";
  date: string;
  time: string;
  selectedFacilities: string[];

  setDeparture: (value: string) => void;
  setDestination: (value: string) => void;
  setTimeMode: (value: "now" | "departure" | "arrival") => void;
  setDate: (value: string) => void;
  setTime: (value: string) => void;
  toggleFacility: (facilityId: string) => void;

  onSubmit: (event: React.FormEvent) => void;
}

const facilities = [
  { id: "lift", name: "Lift" },
  { id: "roltrap", name: "Roltrap" },
  { id: "hellingbaan", name: "Hellingbaan" },
  { id: "geleidelijn", name: "Geleidelijn" },
];

function RouteForm({
    stations,
  departure,
  destination,
  timeMode,
  date,
  time,
  selectedFacilities,
  setDeparture,
  setDestination,
  setTimeMode,
  setDate,
  setTime,
  toggleFacility,
  onSubmit,
}: RouteFormProps) {
  return (
    <form onSubmit={onSubmit}>
      <div>
        <label htmlFor="departure">Vertrekstation</label>
       <select
         id="departure"
         value={departure}
         onChange={(event) => setDeparture(event.target.value)}
       >
         <option value="">Kies een vertrekstation</option>

         {stations
           .filter((station) => station !== departure)
           .map((station) => (
             <option key={station} value={station}>
               {station}
             </option>
           ))}
       </select>
      </div>

      <div>
        <label htmlFor="destination">Bestemming</label>
        <select
          id="destination"
          value={destination}
          onChange={(event) => setDestination(event.target.value)}
        >
          <option value="">Kies een bestemmingsstation</option>

          {stations.map((station) => (
            <option key={station} value={station}>
              {station}
            </option>
          ))}
        </select>
      </div>

      <fieldset>
        <legend>Wanneer wil je reizen?</legend>

     <div className="time-options">
       <div>
         <input
           id="now"
           type="radio"
           name="timeMode"
           checked={timeMode === "now"}
           onChange={() => setTimeMode("now")}
         />
         <label htmlFor="now">Nu</label>
       </div>

       <div>
         <input
           id="departureTime"
           type="radio"
           name="timeMode"
           checked={timeMode === "departure"}
           onChange={() => setTimeMode("departure")}
         />
         <label htmlFor="departureTime">Vertrek om</label>
       </div>

       <div>
         <input
           id="arrivalTime"
           type="radio"
           name="timeMode"
           checked={timeMode === "arrival"}
           onChange={() => setTimeMode("arrival")}
         />
         <label htmlFor="arrivalTime">Aankomst om</label>
       </div>
     </div>
      </fieldset>

      {timeMode !== "now" && (
        <div className="date-time-row">
          <div>
            <label htmlFor="date">Datum</label>
            <input
              id="date"
              type="date"
              value={date}
              onChange={(event) => setDate(event.target.value)}
            />
          </div>

          <div>
            <label htmlFor="time">Tijd</label>
            <input
              id="time"
              type="time"
              value={time}
              onChange={(event) => setTime(event.target.value)}
            />
          </div>
        </div>
      )}

      <fieldset>
        <legend>Benodigde voorzieningen</legend>

        {facilities.map((facility) => (
          <div key={facility.id}>
            <input
              id={facility.id}
              type="checkbox"
              checked={selectedFacilities.includes(facility.id)}
              onChange={() => toggleFacility(facility.id)}
            />
            <label htmlFor={facility.id}>{facility.name}</label>
          </div>
        ))}
      </fieldset>

      <button type="submit">Plan mijn reis</button>
    </form>
  );
}

export default RouteForm;