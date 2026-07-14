package br.com.unimedceara.portalcomunicacao.accesscontrol.domain.model;

/**
 * Status lógico do colaborador.
 */
public enum ColaboradorStatus {
    ACTIVE,
    INACTIVE;

    public static ColaboradorStatus fromFlag(String flag) {
        return "S".equalsIgnoreCase(flag) ? ACTIVE : INACTIVE;
    }

    public String toFlag() {
        return this == ACTIVE ? "S" : "N";
    }
}
