package ru.ruiners.cards.security.service;

import lombok.RequiredArgsConstructor;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.ruiners.cards.controller.dto.authentication.MeDto;
import ru.ruiners.cards.core.model.Player;
import ru.ruiners.cards.core.repository.PlayerRepository;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final PlayerRepository playerRepository;

    public String getUsername() {
        return getAccessToken().getPreferredUsername();
    }

    public MeDto getMe() {
        AccessToken accessToken = getAccessToken();
        String username = accessToken.getPreferredUsername();

        if (playerRepository.findByUsername(username).isEmpty()) {
            playerRepository.save(new Player().setUsername(username).setScore(0));
        }

        return new MeDto()
                .setUsername(username)
                .setRoles(accessToken.getRealmAccess().getRoles());
    }

    private AccessToken getAccessToken() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        KeycloakPrincipal principal = (KeycloakPrincipal) auth.getPrincipal();
        KeycloakSecurityContext session = principal.getKeycloakSecurityContext();
        return session.getToken();
    }

}
