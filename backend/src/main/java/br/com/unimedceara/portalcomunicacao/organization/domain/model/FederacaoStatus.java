package br.com.unimedceara.portalcomunicacao.organization.domain.model;

/**
 * Status lógico da federação organizacional.
 */
public enum FederacaoStatus {
    ACTIVE,
    INACTIVE;

    public static FederacaoStatus fromFlag(String flag) {
        return "S".equalsIgnoreCase(flag) ? ACTIVE : INACTIVE;
    }

    public String toFlag() {
        return this == ACTIVE ? "S" : "N";
    }
}
