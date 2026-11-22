package br.com.deolhonacamara.api.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Log4j2
public class SyncExecutionGate {

    private final Set<String> running = ConcurrentHashMap.newKeySet();
    private final ThreadLocal<Boolean> fullSync = ThreadLocal.withInitial(() -> false);

    /**
     * Motivo para não começar, sem reservar o fluxo.
     */
    public Optional<String> blockReason(String flow) {
        synchronized (running) {
            return reasonFor(flow, false);
        }
    }

    public boolean isRunning(String flow) {
        return running.contains(flow);
    }

    /**
     * Reserva o fluxo. Vazio significa que esta chamada ficou com a reserva.
     */
    public Optional<String> acquire(String flow) {
        synchronized (running) {
            Optional<String> reason = reasonFor(flow, Boolean.TRUE.equals(fullSync.get()));
            if (reason.isEmpty()) {
                running.add(flow);
            }
            return reason;
        }
    }

    /**
     * Segura a carga completa e deixa as etapas dela passarem pela trava.
     */
    public boolean runFull(Runnable work) {
        if (!enter(SyncFlows.ALL)) {
            return false;
        }
        fullSync.set(true);
        try {
            work.run();
            return true;
        } finally {
            fullSync.remove();
            release(SyncFlows.ALL);
        }
    }

    private Optional<String> reasonFor(String flow, boolean insideFullSync) {
        if (running.contains(flow)) {
            return Optional.of(flow + " is already running");
        }
        if (!insideFullSync && running.contains(SyncFlows.ALL) && !SyncFlows.ALL.equals(flow)) {
            return Optional.of("waiting for " + SyncFlows.ALL);
        }
        if (SyncFlows.ALL.equals(flow)) {
            for (String other : running) {
                if (!SyncFlows.ALL.equals(other)) {
                    return Optional.of("waiting for " + other);
                }
            }
        }
        for (String blocker : SyncFlows.BLOCKED_BY.getOrDefault(flow, Set.of())) {
            if (running.contains(blocker)) {
                return Optional.of("waiting for " + blocker);
            }
        }
        return Optional.empty();
    }

    public boolean enter(String flow) {
        Optional<String> reason = acquire(flow);
        if (reason.isPresent()) {
            log.warn("Sync {} skipped: {}", flow, reason.get());
            return false;
        }
        return true;
    }

    public void release(String flow) {
        running.remove(flow);
    }
}
