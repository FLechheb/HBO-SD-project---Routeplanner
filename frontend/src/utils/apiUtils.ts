import { useRef, useEffect, useCallback } from "react";

type SafeCallFunction<T> = (
    controller: AbortController
) => Promise<T>;

export function useSafeCall<T = unknown>(
    dependencies: React.DependencyList = []
) {
    const controllerRef = useRef<AbortController | null>(null);

    useEffect(() => {
        return () => {
            // Abort on unmount or dependency change
            controllerRef.current?.abort();
        };
    }, dependencies);

    const safeCall = useCallback(
        async (fn: SafeCallFunction<T>): Promise<T> => {
            controllerRef.current = new AbortController();

            return fn(controllerRef.current);
        },
        dependencies
    );

    function abort(): void {
        controllerRef.current?.abort();
    }

    return {
        safeCall,
        abort,
    };
}