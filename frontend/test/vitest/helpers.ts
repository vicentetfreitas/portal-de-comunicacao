import { vi } from "vitest";
import type { NavigationGuardNext } from "vue-router";

export function createNextMock(): NavigationGuardNext {
  return vi.fn() as unknown as NavigationGuardNext;
}
