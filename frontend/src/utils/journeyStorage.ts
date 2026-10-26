
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

function isSavedJourney(value: unknown): value is SavedJourney {
    if (typeof value !== "object" || value === null) {
        return false;
    }

    const saved = value as Record<string, unknown>;

    if (
        typeof saved.originName !== "string" ||
        typeof saved.destinationName !== "string" ||
        typeof saved.journey !== "object" ||
        saved.journey === null
    ) {
        return false;
    }

    const journey = saved.journey as Record<string, unknown>;

    return (
        typeof journey.departureTime === "string" &&
        typeof journey.arrivalTime === "string" &&
        Array.isArray(journey.legs)
    );
}

export function loadJourney(): SavedJourney | null {
    try {
        const storedJourney = localStorage.getItem(STORAGE_KEY);

        if (!storedJourney) {
            return null;
        }

        const parsed: unknown = JSON.parse(storedJourney);

        return isSavedJourney(parsed) ? parsed : null;
    } catch {
        return null;
    }
}
