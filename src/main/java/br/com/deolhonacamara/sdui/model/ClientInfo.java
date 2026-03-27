package br.com.deolhonacamara.sdui.model;

/**
 * Holds client device/application metadata sent via HTTP headers on every SDUI request.
 *
 * @param appVersion   value of {@code X-App-Version} header (e.g. "1.2.3")
 * @param appPlatform  value of {@code X-App-Platform} header (e.g. "android", "ios")
 * @param osVersion    value of {@code X-OS-Version} header (e.g. "14.0")
 * @param deviceModel  value of {@code X-Device-Model} header (e.g. "Samsung Galaxy S21")
 * @param deviceId     value of {@code X-Device-Id} header — stable unique device identifier
 * @param appLanguage  value of {@code X-App-Language} header (BCP-47, e.g. "pt-BR")
 */
public record ClientInfo(
        String appVersion,
        String appPlatform,
        String osVersion,
        String deviceModel,
        String deviceId,
        String appLanguage
) {
    public static ClientInfo of(
            String appVersion,
            String appPlatform,
            String osVersion,
            String deviceModel,
            String deviceId,
            String appLanguage) {
        return new ClientInfo(appVersion, appPlatform, osVersion, deviceModel, deviceId, appLanguage);
    }
}
