package br.com.unimedceara.portalcomunicacao.shared.validation.group;

/**
 * Define grupos de validação reutilizáveis para operações da API.
 * Utilize as interfaces marker nos atributos {@code groups} das constraints Bean Validation.
 * Para o grupo padrão do Bean Validation, utilize {@link jakarta.validation.groups.Default}.
 */
public final class ValidationGroups {

    private ValidationGroups() {
    }

    /**
     * Grupo aplicável a operações de criação de recursos.
     */
    public interface Create {
    }

    /**
     * Grupo aplicável a operações de atualização de recursos.
     */
    public interface Update {
    }

    /**
     * Grupo aplicável a operações de exclusão de recursos.
     */
    public interface Delete {
    }
}
