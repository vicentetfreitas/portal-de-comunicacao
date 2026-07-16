/**
 * RN-AUTH-007 — tokens must never be stored in localStorage or sessionStorage.
 */

const TOKEN_KEY_PATTERNS = [
  "token",
  "access_token",
  "refresh_token",
  "jwt",
  "auth_token",
  "bearer"
];

function isForbiddenStorageKey(key: string): boolean {
  const normalized = key.toLowerCase();
  return TOKEN_KEY_PATTERNS.some(pattern => normalized.includes(pattern));
}

function scanStorage(storage: Storage): string[] {
  const violations: string[] = [];

  for (let index = 0; index < storage.length; index += 1) {
    const key = storage.key(index);
    if (key && isForbiddenStorageKey(key)) {
      violations.push(key);
    }
  }

  return violations;
}

export function detectTokenStorageViolations(): string[] {
  if (typeof window === "undefined") {
    return [];
  }

  return [
    ...scanStorage(window.localStorage),
    ...scanStorage(window.sessionStorage)
  ];
}

export function assertNoTokenStorage(): void {
  const violations = detectTokenStorageViolations();
  if (violations.length > 0) {
    console.warn(
      `[auth] RN-AUTH-007: forbidden token keys in browser storage: ${violations.join(", ")}`
    );
  }
}

export function installTokenStorageGuard(): void {
  if (typeof window === "undefined" || !import.meta.env.DEV) {
    return;
  }

  const wrapSetItem = (storage: Storage, label: string): void => {
    const original = storage.setItem.bind(storage);
    storage.setItem = (key: string, value: string) => {
      if (isForbiddenStorageKey(key)) {
        throw new Error(
          `[auth] RN-AUTH-007: storing auth tokens in ${label} is forbidden (key="${key}")`
        );
      }
      original(key, value);
    };
  };

  wrapSetItem(window.localStorage, "localStorage");
  wrapSetItem(window.sessionStorage, "sessionStorage");
}
