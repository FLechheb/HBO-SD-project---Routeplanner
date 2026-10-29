import { afterEach, describe, expect, it, vi } from "vitest";
import { planJourney } from "./journeyResponse";

describe("planJourney", () => {
  afterEach(() => {
    vi.unstubAllGlobals();
  });

  it("sends extra transfer minutes when planning a journey", async () => {
    // Arrange
    const fetchMock = vi.fn().mockResolvedValue({
      ok: true,
      json: async () => ({
        departureTime: "2026-10-24T11:54:00+02:00",
        arrivalTime: "2026-10-24T12:20:00+02:00",
        legs: [],
      }),
    });

    vi.stubGlobal("fetch", fetchMock);

    // Act
    await planJourney(
      "NL:stoparea:17843",
      "NL:stoparea:18188",
      5,
      [],
    );

    // Assert
    expect(fetchMock).toHaveBeenCalledWith(
      expect.any(String),
      expect.objectContaining({
        body: JSON.stringify({
          originStationId: "NL:stoparea:17843",
          destinationStationId: "NL:stoparea:18188",
          extraTransferMinutes: 5,
          selectedFacilities: [],
        }),
      }),
    );
  });
});
