export interface JourneyResponse {
  departureTime: string;
  arrivalTime: string;
  legs: {
    mode: string;
    headsign: string | null;
    departureTime: string | null;
    arrivalTime: string | null;
    routeName: string | null;
    tripNumber: string | null;
    from: string;
    departurePlatform: string | null;
    to: string;
    arrivalPlatform: string | null;
  }[];
}

export async function planJourney(
  originStationId: string,
  destinationStationId: string,
  extraTransferMinutes: number,
): Promise<JourneyResponse> {
  const response = await fetch(
    `${import.meta.env.VITE_API_URL}/api/routes/plan`,
    {
      method: "POST",
      headers: { "Content-Type": "application/json"},
      body: JSON.stringify({
        originStationId,
        destinationStationId,
        extraTransferMinutes,
      }),
    },
  );

  if (!response.ok) {
    throw new Error(`Reis opvragen mislukt (${response.status})`);
  }

  return (await response.json()) as JourneyResponse;
}
