import { describe, expect, it } from "vitest";

import { resolveGreetingName } from "@/utils/user-display-name";

describe("resolveGreetingName", () => {
  it("uses the first token of name when present", () => {
    expect(
      resolveGreetingName({ name: "Monalisa Sousa", email: "m@x.com" })
    ).toBe("Monalisa");
  });

  it("trims and uses a single-word name as-is", () => {
    expect(resolveGreetingName({ name: "  Monalisa  ", email: null })).toBe(
      "Monalisa"
    );
  });

  it("falls back to the capitalized email local-part when name is blank", () => {
    expect(
      resolveGreetingName({
        name: "",
        email: "vicente.freitas@unimedceara.com.br"
      })
    ).toBe("Vicente");
  });

  it("falls back to email when name is whitespace-only", () => {
    expect(
      resolveGreetingName({ name: "   ", email: "ana@unimedceara.com.br" })
    ).toBe("Ana");
  });

  it("handles email local-parts without separators", () => {
    expect(
      resolveGreetingName({ name: null, email: "monalisa@unimedceara.com.br" })
    ).toBe("Monalisa");
  });

  it("returns an empty string when neither name nor email is available", () => {
    expect(resolveGreetingName({ name: null, email: null })).toBe("");
    expect(resolveGreetingName(null)).toBe("");
    expect(resolveGreetingName(undefined)).toBe("");
  });

  it("treats a name that is itself an email as blank (onboarding gap: backend persisted email into nome)", () => {
    expect(
      resolveGreetingName({
        name: "vicentefreitas@unimedceara.com.br",
        email: "vicentefreitas@unimedceara.com.br"
      })
    ).toBe("Vicentefreitas");
  });

  it("falls back to name itself when it looks like an email and email field is unavailable", () => {
    expect(
      resolveGreetingName({ name: "ana.souza@unimedceara.com.br", email: null })
    ).toBe("Ana");
  });
});
