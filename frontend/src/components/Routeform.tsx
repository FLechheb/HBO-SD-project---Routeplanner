import StationPicker from "./StationPicker";
import type { Station } from "../services/stationService";

interface RouteFormProps {
  departure: Station | null;
  destination: Station | null;
  timeMode: "now" | "departure" | "arrival";
  date: string;
  time: string;

  setDeparture: (station: Station | null) => void;
  setDestination: (station: Station | null) => void;
  setTimeMode: (value: "now" | "departure" | "arrival") => void;
  setDate: (value: string) => void;
  setTime: (value: string) => void;

  onSubmit: (event: React.FormEvent) => void;
  onProfileClick: () => void;
}


function RouteForm({
  departure,
  destination,
  timeMode,
  date,
  time,
  setDeparture,
  setDestination,
  setTimeMode,
  setDate,
  setTime,
  onSubmit,
  onProfileClick,
}: RouteFormProps) {
  return (
    <form onSubmit={onSubmit}>
      <StationPicker
        id="departure"
        label="Vertrekstation"
        selected={departure}
        onSelect={setDeparture}
      />

      <StationPicker
        id="destination"
        label="Bestemming"
        selected={destination}
        onSelect={setDestination}
      />

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



     <div className="form-actions">
      <button type="submit">
        Plan mijn reis
      </button>

      <button type="button" onClick={onProfileClick}>
        Mijn profiel
      </button>
    </div>

    </form>
  );
}

export default RouteForm;
