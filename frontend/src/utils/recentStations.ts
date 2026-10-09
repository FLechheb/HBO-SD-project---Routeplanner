import type { Station } from "../services/stationService";

const STORAGE_KEY = "ov-reisplanner-recent-stations";
const MAX_RECENT = 5;

export const loadRecentStations = (): Station[] => {
  try {
    const value: unknown = JSON.parse(localStorage.getItem(STORAGE_KEY) ?? "[]");

    if (!Array.isArray(value)) return [];

    return value.filter(
      (item): item is Station =>
        typeof item === "object" &&
        item !== null && 
        typeof item.gtfsId === "string" &&
        typeof item.name === "string",
    ).slice(0, MAX_RECENT);
  } catch {
    return [];
  }
};

export const rememberStation = (station: Station): void => {
  try {
    const recent = loadRecentStations().filter(
      (item) => item.gtfsId !== station.gtfsId,
    );

    localStorage.setItem(
      STORAGE_KEY,
      JSON.stringify([station, ...recent].slice(0, MAX_RECENT)),
    );
  } catch {
    
  }
}