import axios from "axios";
import {useCallback, useEffect, useState} from "react";
import {useSafeCall} from "../utils/apiUtils.ts";

interface Stub {
    name: string;
}

export function useFetchStub() {
    const [result, setResult] = useState<Stub | null>(null);
    const [failed, toggleFailed] = useState<boolean>(false);
    const [error, setError] = useState<string>("");
    const [loaded, toggleLoaded] = useState<boolean>(false);

    const {safeCall} = useSafeCall();

    const loadCall = useCallback(async (): Promise<void> => {
        await safeCall(async (controller: AbortController) => {
            try {
                toggleLoaded(false);
                toggleFailed(false);
                setError("");
                setResult(null);

                const response = await axios.get<Stub>(
                    `${import.meta.env.VITE_API_URL}/stub`,
                    {
                        signal: controller.signal,
                        params: {},
                    }
                );

                setResult(response.data);
            } catch (err: unknown) {
                if (axios.isCancel(err)) {
                    return;
                }

                console.warn(err);

                toggleFailed(true);

                if (err instanceof Error) {
                    setError(err.message);
                } else {
                    setError("An unknown error occurred");
                }
            } finally {
                toggleLoaded(true);
            }
        });
    }, [safeCall]);

    useEffect(() => {
        void loadCall();
    }, [loadCall]);

    return {
        result,
        loaded,
        failed,
        error,
        reload: loadCall,
    };
}
