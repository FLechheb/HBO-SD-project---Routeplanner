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
  const storedProfile = localStorage.getItem(STORAGE_KEY);

  if (!storedProfile) {
    return defaultProfile;
  }

  try {
    const parsedProfile: unknown = JSON.parse(storedProfile);

    if (
      typeof parsedProfile !== "object" ||
      parsedProfile === null
    ) {
      return defaultProfile;
    }

    const profile = parsedProfile as Partial<TravelProfile>;

    if (
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