export interface TravelProfile {
  selectedFacilities: string[];
  transferTime: number;
}

const STORAGE_KEY = "ov-reisplanner-profile";

const defaultProfile: TravelProfile = {
  selectedFacilities: [],
  transferTime: 0,
};

export function loadProfile(): TravelProfile {
  const storedProfile = localStorage.getItem(STORAGE_KEY); // Hier wordt local storage afgelezen 

  if (!storedProfile) {
    return defaultProfile;
  }

  try {
    const parsedProfile: unknown = JSON.parse(storedProfile); // Leest tekst uit localStorage om naar js gegevens 

    if (
      typeof parsedProfile !== "object" || // checkt of parsedProfile echt een object is 
      parsedProfile === null
    ) {
      return defaultProfile;
    }

    const profile = parsedProfile as Partial<TravelProfile>; //Behandel parsedProfile tijdelijk als een object dat mogelijk onderdelen van TravelProfile bevat.

    if ( // checkt of inhoud van het profiel gelding is 
      !Array.isArray(profile.selectedFacilities) ||
      !profile.selectedFacilities.every(
        (facility) => typeof facility === "string",
      ) ||
      typeof profile.transferTime !== "number"
    ) {
      return defaultProfile;
    }

    return {
      selectedFacilities: profile.selectedFacilities,
      transferTime: profile.transferTime,
    };
  } catch {
    return defaultProfile;
  }
}

export function saveProfile(profile: TravelProfile): void {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(profile));
}