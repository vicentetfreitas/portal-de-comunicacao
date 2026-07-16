import { beforeEach, describe, expect, it } from "vitest";

import { initTheme, useTheme } from "@/composables/useTheme";

describe("useTheme", () => {
  beforeEach(() => {
    document.documentElement.removeAttribute("data-theme");
    initTheme();
    useTheme().setMode("light");
  });

  it("applies dark theme mode", () => {
    const { setMode, isDark } = useTheme();

    setMode("dark");

    expect(isDark.value).toBe(true);
    expect(document.documentElement.getAttribute("data-theme")).toBe("dark");
  });

  it("toggles between light and dark", () => {
    const { toggle, isDark } = useTheme();

    expect(isDark.value).toBe(false);
    toggle();
    expect(isDark.value).toBe(true);
    toggle();
    expect(isDark.value).toBe(false);
  });
});
