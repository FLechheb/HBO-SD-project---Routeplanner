
import type { JourneyResponse } from "../services/journeyResponse";

export interface SavedJourney {
    journey: JourneyResponse;
    originName: string;
    destinationName: string;
}

const STORAGE_KEY = "ov-reisplanner-saved-journey";

export function saveJourney(route: SavedJourney): void {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(route));
}

export function loadJourney(): SavedJourney | null {
    try {
        const storedJourney = localStorage.getItem(STORAGE_KEY);

        if (!storedJourney) {
            return null;
        }

        return JSON.parse(storedJourney) as SavedJourney;
    } catch {
        return null;
    }
}
