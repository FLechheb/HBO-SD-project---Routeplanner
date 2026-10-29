// @vitest-environment jsdom

import { cleanup, fireEvent, render, screen } from "@testing-library/react";
import { afterEach, beforeEach, describe, expect, it, vi } from "vitest";
import App from "./App";
import { loadProfile, saveProfile } from "./utils/profileStorage";

const stations = [
  { gtfsId: "NL:stoparea:17843", name: "Rotterdam Centraal" },
  { gtfsId: "NL:stoparea:18188", name: "Utrecht Centraal" },
];

const journey = {
  departureTime: "2026-10-29T12:00:00+01:00",
  arrivalTime: "2026-10-29T13:00:00+01:00",
  legs: [],
};

function expectPlanningRequest(extraTransferMinutes: number, selectedFacilities: string[]) {
  expect(fetch).toHaveBeenLastCalledWith(
    expect.stringContaining("/api/routes/plan"),
    expect.objectContaining({
      method: "POST",
      body: JSON.stringify({
        originStationId: "NL:stoparea:17843",
        destinationStationId: "NL:stoparea:18188",
        extraTransferMinutes,
        selectedFacilities,
      }),
    }),
  );
}

async function selectStations() {
  fireEvent.change(screen.getByRole("combobox", { name: "Vertrekstation" }), {
    target: { value: "Rotterdam" },
  });
  fireEvent.click(await screen.findByRole("option", { name: "Rotterdam Centraal" }));
  fireEvent.change(screen.getByRole("combobox", { name: "Bestemming" }), {
    target: { value: "Utrecht" },
  });
  fireEvent.click(await screen.findByRole("option", { name: "Utrecht Centraal" }));
}

describe("planning with a saved profile", () => {
  beforeEach(() => {
    localStorage.clear();
    vi.spyOn(navigator, "onLine", "get").mockReturnValue(true);
    vi.stubGlobal("fetch", vi.fn<typeof fetch>(async (url) => {
      if (String(url).includes("/api/routes/stations?")) {
        return new Response(JSON.stringify(stations));
      }
      if (String(url).endsWith("/api/routes/plan")) {
        return new Response(JSON.stringify(journey));
      }
      if (String(url).endsWith("/api/needs")) {
        return new Response(JSON.stringify([
          { type: "ELEVATOR_REQUIRED", description: "Lift nodig", category: "FACILITY" },
          { type: "GUIDING_LINE_REQUIRED", description: "Geleidelijn nodig", category: "FACILITY" },
        ]));
      }
      throw new Error(`Unexpected request: ${String(url)}`);
    }));
  });

  afterEach(() => {
    cleanup();
    vi.restoreAllMocks();
    vi.unstubAllGlobals();
  });

  it.each([
    { name: "saved preferences", profile: { selectedFacilities: ["ELEVATOR_REQUIRED"], transferTime: 10 } },
    { name: "no saved profile", profile: null },
  ])("submits $name when clicking Plan mijn reis", async ({ profile }) => {
    if (profile) saveProfile(profile);
    render(<App />);
    await selectStations();

    fireEvent.click(screen.getByRole("button", { name: "Plan mijn reis" }));
    await screen.findByRole("button", { name: /Reis aanpassen/ });

    if (profile) {
      expectPlanningRequest(10, ["ELEVATOR_REQUIRED"]);
    } else {
      expectPlanningRequest(0, []);
    }
  });

  it("only updates planning preferences after explicitly saving the profile", async () => {
    saveProfile({ selectedFacilities: ["ELEVATOR_REQUIRED"], transferTime: 10 });
    render(<App />);
    await selectStations();

    fireEvent.click(screen.getByRole("button", { name: "Mijn profiel" }));
    fireEvent.click(await screen.findByRole("checkbox", { name: "Geleidelijn nodig" }));
    fireEvent.click(screen.getByRole("radio", { name: "15 minuten extra" }));
    fireEvent.click(screen.getByRole("button", { name: "Annuleren" }));
    fireEvent.click(screen.getByRole("button", { name: "Plan mijn reis" }));
    await screen.findByRole("button", { name: /Reis aanpassen/ });
    expectPlanningRequest(10, ["ELEVATOR_REQUIRED"]);

    fireEvent.click(screen.getByRole("button", { name: /Reis aanpassen/ }));
    fireEvent.click(screen.getByRole("button", { name: "Mijn profiel" }));
    fireEvent.click(await screen.findByRole("checkbox", { name: "Geleidelijn nodig" }));
    fireEvent.click(screen.getByRole("radio", { name: "15 minuten extra" }));
    fireEvent.click(screen.getByRole("button", { name: "Profiel opslaan" }));
    fireEvent.click(screen.getByRole("button", { name: "Plan mijn reis" }));
    await screen.findByRole("button", { name: /Reis aanpassen/ });
    expectPlanningRequest(15, ["ELEVATOR_REQUIRED", "GUIDING_LINE_REQUIRED"]);
  });

  it.each([
    { status: 400, message: "Mijn profiel" },
    { status: 502, message: "De route kon niet worden opgehaald. Probeer het opnieuw." },
  ])("explains HTTP $status without changing saved preferences", async ({ status, message }) => {
    const savedProfile = { selectedFacilities: ["RETIRED_NEED"], transferTime: 10 };
    saveProfile(savedProfile);
    render(<App />);
    await selectStations();
    vi.mocked(fetch).mockResolvedValueOnce(new Response(null, { status }));

    fireEvent.click(screen.getByRole("button", { name: "Plan mijn reis" }));

    expect((await screen.findByRole("alert")).textContent).toContain(message);
    expect(loadProfile()).toEqual(savedProfile);
    expect(screen.queryByRole("button", { name: /Reis aanpassen/ })).toBeNull();
  });
});
