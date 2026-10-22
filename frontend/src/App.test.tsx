
// @vitest-environment jsdom

import { beforeEach, describe, expect, it, vi } from "vitest";
import { fireEvent, render, screen } from "@testing-library/react";
import App from "./App";
import { loadJourney, saveJourney } from "./utils/journeyStorage";

vi.mock("./components/Routeform", () => ({
    default: ({ onSubmit }: { onSubmit: (event: React.FormEvent) => void }) => (
        <form
            onSubmit={(event) => {
                event.preventDefault();
                onSubmit(event);
            }}
        >
            <button type="submit">Plan mijn reis</button>
        </form>
    ),
}));

vi.mock("./components/Routeform", () => ({
    default: ({
                  onSubmit,
                  setDeparture,
                  setDestination,
              }: {
        onSubmit: (event: React.FormEvent) => void;
        setDeparture: (station: { gtfsId: string; name: string }) => void;
        setDestination: (station: { gtfsId: string; name: string }) => void;
    }) => (
        <form
            onSubmit={(event) => {
                event.preventDefault();
                onSubmit(event);
            }}
        >
            <button
                type="button"
                onClick={() => {
                    setDeparture({
                        gtfsId: "NL:station:8000561",
                        name: "Rotterdam Centraal",
                    });
                    setDestination({
                        gtfsId: "NL:station:8400621",
                        name: "Utrecht Centraal",
                    });
                }}
            >
                Stel teststations in
            </button>

            <button type="submit">Plan mijn reis</button>
        </form>
    ),
}));

const mockPlanJourney = vi.hoisted(() => vi.fn());

vi.mock("./services/journeyResponse", () => ({
    planJourney: mockPlanJourney,
}));

describe("App offline gedrag", () => {
    beforeEach(() => {
        localStorage.clear();
        vi.restoreAllMocks();

        vi.stubGlobal("navigator", {
            ...navigator,
            onLine: false,
        });
    });

    it("toont de opgeslagen route offline zonder de backend aan te roepen", () => {
        saveJourney({
            journey: {
                departureTime: "2026-10-22T10:00:00+02:00",
                arrivalTime: "2026-10-22T11:00:00+02:00",
                legs: [],
            },
            originName: "Rotterdam Centraal",
            destinationName: "Utrecht Centraal",
        });

        render(<App />);

        fireEvent.click(screen.getByRole("button", { name: /Reis aanpassen/ }));
        fireEvent.click(screen.getByRole("button", { name: "Stel teststations in" }));
        fireEvent.click(screen.getByRole("button", { name: "Plan mijn reis" }));

        expect(screen.getByText("Rotterdam Centraal")).toBeTruthy();
        expect(screen.getByText("Utrecht Centraal")).toBeTruthy();
        expect(loadJourney()).not.toBeNull();
        expect(mockPlanJourney).not.toHaveBeenCalled();
    });
});
