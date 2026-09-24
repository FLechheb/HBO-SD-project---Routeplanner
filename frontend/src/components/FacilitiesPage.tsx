interface FacilitiesPageProps {
    onBack: () => void;
    onPlanJourney: () => void;
    selectedFacilities: string[];
    toggleFacility: (facilityId: string) => void;
    transferTime: number;
    setTransferTime: (value: number) => void;
  }

function FacilitiesPage({
  onBack,
  onPlanJourney,
  selectedFacilities,
  toggleFacility,
  transferTime,
  setTransferTime,
}: FacilitiesPageProps) {

  return (
    <section>
    <button type="button" onClick={onBack}>
      ← Terug
    </button>

      <h2>Extra voorzieningen</h2>

      <p>
        Kies hier welke voorzieningen je nodig hebt tijdens je reis.
      </p>

      <label>
        <input
          type="checkbox"
          checked={selectedFacilities.includes("lift")}
          onChange={() => toggleFacility("lift")}
        />
        Lift
      </label>

      <label>
        <input type="checkbox"
         checked={selectedFacilities.includes("Roltrap")}
         onChange={() => toggleFacility("Roltrap")}
        />
        Roltrap
      </label>

      <label>
        <input type="checkbox"
         checked={selectedFacilities.includes("Hellingbaan")}
         onChange={() => toggleFacility("Hellingbaan")}
         />
        Hellingbaan
      </label>

      <label>
        <input type="checkbox"
         checked={selectedFacilities.includes("Geleidelijn")}
         onChange={() => toggleFacility("Geleidelijn")}
         />
        Geleidelijn
      </label>

<fieldset>
  <legend>Extra tijd nodig voor overstappen?</legend>

  <label>
    <input
      type="radio"
      name="transferTime"
      checked={transferTime === 0}
      onChange={() => setTransferTime(0)}
    />
    Geen extra tijd
  </label>

  <label>
   <input
     type="radio"
     name="transferTime"
     checked={transferTime === 5}
     onChange={() => setTransferTime(5)}
   />
    5 minuten extra
  </label>

  <label>
    <input
      type="radio"
      name="transferTime"
      checked={transferTime === 10}
      onChange={() => setTransferTime(10)}
    />
    10 minuten extra
  </label>

  <label>
    <input
      type="radio"
      name="transferTime"
      checked={transferTime === 15}
      onChange={() => setTransferTime(15)}
    />
    15 minuten extra
  </label>
</fieldset>

      <button type="button" onClick={onPlanJourney}>
        Plan mijn reis
      </button>
    </section>
  );
}

export default FacilitiesPage;