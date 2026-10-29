export interface Facility {
  type: string;
  operational: boolean;
}

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
    departureFacilities: Facility[] | null;
    arrivalFacilities: Facility[] | null;
  }[];
}

export class JourneyRequestError extends Error {
  readonly status: number;

  constructor(status: number) {
    super(`Reis opvragen mislukt (${status})`);
    this.status = status;
  }
}

export async function planJourney(
  originStationId: string,
  destinationStationId: string,
  extraTransferMinutes: number,
  selectedFacilities: string[],
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
        selectedFacilities,
      }),
    },
  );

  if (!response.ok) {
    throw new JourneyRequestError(response.status);
  }

  return (await response.json()) as JourneyResponse;
}
