package br.com.deolhonacamara.api.service;

import br.com.deolhonacamara.api.repository.UserDeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserDeviceService {

    private final UserDeviceRepository repository;

    public void registerDevice(UUID userId, String fcmToken) {
        repository.upsertDevice(userId, fcmToken);
    }

    public void unregisterDevice(UUID userId, String fcmToken) {
        repository.deleteDevice(userId, fcmToken);
    }
}

