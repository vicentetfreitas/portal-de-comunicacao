package br.com.unimedceara.portalcomunicacao.organization.domain.model;

/**
 * Status lógico da equipe organizacional.
 */
public enum EquipeStatus {
    ACTIVE,
    INACTIVE;

    public static EquipeStatus fromFlag(String flag) {
        return "S".equalsIgnoreCase(flag) ? ACTIVE : INACTIVE;
    }

    public String toFlag() {
        return this == ACTIVE ? "S" : "N";
    }
}
