export interface Need {
  type: string;
  description: string;
  category: "FACILITY" | "TIMING";
}

function isNeed(value: unknown): value is Need {
  return typeof value === "object"
    && value !== null
    && "type" in value && typeof value.type === "string"
    && "description" in value && typeof value.description === "string"
    && "category" in value && (value.category === "FACILITY" || value.category === "TIMING");
}

export async function getNeeds(signal: AbortSignal): Promise<Need[]> {
  const response = await fetch(`${import.meta.env.VITE_API_URL}/api/needs`, { signal });

  if (!response.ok) {
    throw new Error(`Voorzieningen ophalen mislukt (${response.status})`);
  }

  const needs: unknown = await response.json();
  if (!Array.isArray(needs) || !needs.every(isNeed)) {
    throw new Error("De voorzieningencatalogus heeft een ongeldig formaat");
  }

  return needs;
}
