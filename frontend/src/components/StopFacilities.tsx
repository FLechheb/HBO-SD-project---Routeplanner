import type { Facility } from "../services/journeyResponse";

interface StopFacilitiesProps {
  facilities: Facility[] | null;
}

const facilityLabels: Record<string, string> = {
  ELEVATOR: "Lift",
  ESCALATOR: "Roltrap",
  STAIRS: "Trap",
  INFO_BOARD: "Informatiebord",
  INFO_SPEAKER: "Omroepinstallatie",
};

const StopFacilities = ({ facilities }: StopFacilitiesProps) => {
  if (facilities === null) {
    return (
      <p className="stop-facilities-message">
        Geen gegevens over voorzieningen op dit perron
      </p>
    );
  }

  if (facilities.length === 0) {
    return (
      <p className="stop-facilities-message">
        Geen voorzieningen op dit perron
      </p>
    );
  }

  return (
    <ul className="stop-facilities" aria-label="Voorzieningen op dit perron">
      {facilities.map((facility, index) => (
        <li
          key={`${facility.type}-${index}`}
          className={facility.operational ? undefined : "facility-out-of-order"}
        >
          {facilityLabels[facility.type] ?? facility.type}
          {!facility.operational && " (buiten gebruik)"}
        </li>
      ))}
    </ul>
  );
};

export default StopFacilities;
