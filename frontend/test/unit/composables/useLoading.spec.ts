import { describe, expect, it } from "vitest";

import { useLoading } from "@/composables/useLoading";

describe("useLoading", () => {
  it("starts and stops loading state", () => {
    const { loading, start, stop } = useLoading();

    expect(loading.value).toBe(false);
    start();
    expect(loading.value).toBe(true);
    stop();
    expect(loading.value).toBe(false);
  });

  it("wraps async operations with loading state", async () => {
    const { loading, withLoading } = useLoading();
    let observedDuringOperation = false;

    await withLoading(async () => {
      observedDuringOperation = loading.value;
      return "done";
    });

    expect(observedDuringOperation).toBe(true);
    expect(loading.value).toBe(false);
  });
});
