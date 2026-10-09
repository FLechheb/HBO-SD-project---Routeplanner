import { useEffect, useState } from "react";
import { searchStations, type Station } from "../services/stationService";
import {
  loadRecentStations,
  rememberStation,
} from "../utils/recentStations";

interface StationPickerProps {
  id: string;
  label: string;
  selected: Station | null;
  onSelect: (station: Station | null) => void;
}

const StationPicker = ({
  id,
  label,
  selected,
  onSelect,
}: StationPickerProps) => {
  // Wat de gebruiker in het invoerveld ziet en typt.
  const [query, setQuery] = useState(selected?.name ?? "");

  // Zoekresultaten van de API en eerder gekozen stations uit localStorage.
  const [results, setResults] = useState<Station[]>([]);
  const [recent, setRecent] = useState<Station[]>([]);

  const [isOpen, setIsOpen] = useState(false);
  const [activeIndex, setActiveIndex] = useState(-1);
  const [error, setError] = useState("");

  useEffect(() => {
    // Zoek pas vanaf twee tekens. Een al gekozen station hoef je niet opnieuw te zoeken.
    if (query.trim().length < 2 || selected?.name === query) return;

    let active = true;

    // Wacht even na het typen, zodat niet voor ieder teken een verzoek vertrekt.
    const timer = window.setTimeout(() => {
      searchStations(query.trim())
        .then((stations) => {
          if (active) {
            setResults(stations);
            setError("");
          }
        })
        .catch(() => {
          if (active) setError("Stations zoeken is niet gelukt.");
        });
    }, 300);

    // Voorkom dat een oud zoekverzoek de resultaten van nieuwe invoer overschrijft.
    return () => {
      active = false;
      window.clearTimeout(timer);
    };
  }, [query, selected]);

  // Bij een leeg veld toon je recente keuzes; anders maximaal tien API-resultaten.
  const options =
    query.trim() === ""
      ? recent
      : results.slice(0, 10);

  const chooseStation = (station: Station) => {
    setQuery(station.name);
    setResults([]);
    setIsOpen(false);
    setActiveIndex(-1);

    // Geef het gekozen station inclusief gtfsId door aan het formulier.
    onSelect(station);

    // Bewaar de keuze voor de volgende keer dat de dropdown wordt geopend.
    rememberStation(station);
    setRecent(loadRecentStations());
  };

  return (
    <div
      className="station-picker"
      onBlur={(event) => {
        // Sluit pas als de focus de hele picker verlaat, niet bij een klik op een optie.
        if (!event.currentTarget.contains(event.relatedTarget)) {
          setIsOpen(false);
        }
      }}
    >
      <label htmlFor={id}>{label}</label>

      <input
        id={id}
        value={query}
        autoComplete="off"
        role="combobox"
        aria-autocomplete="list"
        aria-expanded={isOpen && options.length > 0}
        aria-controls={
          isOpen && options.length > 0 ? `${id}-options` : undefined
        }
        aria-activedescendant={
          isOpen && activeIndex >= 0
            ? `${id}-option-${activeIndex}`
            : undefined
        }
        onFocus={() => {
          setRecent(loadRecentStations());
          setIsOpen(true);
        }}
        onChange={(event) => {
          setQuery(event.target.value);
          setResults([]);
          setError("");
          setActiveIndex(-1);
          setIsOpen(true);

          // De oude selectie is niet meer geldig zodra de gebruiker verder typt.
          onSelect(null);
        }}
        onKeyDown={(event) => {
          if (!isOpen || options.length === 0) return;

          // Pijltjes toetsen kiezen een optie; Enter bevestigt de actieve optie.
          if (event.key === "ArrowDown") {
            event.preventDefault();
            setActiveIndex((index) => (index + 1) % options.length);
          } else if (event.key === "ArrowUp") {
            event.preventDefault();
            setActiveIndex((index) =>
              index <= 0 ? options.length - 1 : index - 1,
            );
          } else if (event.key === "Enter" && activeIndex >= 0) {
            event.preventDefault();
            chooseStation(options[activeIndex]);
          } else if (event.key === "Escape") {
            setIsOpen(false);
            setActiveIndex(-1);
          }
        }}
      />

      {error && <p role="alert">{error}</p>}

      {isOpen && options.length > 0 && (
        <div
          id={`${id}-options`}
          className="station-options"
          role="listbox"
          aria-label={
            query.trim() === "" ? "Recente stations" : "Zoekresultaten"
          }
        >
          {options.map((station, index) => (
            <button
              id={`${id}-option-${index}`}
              key={station.gtfsId}
              type="button"
              role="option"
              aria-selected={index === activeIndex}
              onMouseEnter={() => setActiveIndex(index)}
              onClick={() => chooseStation(station)}
            >
              {station.name}
            </button>
          ))}
        </div>
      )}
    </div>
  );
};

export default StationPicker;

























// import { useEffect, useState } from "react";
// import { searchStations, type Station } from "../services/stationService";

// interface StationPickerProps {
//   id: string;
//   label: string;
//   selected: Station | null;
//   onSelect: (station: Station | null) => void;
// }

// function StationPicker({
//                          id,
//                          label,
//                          selected,
//                          onSelect,
//                        }: StationPickerProps) {
//   const [query, setQuery] = useState(selected?.name ?? "");
//   const [results, setResults] = useState<Station[]>([]);
//   const [error, setError] = useState("");

//   useEffect(() => {
//     if (query.trim().length < 2 || selected?.name === query) {
//       return;
//     }

//     let active = true;
//     const timer = window.setTimeout(() => {
//       searchStations(query.trim())
//         .then((stations) => {
//           if (active) setResults(stations);
//         })
//         .catch(() => {
//           if (active) setError("Stations zoeken is niet gelukt.");
//         });
//     }, 300);

//     return () => {
//       active = false;
//       window.clearTimeout(timer);
//     };
//   }, [query, selected]);

//   return (
//     <div>
//       <label htmlFor={id}>{label}</label>
//       <input
//         id={id}
//         value={query}
//         autoComplete="off"
//         onChange={(event) => {
//           setQuery(event.target.value);
//           setResults([]);
//           setError("");
//           onSelect(null);
//         }}
//       />

//       {error && <p role="alert">{error}</p>}

//       {results.length > 0 && (
//         <ul>
//           {results.map((station) => (
//             <li key={station.gtfsId}>
//               <button
//                 type="button"
//                 onClick={() => {
//                   setQuery(station.name);
//                   setResults([]);
//                   onSelect(station);
//                 }}
//               >
//                 {station.name}
//               </button>
//             </li>
//           ))}
//         </ul>
//       )}
//     </div>
//   );
// }

// export default StationPicker;
