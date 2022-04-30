package ru.ruiners.cards.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ru.ruiners.cards.controller.dto.authentication.AuthenticationResponseDto;
import ru.ruiners.cards.controller.dto.authentication.LoginDto;

@Component
@RequiredArgsConstructor
@Slf4j
public class KeycloakAuthenticationClient {

    private static final String GRANT_TYPE = "grant_type";
    private static final String GRANT_TYPE_PASSWORD = "password";

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    private static final String CLIENT_ID = "client_id";
    private static final String CLIENT_SECRET = "client_secret";

    @Value("${keycloak.auth-server-url}")
    private String authUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.resource}")
    private String clientId;

    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

    private final RestTemplate restTemplate;

    public AuthenticationResponseDto authenticate(LoginDto authenticationRequest) {
        log.info("Authorize user {}", authenticationRequest.getUsername());

        MultiValueMap<String, String> paramMap = buildGeneralParamMap();
        paramMap.add(GRANT_TYPE, GRANT_TYPE_PASSWORD);
        paramMap.add(USERNAME, authenticationRequest.getUsername());
        paramMap.add(PASSWORD, authenticationRequest.getPassword());
        paramMap.add(CLIENT_SECRET, clientSecret);

        return authenticationExchange(paramMap);
    }

    private AuthenticationResponseDto authenticationExchange(MultiValueMap<String, String> paramMap) {
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(paramMap, buildHttpHeaders());

        return restTemplate.exchange(authUrl + "/realms/" + realm + "/protocol/openid-connect/token",
                HttpMethod.POST,
                entity,
                AuthenticationResponseDto.class).getBody();
    }

    private MultiValueMap<String, String> buildGeneralParamMap() {
        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add(CLIENT_ID, clientId);
        return paramMap;
    }

    private HttpHeaders buildHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return headers;
    }

}
