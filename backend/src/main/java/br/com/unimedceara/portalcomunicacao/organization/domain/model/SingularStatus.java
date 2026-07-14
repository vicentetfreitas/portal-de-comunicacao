package br.com.unimedceara.portalcomunicacao.organization.domain.model;

/**
 * Status lógico da singular organizacional.
 */
public enum SingularStatus {
    ACTIVE,
    INACTIVE;

    public static SingularStatus fromFlag(String flag) {
        return "S".equalsIgnoreCase(flag) ? ACTIVE : INACTIVE;
    }

    public String toFlag() {
        return this == ACTIVE ? "S" : "N";
    }
}
