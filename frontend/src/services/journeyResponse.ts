export interface JourneyResponse {
  departureTime: string;
  arrivalTime: string;
  legs: {
    mode: string;
    from: string;
    to: string;
  } [];
}

export async function planJourney(
  originStationId: string,
  destinationStationId: string,
): Promise<JourneyResponse>  {
  const response = await fetch(
    `${import.meta.env.VITE_API_URL}/api/routes/plan`,
    {
      method: "POST",
      headers: { "Content-Type": "application/json"},
      body: JSON.stringify({
        originStationId,
        destinationStationId,
      }),
    },
  );

  if (!response.ok) {
    throw new Error(`Reis opvragen mislukt (${response.status})`);
  }

  return (await response.json()) as JourneyResponse;
}