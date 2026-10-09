import type { ApiStop } from "../types/route";


export async function fetchStops(): Promise<ApiStop[]> {
    const response = await fetch(`${import.meta.env.VITE_API_URL}/api/stops`, {
        method: "GET"
    });

    if (!response.ok) {
        throw new Error(`Failed to fetch stops: ${response.status}`);
    }
    return response.json()
}
