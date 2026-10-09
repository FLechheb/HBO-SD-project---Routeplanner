import { useState, type FormEvent } from "react";

interface ProfilePageProps {
  selectedFacilities: string[];
  transferTime: number;
  onSave: (facilities: string[], transferTime: number) => void;
  onBack: () => void;
}

const facilities = [
  { id: "lift", label: "Lift" },
  { id: "roltrap", label: "Roltrap" },
  { id: "hellingbaan", label: "Hellingbaan" },
  { id: "geleidelijn", label: "Geleidelijn" },
];

const transferTimeOptions = [0, 5, 10, 15];

function ProfilePage({
  selectedFacilities,
  transferTime,
  onSave,
  onBack,
}: ProfilePageProps) {
  // Keep edits local until the user explicitly saves the profile.
  const [facilitiesDraft, setFacilitiesDraft] =
    useState(selectedFacilities);
  const [transferTimeDraft, setTransferTimeDraft] =
    useState(transferTime);

  const toggleFacility = (facilityId: string) => {
    // Create a new array instead of mutating React state directly. 
    setFacilitiesDraft((current) =>
      current.includes(facilityId)
        ? current.filter((id) => id !== facilityId)
        : [...current, facilityId],
    );
  };

  const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    // App.tsx owns the profile state and decides how it is persisted.
    onSave(facilitiesDraft, transferTimeDraft);
  };

  return (
    <section className="profile-page">
      <h2>Mijn profiel</h2>

      <form className="profile-form" onSubmit={handleSubmit}>
        <fieldset>
          <legend>Benodigde voorzieningen</legend>

          {facilities.map((facility) => (
            <label key={facility.id}>
              <input
                type="checkbox"
                checked={facilitiesDraft.includes(facility.id)}
                onChange={() => toggleFacility(facility.id)}
              />
              {facility.label}
            </label>
          ))}
        </fieldset>

        <fieldset>
          <legend>Extra overstaptijd</legend>

          {transferTimeOptions.map((minutes) => (
            <label key={minutes}>
              <input
                type="radio"
                name="profileTransferTime"
                checked={transferTimeDraft === minutes}
                onChange={() => setTransferTimeDraft(minutes)}
              />
              {minutes === 0
                ? "Geen extra tijd"
                : `${minutes} minuten extra`}
            </label>
          ))}
        </fieldset>

        <div className="profile-actions">
          <button type="submit">Profiel opslaan</button>
          <button type="button" onClick={onBack}>
            Annuleren
          </button>
        </div>
      </form>
    </section>
  );
}

export default ProfilePage;