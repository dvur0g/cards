package ru.ruiners.cards.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.ruiners.cards.controller.dto.authentication.AuthenticationResponseDto;
import ru.ruiners.cards.controller.dto.authentication.LoginDto;
import ru.ruiners.cards.controller.dto.authentication.MeDto;
import ru.ruiners.cards.controller.dto.authentication.ResponseDto;
import ru.ruiners.cards.security.service.AuthenticationService;
import ru.ruiners.cards.security.service.KeycloakAuthenticationClient;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final KeycloakAuthenticationClient keycloakAuthenticationClient;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> login(@RequestBody LoginDto loginDto) {
        AuthenticationResponseDto response = keycloakAuthenticationClient.authenticate(loginDto);
        return ResponseEntity.ok(response);
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