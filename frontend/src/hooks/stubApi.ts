import axios, {AxiosError, AxiosResponse} from "axios";
import {useState} from "react";
import {useSafeCall} from "../utils/apiUtils.ts";

interface Stub {
    // Add your actual Stub properties here
    id: number;
    // ...
}

interface CreateStubResult {
    success: boolean;
    response: AxiosResponse<Stub> | AxiosError;
}

export function useFetchStub(dependencies: unknown[] = []) {
    const [result, setResult] = useState<Stub[]>([]);
    const [failed, toggleFailed] = useState<boolean>(false);
    const [error, setError] = useState<string>("");
    const [loaded, toggleLoaded] = useState<boolean>(false);

    const {safeCall, abort} = useSafeCall();

    async function loadCall(controller: AbortController): Promise<void> {
        return safeCall(async (controller: AbortController) => {
            try {
                toggleLoaded(false);
                toggleFailed(false);
                setError("");
                setResult([]);

                const response = await axios.get<Stub[]>(
                    `${import.meta.env.VITE_API_URL}/stub`,
                    {
                        signal: controller.signal,
                        params: {},
                    }
                );

                setResult(
                    response.data.map((stubData: Stub) => {
                        return stubData;
                    })
                );
            } catch (err: unknown) {
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
        })
    }

    return {
        result,
        loaded,
        failed,
        error,
    };
}

export function useCreateStub() {
    const [success, setSuccess] = useState<boolean>(false);
    const [result, setResult] = useState<AxiosResponse<Stub> | null>(null);

    const {safeCall, abort} = useSafeCall();

    async function createStub(data: Stub) {
        return safeCall(async (controller: AbortController) => {
            try {
                const response = await axios.post<Stub>(
                    `${import.meta.env.VITE_API_URL}/stub`,
                    data,
                    {
                        signal: controller.signal,
                    }
                );

                setSuccess(true);
                setResult(response);

                return [true, response] as const;
            } catch (err: unknown) {
                if (axios.isCancel(err)) {
                    console.log("Request was aborted");
                    return [false, err] as const;
                }

                if (axios.isAxiosError(err)) {
                    return [false, err.response ?? err] as const;
                }

                return [false, err] as const;
            }
        });
    }

    return {
        createStub,
        abort,
        success,
        result,
    };
}