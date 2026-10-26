import type { JourneyResponse } from "../services/journeyResponse";
import StopFacilities from "./StopFacilities";

interface RouteResultProps {
  journey: JourneyResponse;
  originName: string;
  destinationName: string;
}

const WALK_MODE = "WALK";

const modeLabels: Record<string, string> = {
  RAIL: "Trein",
  BUS: "Bus",
  TRAM: "Tram",
  SUBWAY: "Metro",
  WALK: "Lopen",
};

const formatTime = (value: string): string =>
  new Intl.DateTimeFormat("nl-NL", {
    hour: "2-digit",
    minute: "2-digit",
    timeZone: "Europe/Amsterdam",
  }).format(new Date(value));

const RouteResult = ({
  journey,
  originName,
  destinationName,
}: RouteResultProps) => {
  const durationMinutes = Math.round(
    (new Date(journey.arrivalTime).getTime() -
      new Date(journey.departureTime).getTime()) /
      60_000,
  );

  return (
    <section className="route-result" aria-labelledby="route-title">
      <h1 id="route-title">Jouw route</h1>

      <div className="route-summary">
        <div>
          <strong>{formatTime(journey.departureTime)}</strong>
          <span>{originName}</span>
        </div>
        <span aria-hidden="true">→</span>
        <div>
          <strong>{formatTime(journey.arrivalTime)}</strong>
          <span>{destinationName}</span>
        </div>
      </div>

      <p>{durationMinutes} min reistijd</p>

      <h2>Reisstappen</h2>
      <ol>
        {journey.legs.map((leg, index) => (
          <li key={`${index}-${leg.from}-${leg.to}`}>
            <div>
              <strong>
                {leg.routeName ?? modeLabels[leg.mode] ?? leg.mode}
                {leg.tripNumber && ` ${leg.tripNumber}`}
              </strong>

              {leg.headsign && <p>Richting {leg.headsign}</p>}

              <p>{leg.from} → {leg.to}</p>

              <p>
                Vertrek:{" "}
                {leg.departureTime
                  ? formatTime(leg.departureTime)
                  : "tijd onbekend"}
                {leg.departurePlatform &&
                  ` · gepland spoor ${leg.departurePlatform}`}
              </p>

              {leg.mode !== WALK_MODE && (
                <StopFacilities facilities={leg.departureFacilities} />
              )}

              <p>
                Aankomst:{" "}
                {leg.arrivalTime
                  ? formatTime(leg.arrivalTime)
                  : "tijd onbekend"}
                {leg.arrivalPlatform &&
                  ` · gepland spoor ${leg.arrivalPlatform}`}
              </p>

              {leg.mode !== WALK_MODE && (
                <StopFacilities facilities={leg.arrivalFacilities} />
              )}
            </div>

            <span>{modeLabels[leg.mode] ?? leg.mode}</span>
          </li>
        ))}
    </ol>
    </section>
  );
};

export default RouteResult;






























// import type { DummyRoute } from "../types/route.ts";
// import { calculateTravelTime } from "../utils/time.ts";

// interface RouteResultProps {
//   route: DummyRoute;
//   departure: string;
//   destination: string;
//   selectedFacilities: string[];
//   transferTime: number;
// }

// function RouteResult({
//   route,
//   departure,
//   destination,
//   selectedFacilities,
//   transferTime,
// }: RouteResultProps) {
//   const departureStop = route.stops.find(
//     (stop) => stop.stopName === departure
//   );

//   const destinationStop = route.stops.find(
//     (stop) => stop.stopName === destination
//   );
// if (
//   departureStop &&
//   destinationStop &&
//   destinationStop.stopNumber <= departureStop.stopNumber
// ) {
//   return (
//     <section>
//       <h2>Reisresultaat</h2>
//       <p>Voor deze stations is geen geldige reis beschikbaar.</p>
//     </section>
//   );
// }
//   if (!departureStop || !destinationStop) {
//     return (
//       <section>
//         <h2>Reisresultaat</h2>
//         <p>Voor deze stations is nog geen route beschikbaar.</p>
//       </section>
//     );
//   }

//   const departureTime = departureStop.departureTime;
//   const arrivalTime = destinationStop.arrivalTime;

//   const stationFacilities = [
//     departureStop.facility,
//     destinationStop.facility,
//   ];

//   const availableFacilities = selectedFacilities.filter((facility) =>
//     stationFacilities.includes(facility)
//   );

//   const unavailableFacilities = selectedFacilities.filter(
//     (facility) => !stationFacilities.includes(facility)
//   );

//   return (
//     <section>
//       <h2>Reisresultaat</h2>

//       <h3>
//         {departure} → {destination}
//       </h3>

//       <p>
//         <strong>Vertrek:</strong> {departureTime}
//       </p>

//       <p>
//         <strong>Aankomst:</strong> {arrivalTime}
//       </p>

//       <p>
//         <strong>Reistijd:</strong>{" "}
//         {calculateTravelTime(departureTime, arrivalTime, transferTime)}
//       </p>

//       {transferTime > 0 && (
//         <p>
//           <strong>Extra overstaptijd:</strong> {transferTime} minuten
//         </p>
//       )}

//       <h3>Voorzieningen</h3>

//       {availableFacilities.length > 0 && (
//         <ul>
//           {availableFacilities.map((facility) => (
//             <li key={facility}>{facility}</li>
//           ))}
//         </ul>
//       )}

//       {unavailableFacilities.length > 0 && (
//         <p>
//           De volgende gekozen voorzieningen zijn niet aanwezig op de gekozen stations: {unavailableFacilities.join(", ")}.
//         </p>
//       )}
//     </section>
//   );
// }

// export default RouteResult;