package br.com.deolhonacamara.api.controller;

import br.com.deolhonacamara.api.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import net.coelho.deolhonacamara.api.AuthApi;
import net.coelho.deolhonacamara.api.model.AuthResponseDTO;
import net.coelho.deolhonacamara.api.model.RequestLogin;
import net.coelho.deolhonacamara.api.model.RequestNewUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller responsável por gerenciar as operações de autenticação e
 * autorização.
 * Implementa a interface AuthApi e fornece endpoints para login, recuperação de
 * senha
 * e reset de senha.
 * 
 * @author Personal Finance Team
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequiredArgsConstructor
@Log4j2
public class AuthController implements AuthApi {


    private final UserService userService;

    @Override
    public ResponseEntity<AuthResponseDTO> apiV1AuthLoginPost(RequestLogin requestLogin) {
        return ResponseEntity.ok(userService.doLogin(requestLogin));
    }

    @Override
    public ResponseEntity<AuthResponseDTO> apiV1AuthRegisterPost(RequestNewUser requestNewUser) {
        return ResponseEntity.ok(userService.createUser(requestNewUser));
    }
}