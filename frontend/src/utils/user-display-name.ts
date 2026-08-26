function deriveFromEmail(email: string | null | undefined): string {
  const localPart = email?.trim().split("@")[0]?.trim();
  const firstSegment = localPart?.split(/[._-]+/).find(Boolean);
  if (!firstSegment) {
    return "";
  }

  return (
    firstSegment.charAt(0).toUpperCase() + firstSegment.slice(1).toLowerCase()
  );
}

/**
 * Resolves a short display name for greetings ("Olá, {name}") from session
 * data already available on `AuthenticatedUser` — never fabricates a name.
 * Prefers the first token of `name`; falls back to the email local-part
 * (capitalized) when `name` is blank, since some sessions only carry email.
 *
 * A session can also persist the account's own email address *into* `name`
 * (backend onboarding gap: the SSO identity had no display name, so the
 * email was stored as the collaborator's `nome`) — an email has no
 * whitespace, so `name.split(/\s+/)[0]` would return it verbatim instead of
 * a first name. Treat a `name` that looks like an email the same as a blank
 * one, routing it through the same derivation used for the email fallback.
 */
export function resolveGreetingName(
  user: { name?: string | null; email?: string | null } | null | undefined
): string {
  const name = user?.name?.trim();
  if (name && !name.includes("@")) {
    return name.split(/\s+/)[0]!;
  }

  return deriveFromEmail(user?.email ?? name);
}
