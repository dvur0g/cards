package ru.ruiners.cards.service;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.ruiners.cards.controller.dto.authentication.MeDto;

@Service
public class AuthenticationService {

    public String getUsername() {
        return getAccessToken().getPreferredUsername();
    }

    public MeDto getMe() {
        AccessToken accessToken = getAccessToken();
        return new MeDto()
                .setUsername(accessToken.getPreferredUsername())
                .setRoles(accessToken.getRealmAccess().getRoles());
    }

    private AccessToken getAccessToken() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        KeycloakPrincipal principal = (KeycloakPrincipal) auth.getPrincipal();
        KeycloakSecurityContext session = principal.getKeycloakSecurityContext();
        return session.getToken();
    }

}
