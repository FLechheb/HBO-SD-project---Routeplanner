import { useEffect, useState, type FormEvent } from "react";
import { getNeeds, type Need } from "../services/needsService";

interface ProfilePageProps {
  selectedFacilities: string[];
  transferTime: number;
  onSave: (facilities: string[], transferTime: number) => void;
  onResetFacilities: () => void;
  onBack: () => void;
}

type CatalogueState =
  | { status: "loading" }
  | { status: "ready"; facilities: Need[] }
  | { status: "error"; message: string };

const transferTimeOptions = [0, 5, 10, 15];
const legacyFacilityIds = new Set(["lift", "roltrap", "hellingbaan", "geleidelijn"]);

function ProfilePage({
  selectedFacilities,
  transferTime,
  onSave,
  onResetFacilities,
  onBack,
}: ProfilePageProps) {
  // Keep edits local until the user explicitly saves the profile.
  const [facilitiesDraft, setFacilitiesDraft] =
    useState(selectedFacilities);
  const [initialFacilities] = useState(selectedFacilities);
  const [transferTimeDraft, setTransferTimeDraft] =
    useState(transferTime);
  const [catalogue, setCatalogue] = useState<CatalogueState>({ status: "loading" });
  const [attempt, setAttempt] = useState(0);
  const [notice, setNotice] = useState("");

  useEffect(() => {
    const controller = new AbortController();

    getNeeds(controller.signal)
      .then((needs) => {
        if (controller.signal.aborted) return;
        const facilities = needs.filter((need) => need.category === "FACILITY");
        if (initialFacilities.some((id) => legacyFacilityIds.has(id))) {
          try {
            onResetFacilities();
          } catch {
            setCatalogue({
              status: "error",
              message: "Je oude voorkeuren konden niet worden gewist. Probeer opnieuw.",
            });
            return;
          }
          setFacilitiesDraft([]);
          setNotice("Je oude voorkeuren zijn gewist. Kies je benodigde voorzieningen opnieuw.");
        } else {
          const availableTypes = new Set(facilities.map((need) => need.type));
          setFacilitiesDraft((current) => current.filter((id) => availableTypes.has(id)));
          if (initialFacilities.some((id) => !availableTypes.has(id))) {
            setNotice("Sommige opgeslagen voorzieningen zijn niet meer beschikbaar. Bij opslaan worden ze verwijderd.");
          }
        }
        setCatalogue({
          status: "ready",
          facilities,
        });
      })
      .catch(() => {
        if (!controller.signal.aborted) {
          setCatalogue({
            status: "error",
            message: "De voorzieningen konden niet worden geladen.",
          });
        }
      });

    return () => controller.abort();
  }, [attempt, initialFacilities, onResetFacilities]);

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
    if (catalogue.status !== "ready") return;
    // App.tsx owns the profile state and decides how it is persisted.
    onSave(facilitiesDraft, transferTimeDraft);
  };

  return (
    <section className="profile-page">
      <h2>Mijn profiel</h2>

      <form className="profile-form" onSubmit={handleSubmit}>
        <fieldset>
          <legend>Benodigde voorzieningen</legend>

          {catalogue.status === "loading" && <p role="status">Voorzieningen worden geladen...</p>}
          {notice && <p role="status">{notice}</p>}
          {catalogue.status === "error" && (
            <>
              <p role="alert">{catalogue.message}</p>
              <button type="button" onClick={() => {
                setCatalogue({ status: "loading" });
                setAttempt((current) => current + 1);
              }}>
                Opnieuw proberen
              </button>
            </>
          )}

          {catalogue.status === "ready" && catalogue.facilities.length === 0 && (
            <p role="status">Er zijn geen voorzieningen beschikbaar.</p>
          )}
          {catalogue.status === "ready" && catalogue.facilities.map((facility) => (
            <label key={facility.type}>
              <input
                type="checkbox"
                value={facility.type}
                checked={facilitiesDraft.includes(facility.type)}
                onChange={() => toggleFacility(facility.type)}
              />
              {facility.description}
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
          <button type="submit" disabled={catalogue.status !== "ready"}>Profiel opslaan</button>
          <button type="button" onClick={onBack}>
            Annuleren
          </button>
        </div>
      </form>
    </section>
  );
}

export default ProfilePage;