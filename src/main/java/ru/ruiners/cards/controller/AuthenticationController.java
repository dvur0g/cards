package ru.ruiners.cards.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ruiners.cards.controller.dto.MeDto;
import ru.ruiners.cards.controller.dto.ResponseDto;
import ru.ruiners.cards.service.AuthenticationService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @GetMapping(path = "/me")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public MeDto me() {
        return new MeDto().setUsername(authenticationService.getUsername());
    }

    @GetMapping(path = "/logout")
    public ResponseDto logout(HttpServletRequest request) throws ServletException {
        request.logout();
        return new ResponseDto().setMessage("Success");
    }

}