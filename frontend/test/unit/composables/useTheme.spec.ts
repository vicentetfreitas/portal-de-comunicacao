import { beforeEach, describe, expect, it } from "vitest";

import { THEME_STORAGE_KEY } from "@/constants/theme";
import { initTheme, useTheme } from "@/composables/useTheme";

describe("useTheme", () => {
  beforeEach(() => {
    document.documentElement.removeAttribute("data-theme");
    window.localStorage.clear();
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

  it("persists an explicit selection to storage (DEC-FA-005)", () => {
    const { setMode } = useTheme();

    setMode("dark");

    expect(window.localStorage.getItem(THEME_STORAGE_KEY)).toBe("dark");
  });

  it("restores the persisted preference on the next init, without depending on system preference", () => {
    useTheme().setMode("dark");

    document.documentElement.removeAttribute("data-theme");
    initTheme();

    expect(document.documentElement.getAttribute("data-theme")).toBe("dark");
    expect(useTheme().mode.value).toBe("dark");
  });

  it("falls back to following the system preference when nothing was persisted yet", () => {
    window.localStorage.clear();

    initTheme();

    expect(useTheme().mode.value).toBe("auto");
  });
});
