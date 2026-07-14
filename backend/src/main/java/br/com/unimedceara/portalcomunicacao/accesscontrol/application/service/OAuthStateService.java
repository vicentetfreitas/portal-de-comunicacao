package br.com.unimedceara.portalcomunicacao.accesscontrol.application.service;

import br.com.unimedceara.portalcomunicacao.shared.exception.ValidationException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Armazenamento em memória de states OAuth anti-CSRF para o fluxo de login Zimbra.
 */
@Service
public class OAuthStateService {

    private static final String INVALID_STATE_MESSAGE = "State de autenticação inválido ou expirado";

    private final Map<String, StateEntry> states = new ConcurrentHashMap<>();

    /**
     * Gera e armazena um novo state com TTL de 5 minutos.
     */
    public String createState(boolean rememberMe) {
        purgeExpired();
        String state = UUID.randomUUID().toString();
        states.put(state, new StateEntry(rememberMe, Instant.now().plusSeconds(300)));
        return state;
    }

    /**
     * Valida e consome o state (uso único).
     */
    public boolean consumeState(String state) {
        purgeExpired();
        if (state == null || state.isBlank()) {
            throw new ValidationException(INVALID_STATE_MESSAGE, java.util.List.of());
        }
        StateEntry entry = states.remove(state);
        if (entry == null || entry.expiresAt().isBefore(Instant.now())) {
            throw new ValidationException(INVALID_STATE_MESSAGE, java.util.List.of());
        }
        return entry.rememberMe();
    }

    private void purgeExpired() {
        Instant now = Instant.now();
        states.entrySet().removeIf(entry -> entry.getValue().expiresAt().isBefore(now));
    }

    private record StateEntry(boolean rememberMe, Instant expiresAt) {
    }
}
