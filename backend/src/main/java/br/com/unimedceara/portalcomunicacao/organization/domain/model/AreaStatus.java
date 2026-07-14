package br.com.unimedceara.portalcomunicacao.organization.domain.model;

/**
 * Status lógico da área organizacional.
 */
public enum AreaStatus {
    ACTIVE,
    INACTIVE;

    public static AreaStatus fromFlag(String flag) {
        return "S".equalsIgnoreCase(flag) ? ACTIVE : INACTIVE;
    }

    public String toFlag() {
        return this == ACTIVE ? "S" : "N";
    }
}
