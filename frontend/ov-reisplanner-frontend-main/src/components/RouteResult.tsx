import type { DummyRoute } from "../types/route";
import { calculateTravelTime } from "../utils/time";

interface RouteResultProps {
  route: DummyRoute;
  departure: string;
  destination: string;
  selectedFacilities: string[];
}

function RouteResult({
  route,
  departure,
  destination,
  selectedFacilities,
}: RouteResultProps) {
  const departureStop = route.stops.find(
    (stop) => stop.stopName === departure
  );

  const destinationStop = route.stops.find(
    (stop) => stop.stopName === destination
  );
if (
  departureStop &&
  destinationStop &&
  destinationStop.stopNumber <= departureStop.stopNumber
) {
  return (
    <section>
      <h2>Reisresultaat</h2>
      <p>Voor deze stations is geen geldige reis beschikbaar.</p>
    </section>
  );
}
  if (!departureStop || !destinationStop) {
    return (
      <section>
        <h2>Reisresultaat</h2>
        <p>Voor deze stations is nog geen route beschikbaar.</p>
      </section>
    );
  }

  const departureTime = departureStop.departureTime;
  const arrivalTime = destinationStop.arrivalTime;

  const stationFacilities = [
    departureStop.facility,
    destinationStop.facility,
  ];

  const availableFacilities = selectedFacilities.filter((facility) =>
    stationFacilities.includes(facility)
  );

  const unavailableFacilities = selectedFacilities.filter(
    (facility) => !stationFacilities.includes(facility)
  );

  return (
    <section>
      <h2>Reisresultaat</h2>

      <h3>
        {departure} → {destination}
      </h3>

      <p>
        <strong>Vertrek:</strong> {departureTime}
      </p>

      <p>
        <strong>Aankomst:</strong> {arrivalTime}
      </p>

      <p>
        <strong>Reistijd:</strong>{" "}
        {calculateTravelTime(departureTime, arrivalTime)}
      </p>

      <h3>Voorzieningen</h3>

      {availableFacilities.length > 0 && (
        <ul>
          {availableFacilities.map((facility) => (
            <li key={facility}>{facility}</li>
          ))}
        </ul>
      )}

      {unavailableFacilities.length > 0 && (
        <p>
          De volgende gekozen voorzieningen zijn niet aanwezig op de gekozen stations: {unavailableFacilities.join(", ")}.
        </p>
      )}
    </section>
  );
}

export default RouteResult;