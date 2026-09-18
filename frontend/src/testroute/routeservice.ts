import type { Coordinate } from "../types/route";

const BASE_URL = "http://localhost:8081/api/routes";

export async function planRoute(origin: Coordinate, destination: Coordinate) {
    const response = await fetch(`${BASE_URL}/plan`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            originLat: Number(origin.lat),
            originLon: Number(origin.lon),
            destinationLat: Number(destination.lat),
            destinationLon: Number(destination.lon),
        }),
    });

    if(response.status == 200){
        console.log("message binnen")
    }
    else{
        console.log (response.status)
    }  
    return response.json()
}
  // hier moet jij nog: response.ok checken, en bij falen een error gooien
  // vervolgens: return response.json();
