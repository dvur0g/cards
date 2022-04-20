package ru.ruiners.cards.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.ruiners.cards.controller.dto.authentication.*;
import ru.ruiners.cards.security.service.AuthenticationService;
import ru.ruiners.cards.security.service.KeycloakAuthenticationClient;
import ru.ruiners.cards.security.service.KeycloakClient;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final KeycloakAuthenticationClient keycloakAuthenticationClient;
    private final KeycloakClient keycloakClient;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> login(@RequestBody LoginDto loginDto) {
        AuthenticationResponseDto response = keycloakAuthenticationClient.authenticate(loginDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDto> register(@RequestBody RegistrationDto responseDto) {
        String userId = keycloakClient.registerUser(responseDto);
        return ResponseEntity.ok(new ResponseDto().setMessage(userId));
    }

    @GetMapping(path = "/me")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<MeDto> me() {
        return ResponseEntity.ok(authenticationService.getMe());
    }

    @GetMapping(path = "/logout")
    public ResponseEntity<ResponseDto> logout(HttpServletRequest request) throws ServletException {
        request.logout();
        return ResponseEntity.ok(new ResponseDto().setMessage("Success"));
    }

}