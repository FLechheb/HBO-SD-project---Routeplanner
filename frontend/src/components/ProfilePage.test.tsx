// @vitest-environment jsdom

import { act, cleanup, fireEvent, render, screen, within } from "@testing-library/react";
import { afterEach, beforeEach, describe, expect, it, vi } from "vitest";
import App from "../App";
import { loadProfile, saveProfile } from "../utils/profileStorage";

const catalogue = [
  { type: "SEATING_REQUIRED", description: "Zitplaats nodig", category: "FACILITY" },
  { type: "EXTRA_TRANSFER_TIME", description: "Extra overstaptijd", category: "TIMING" },
  { type: "ELEVATOR_REQUIRED", description: "Lift nodig", category: "FACILITY" },
  { type: "GUIDING_LINE_REQUIRED", description: "Geleidelijn nodig", category: "FACILITY" },
];

function openProfile() {
  render(<App />);
  fireEvent.click(screen.getByRole("button", { name: "Mijn profiel" }));
}

describe("catalogue-driven profile", () => {
  beforeEach(() => {
    localStorage.clear();
    vi.stubGlobal("fetch", vi.fn().mockImplementation(
      async () => new Response(JSON.stringify(catalogue)),
    ));
  });

  afterEach(() => {
    cleanup();
    vi.restoreAllMocks();
    vi.unstubAllGlobals();
  });

  it("renders facility needs in catalogue order and saves their identifiers", async () => {
    saveProfile({ selectedFacilities: ["ELEVATOR_REQUIRED"], transferTime: 10 });
    openProfile();

    await screen.findByRole("checkbox", { name: "Zitplaats nodig" });
    const facilities = screen.getByRole("group", { name: "Benodigde voorzieningen" });
    expect(within(facilities).getAllByRole("checkbox").map(
      (checkbox) => checkbox.closest("label")?.textContent,
    )).toEqual(["Zitplaats nodig", "Lift nodig", "Geleidelijn nodig"]);
    expect(screen.getByRole("checkbox", { name: "Lift nodig" })).toHaveProperty("checked", true);
    expect(screen.getByRole("checkbox", { name: "Zitplaats nodig" })).toHaveProperty("checked", false);

    fireEvent.click(screen.getByRole("checkbox", { name: "Zitplaats nodig" }));
    fireEvent.click(screen.getByRole("button", { name: "Profiel opslaan" }));

    expect(loadProfile()).toEqual({
      selectedFacilities: ["ELEVATOR_REQUIRED", "SEATING_REQUIRED"],
      transferTime: 10,
    });
  });

  it("blocks saving during loading and failure, then retries without losing the draft", async () => {
    const savedProfile = { selectedFacilities: ["ELEVATOR_REQUIRED"], transferTime: 10 };
    saveProfile(savedProfile);
    vi.mocked(fetch).mockResolvedValueOnce(new Response(null, { status: 503 }));
    openProfile();

    expect(screen.getByRole("status").textContent).toContain("geladen");
    expect(screen.getByRole("button", { name: "Profiel opslaan" })).toHaveProperty("disabled", true);
    fireEvent.click(screen.getByRole("radio", { name: "15 minuten extra" }));

    await screen.findByRole("alert");
    expect(loadProfile()).toEqual(savedProfile);
    expect(screen.getByRole("button", { name: "Profiel opslaan" })).toHaveProperty("disabled", true);

    fireEvent.click(screen.getByRole("button", { name: "Opnieuw proberen" }));
    await screen.findByRole("checkbox", { name: "Lift nodig" });
    expect(screen.getByRole("checkbox", { name: "Lift nodig" })).toHaveProperty("checked", true);
    expect(screen.getByRole("radio", { name: "15 minuten extra" })).toHaveProperty("checked", true);
    fireEvent.click(screen.getByRole("button", { name: "Profiel opslaan" }));
    expect(loadProfile()).toEqual({ ...savedProfile, transferTime: 15 });
  });

  it.each(["lift", "roltrap", "hellingbaan", "geleidelijn"])(
    "resets legacy preference %s immediately without resetting transfer time",
    async (legacyId) => {
      saveProfile({
        selectedFacilities: [legacyId, "ELEVATOR_REQUIRED"],
        transferTime: 10,
      });
      openProfile();

      await screen.findByRole("checkbox", { name: "Lift nodig" });
      expect(screen.getByRole("status").textContent).toContain("oude");
      expect(loadProfile()).toEqual({ selectedFacilities: [], transferTime: 10 });
      expect(screen.getByRole("checkbox", { name: "Lift nodig" })).toHaveProperty("checked", false);

      fireEvent.click(screen.getByRole("button", { name: "Annuleren" }));
      fireEvent.click(screen.getByRole("button", { name: "Mijn profiel" }));
      await screen.findByRole("checkbox", { name: "Lift nodig" });
      expect(screen.getByRole("checkbox", { name: "Lift nodig" })).toHaveProperty("checked", false);
    },
  );

  it("explains removed needs but only persists their removal when saving", async () => {
    const savedProfile = {
      selectedFacilities: ["ELEVATOR_REQUIRED", "RETIRED_NEED", "EXTRA_TRANSFER_TIME"],
      transferTime: 5,
    };
    saveProfile(savedProfile);
    openProfile();

    await screen.findByRole("checkbox", { name: "Lift nodig" });
    expect(screen.getByRole("status").textContent).toContain("niet meer");
    expect(loadProfile()).toEqual(savedProfile);

    fireEvent.click(screen.getByRole("checkbox", { name: "Zitplaats nodig" }));
    fireEvent.click(screen.getByRole("button", { name: "Annuleren" }));
    expect(loadProfile()).toEqual(savedProfile);
    fireEvent.click(screen.getByRole("button", { name: "Mijn profiel" }));
    await screen.findByRole("checkbox", { name: "Lift nodig" });
    expect(screen.getByRole("checkbox", { name: "Zitplaats nodig" })).toHaveProperty("checked", false);

    fireEvent.click(screen.getByRole("checkbox", { name: "Lift nodig" }));
    fireEvent.click(screen.getByRole("button", { name: "Profiel opslaan" }));
    expect(loadProfile()).toEqual({ selectedFacilities: [], transferTime: 5 });
  });

  it.each([
    { name: "HTTP failure", response: () => new Response(null, { status: 503 }) },
    { name: "missing category", response: () => new Response(JSON.stringify([
      { type: "ELEVATOR_REQUIRED", description: "Lift nodig" },
    ])) },
    { name: "malformed catalogue", response: () => new Response(JSON.stringify({ needs: [] })) },
  ])("keeps legacy preferences untouched on $name", async ({ response }) => {
    const savedProfile = { selectedFacilities: ["lift"], transferTime: 15 };
    saveProfile(savedProfile);
    vi.mocked(fetch).mockResolvedValueOnce(response());
    openProfile();

    await screen.findByRole("alert");
    expect(screen.getByRole("button", { name: "Profiel opslaan" })).toHaveProperty("disabled", true);
    expect(loadProfile()).toEqual(savedProfile);
  });

  it("explains an empty facility catalogue and allows saving no requirements", async () => {
    vi.mocked(fetch).mockResolvedValueOnce(new Response(JSON.stringify([
      { type: "EXTRA_TRANSFER_TIME", description: "Extra overstaptijd", category: "TIMING" },
    ])));
    openProfile();

    await screen.findByText("Er zijn geen voorzieningen beschikbaar.");
    expect(screen.queryAllByRole("checkbox")).toHaveLength(0);
    fireEvent.click(screen.getByRole("button", { name: "Profiel opslaan" }));
    expect(loadProfile()).toEqual({ selectedFacilities: [], transferTime: 0 });
  });

  it("ignores an outstanding catalogue response after leaving the profile", async () => {
    const savedProfile = { selectedFacilities: ["lift"], transferTime: 5 };
    saveProfile(savedProfile);
    let resolveResponse!: (response: Response) => void;
    vi.mocked(fetch).mockReturnValueOnce(new Promise<Response>((resolve) => {
      resolveResponse = resolve;
    }));
    openProfile();
    fireEvent.click(screen.getByRole("button", { name: "Annuleren" }));

    await act(async () => {
      resolveResponse(new Response(JSON.stringify(catalogue)));
    });

    expect(loadProfile()).toEqual(savedProfile);
    expect(screen.queryByRole("checkbox")).toBeNull();
    fireEvent.click(screen.getByRole("button", { name: "Mijn profiel" }));
    await screen.findByRole("checkbox", { name: "Lift nodig" });
    expect(loadProfile()).toEqual({ selectedFacilities: [], transferTime: 5 });
  });

  it("reports a failed legacy reset and retries without losing stored preferences", async () => {
    const savedProfile = { selectedFacilities: ["lift"], transferTime: 10 };
    saveProfile(savedProfile);
    vi.spyOn(Storage.prototype, "setItem").mockImplementationOnce(() => {
      throw new DOMException("Storage unavailable", "QuotaExceededError");
    });
    openProfile();

    expect((await screen.findByRole("alert")).textContent).toContain("niet worden gewist");
    expect(loadProfile()).toEqual(savedProfile);
    expect(screen.getByRole("button", { name: "Profiel opslaan" })).toHaveProperty("disabled", true);
    fireEvent.click(screen.getByRole("button", { name: "Opnieuw proberen" }));
    await screen.findByRole("checkbox", { name: "Lift nodig" });
    expect(loadProfile()).toEqual({ selectedFacilities: [], transferTime: 10 });
  });
});
