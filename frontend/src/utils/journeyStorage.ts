
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

    if (
        typeof journey.departureTime !== "string" ||
        typeof journey.arrivalTime !== "string" ||
        !Array.isArray(journey.legs)
    ) {
        return false;
    }

    return journey.legs.every((leg: unknown) => {
        if (typeof leg !== "object" || leg === null) {
            return false;
        }

        const item = leg as Record<string, unknown>;

        return (
            typeof item.mode === "string" &&
            (typeof item.headsign === "string" || item.headsign === null) &&
            (typeof item.departureTime === "string" || item.departureTime === null) &&
            (typeof item.arrivalTime === "string" || item.arrivalTime === null) &&
            (typeof item.routeName === "string" || item.routeName === null) &&
            (typeof item.tripNumber === "string" || item.tripNumber === null) &&
            typeof item.from === "string" &&
            (typeof item.departurePlatform === "string" ||
                item.departurePlatform === null) &&
            typeof item.to === "string" &&
            (typeof item.arrivalPlatform === "string" ||
                item.arrivalPlatform === null)
        );
    });
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
