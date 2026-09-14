export function calculateTravelTime(
  departureTime: string,
  arrivalTime: string
) {
  const [departureHours, departureMinutes] =
    departureTime.split(":").map(Number);

  const [arrivalHours, arrivalMinutes] =
    arrivalTime.split(":").map(Number);

  const departure =
    departureHours * 60 + departureMinutes;

  const arrival =
    arrivalHours * 60 + arrivalMinutes;

const difference = arrival - departure;


  const hours = Math.floor(difference / 60);
  const minutes = difference % 60;

  if (hours === 0) {
    return `${minutes} minuten`;
  }

  if (minutes === 0) {
    return `${hours} uur`;
  }

  return `${hours} uur en ${minutes} minuten`;
}