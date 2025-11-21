package br.com.deolhonacamara.api.controller;

import br.com.deolhonacamara.api.service.JwtService;
import br.com.deolhonacamara.api.service.UserDeviceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coelho.deolhonacamara.api.DevicesApi;
import net.coelho.deolhonacamara.api.model.DeviceRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Log4j2
public class DeviceController implements DevicesApi {

    private final JwtService jwtService;
    private final UserDeviceService userDeviceService;

    @Override
    public ResponseEntity<Void> registerDevice(String authorization, DeviceRequest deviceRequest) {
        var userId = jwtService.extractUserId(authorization);
        userDeviceService.registerDevice(userId, deviceRequest.getFcmToken());
        return ResponseEntity.status(201).build();
    }

    @Override
    public ResponseEntity<Void> unregisterDevice(String authorization, String fcmToken) {
        var userId = jwtService.extractUserId(authorization);
        userDeviceService.unregisterDevice(userId, fcmToken);
        return ResponseEntity.noContent().build();
    }
}

