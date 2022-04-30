package ru.ruiners.cards.security.client;

import liquibase.repackaged.org.apache.commons.lang3.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.ruiners.cards.common.BusinessException;
import ru.ruiners.cards.controller.dto.authentication.RegistrationDto;

import javax.annotation.PostConstruct;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeycloakClient {

    public static final String USER_REGEX = ".*/([^/]+)$";
    public static final String USER_REGEX_ARG_POS = "$1";
    private static final String ROLE_USER = "USER";

    @Value("${keycloak.auth-server-url}")
    private String authUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.resource}")
    private String clientId;

    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

    private RealmResource realmResource;

    @PostConstruct
    public void initialize() {
        Keycloak keycloak = getKeycloakInstance();
        realmResource = keycloak.realm(realm);
    }

    public String registerUser(RegistrationDto request) {
        UserRepresentation user = createUserRepresentation(request);
        return registerUser(user);
    }

    private String registerUser(UserRepresentation user) {
        UsersResource usersResource = getUsersResource();
        int count = usersResource.count();
        Response response = usersResource.create(user);
        if (response == null || !response.getStatusInfo().equals(Response.Status.CREATED)) {
            throw new BusinessException("Пользователь не создан");
        }
        String userId = extractUserId(response);
        addUserRole(userId);
        return userId;
    }

    private UsersResource getUsersResource() {
        return realmResource.users();
    }

    private Keycloak getKeycloakInstance() {
        return KeycloakBuilder.builder()
                .serverUrl(authUrl)
                .realm(realm)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .build();
    }

    private UserRepresentation createUserRepresentation(RegistrationDto registrationDto) {
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(registrationDto.getUsername());
        setBasicAttributes(registrationDto, user);
        return user;
    }

    private void setBasicAttributes(RegistrationDto registrationDto, UserRepresentation userRepresentation) {
        userRepresentation.setFirstName(registrationDto.getName());
        userRepresentation.setLastName(registrationDto.getSurname());
        userRepresentation.setEmail(registrationDto.getEmail());
        if (StringUtils.isNotBlank(registrationDto.getPassword())) {
            userRepresentation.setCredentials(Collections.singletonList(
                    createUserCredentials(registrationDto.getPassword())));
        }
    }

    private CredentialRepresentation createUserCredentials(String password) {
        CredentialRepresentation credentials = new CredentialRepresentation();
        credentials.setTemporary(false);
        credentials.setType(CredentialRepresentation.PASSWORD);
        credentials.setValue(password);
        return credentials;
    }

    private String extractUserId(Response response) {
        return response.getLocation().getPath().replaceAll(USER_REGEX, USER_REGEX_ARG_POS);
    }

    private void addUserRole(String userId) {
        RoleRepresentation role = realmResource.roles().get(ROLE_USER).toRepresentation();
        getUsersResource().get(userId).roles().realmLevel().add(List.of(role));
    }

}
