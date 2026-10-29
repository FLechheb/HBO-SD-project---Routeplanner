
import { describe, it, expect } from "vitest";
import { calculateTravelTime } from "./time";

describe("calculateTravelTime", () => {
    it("berekent de reistijd correct", () => {
        // Arrange
        const departureTime = "23:00";
        const arrivalTime = "00:30";

        // Act
        const result = calculateTravelTime(
            departureTime,
            arrivalTime
        );

        // Assert
        expect(result).toBe("1 uur en 30 minuten");
    });

        it("berekent de reistijd correct 2", () => {
            // Arrange
            const departureTime = "09:00";
            const arrivalTime = "10:30";

            // Act
            const result = calculateTravelTime(
                departureTime,
                arrivalTime
            );

            // Assert
            expect(result).toBe("1 uur en 30 minuten");
        });


    });

