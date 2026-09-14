export interface Stop {
  stopNumber: number;
  stopName: string;
  arrivalTime: string;
  departureTime: string;
  facility: string;
}

export interface Route {
  from: string;
  to: string;
}

export interface DummyRoute {
  route: Route;
  stops: Stop[];
}