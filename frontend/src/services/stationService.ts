export interface Station {
  gtfsId: string;
  name: string;
}

export async function searchStations(name: string): Promise<Station[]>
{
  const response = await fetch(
    `${import.meta.env.VITE_API_URL}/api/routes/stations?name=${encodeURIComponent(name)}`,);

  if (!response.ok) {
    throw new Error(`Stations zoeken mislukt (${response.status})`);
  }

  return (await response.json()) as Station[];
}