// @vitest-environment jsdom
import { describe, it, expect, beforeEach } from "vitest";
import { loadRecentStations, rememberStation } from "./recentStations";



describe("recentStations", () => {
    beforeEach(() => {
        localStorage.clear();
    });


    //Test 1

    it("bewaart een station in de lijst", () => {
        // Arrange
        const station = {
            gtfsId: "NLASD",
            name: "Amsterdam Centraal",
        };

        // Act
        rememberStation(station);
        const result = loadRecentStations();

        // Assert
        expect(result).toHaveLength(1);
        expect(result[0]).toEqual(station);
    });




//Test 2


it("bewaart hetzelfde station niet dubbel", () => {
    // Arrange
    const station = {
        gtfsId: "NLASD",
        name: "Amsterdam Centraal",
    };

    // Act
    rememberStation(station);
    rememberStation(station);
    const result = loadRecentStations();

    // Assert
    expect(result).toHaveLength(1);
    expect(result[0]).toEqual(station);
});


//Test 3


it("bewaart maximaal vijf stations", () => {
    // Arrange
    const stations = [
        { gtfsId: "NLASD", name: "Amsterdam" },
        { gtfsId: "NLUT", name: "Utrecht" },
        { gtfsId: "NLRTD", name: "Rotterdam" },
        { gtfsId: "NLDH", name: "Den Haag" },
        { gtfsId: "NLEHV", name: "Eindhoven" },
        { gtfsId: "NLGRO", name: "Groningen" },
    ];

    // Act
    stations.forEach((station) => rememberStation(station));
    const result = loadRecentStations();

    // Assert
    expect(result).toHaveLength(5);
    expect(result[0]).toEqual(stations[5]);
});
});