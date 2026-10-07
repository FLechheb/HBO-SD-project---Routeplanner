import { useEffect, useState } from "react";
import { searchStations, type Station } from "../services/stationService";

interface StationPickerProps {
  id: string;
  label: string;
  selected: Station | null;
  onSelect: (station: Station | null) => void;
}

function StationPicker({
                         id,
                         label,
                         selected,
                         onSelect,
                       }: StationPickerProps) {
  const [query, setQuery] = useState(selected?.name ?? "");
  const [results, setResults] = useState<Station[]>([]);
  const [error, setError] = useState("");

  useEffect(() => {
    if (query.trim().length < 2 || selected?.name === query) {
      return;
    }

    let active = true;
    const timer = window.setTimeout(() => {
      searchStations(query.trim())
        .then((stations) => {
          if (active) setResults(stations);
        })
        .catch(() => {
          if (active) setError("Stations zoeken is niet gelukt.");
        });
    }, 300);

    return () => {
      active = false;
      window.clearTimeout(timer);
    };
  }, [query, selected]);

  return (
    <div>
      <label htmlFor={id}>{label}</label>
      <input
        id={id}
        value={query}
        autoComplete="off"
        onChange={(event) => {
          setQuery(event.target.value);
          setResults([]);
          setError("");
          onSelect(null);
        }}
      />

      {error && <p role="alert">{error}</p>}

      {results.length > 0 && (
        <ul>
          {results.map((station) => (
            <li key={station.gtfsId}>
              <button
                type="button"
                onClick={() => {
                  setQuery(station.name);
                  setResults([]);
                  onSelect(station);
                }}
              >
                {station.name}
              </button>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

export default StationPicker;
