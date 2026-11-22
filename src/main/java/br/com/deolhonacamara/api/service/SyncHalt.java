package br.com.deolhonacamara.api.service;

/**
 * Pedido da Cabine para parar no próximo ponto já gravado.
 * {@code status} é {@code paused} ou {@code cancelled}.
 */
public class SyncHalt extends RuntimeException {

    private final String status;

    public SyncHalt(String status) {
        super(status);
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
