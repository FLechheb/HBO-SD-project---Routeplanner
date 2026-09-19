import { useState } from "react";
import type { Coordinate } from "../types/route";
import { planRoute } from "./routeservice";

function TestRouteForm(){
    const [origin, setOrigin] = useState<Coordinate>({lat: "", lon: "" });
    const [destination, setDestination] = useState<Coordinate>({lat: "", lon: "" });
    const [result, setResult] = useState<unknown>(null);

    async function handleSubmit(event: React.FormEvent<HTMLFormElement>) {
        event.preventDefault();

        try {
            const response = await planRoute(origin, destination);
            setResult(response);
        } catch (error) {
            setResult({ error: String(error) });
        }
    }
    return <>
    <form onSubmit={handleSubmit}>
      <div>
        <label htmlFor="vertek lat">vertrek lat</label>
            <input type = "text"
                value={origin.lat}
                onChange={(event) =>
                  setOrigin((prev) => ({ ...prev, lat: event.target.value }))
                }
            />
      </div>
      <div>
        <label htmlFor="vertek lon">vertrek lon</label>
            <input type = "text"
                value={origin.lon}
                onChange={(event) =>
                  setOrigin((prev) => ({ ...prev, lon: event.target.value }))
                }
            />
      </div>

      <div>
        <label htmlFor="aankomst lat">aankomst lat</label>
            <input type = "text"
                value={destination.lat}
                onChange={(event) =>
                  setDestination((prev) => ({ ...prev, lat: event.target.value }))
                }
            />
      </div>
      <div>
        <label htmlFor="aankomst lon">aankomst lon</label>
            <input type = "text"
                value={destination.lon}
                onChange={(event) =>
                  setDestination((prev) => ({ ...prev, lon: event.target.value }))
                }
            />
      </div>
      <button type="submit">Plan mijn reis</button>
    </form>

    <pre>{JSON.stringify(result, null, 2)}</pre>
    </>
}

export default TestRouteForm;
