import { useRef, useEffect, useCallback } from "react";

type SafeCallFunction<T> = (
    controller: AbortController
) => Promise<T>;

export function useSafeCall() {
    const controllerRef = useRef<AbortController | null>(null);

    const abort = useCallback((): void => {
        const controller = controllerRef.current;
        controllerRef.current = null;
        controller?.abort();
    }, []);

    useEffect(() => {
        return abort;
    }, [abort]);

    const safeCall = useCallback(
        async <T>(fn: SafeCallFunction<T>): Promise<T> => {
            abort();

            const controller = new AbortController();
            controllerRef.current = controller;

            try {
                return await fn(controller);
            } finally {
                if (controllerRef.current === controller) {
                    controllerRef.current = null;
                }
            }
        },
        [abort]
    );

    return {
        safeCall,
        abort,
    };
}