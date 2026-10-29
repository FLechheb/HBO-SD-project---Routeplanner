// @vitest-environment jsdom

import { beforeEach, describe, expect, it } from "vitest";
import { loadJourney, saveJourney } from "./journeyStorage";

describe("journeyStorage", () => {
    beforeEach(() => {
        localStorage.clear();
    });

    it("geeft null terug als er geen route is opgeslagen", () => {
        // Arrange
        localStorage.clear();

        // Act
        const result = loadJourney();

        // Assert
        expect(result).toBeNull();
    });


    it("slaat een route op en laadt deze weer", () => {
        // Arrange
        const savedRoute = {
            journey: {
                departureTime: "2026-10-22T10:00:00+02:00",
                arrivalTime: "2026-10-22T11:00:00+02:00",
                legs: [],
            },
            originName: "Rotterdam Centraal",
            destinationName: "Utrecht Centraal",
        };

        // Act
        saveJourney(savedRoute);
        const result = loadJourney();

        // Assert
        expect(result).toEqual(savedRoute);
    });


    it("geeft null terug als de opgeslagen data ongeldig is", () => {
        // Arrange
        localStorage.setItem("ov-reisplanner-saved-journey", "ongeldige json");

        // Act
        const result = loadJourney();

        // Assert
        expect(result).toBeNull();
    });
});