package br.com.deolhonacamara.api.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class SyncRunControl {

    public static final String PAUSE = "pause";
    public static final String CANCEL = "cancel";

    private final ConcurrentHashMap<String, String> signals = new ConcurrentHashMap<>();

    public void request(String flow, String signal) {
        signals.put(flow, signal);
        if (SyncFlows.ALL.equals(flow)) {
            SyncFlows.DEFAULT_CRONS.keySet().forEach(child -> signals.put(child, signal));
        }
    }

    public void clear(String flow) {
        signals.remove(flow);
        if (SyncFlows.ALL.equals(flow)) {
            SyncFlows.DEFAULT_CRONS.keySet().forEach(signals::remove);
        }
    }

    public boolean isRequested(String flow) {
        return signals.containsKey(flow);
    }

    public void ensureRunning(String flow) {
        String signal = signals.remove(flow);
        if (signal == null) {
            return;
        }
        throw new SyncHalt(PAUSE.equals(signal) ? "paused" : "cancelled");
    }
}
