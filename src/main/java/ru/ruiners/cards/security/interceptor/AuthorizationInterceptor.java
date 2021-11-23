package ru.ruiners.cards.security.interceptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import ru.ruiners.cards.controller.dto.AuthenticateDto;
import ru.ruiners.cards.security.AuthorizationService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthorizationInterceptor implements HandlerInterceptor {

    private static final Set<String> excludeEndpoints = Set.of("/gameplay.html", "/css/style.css", "/js/socket_js.js", "/js/script.js", "/favicon.ico");

    private final AuthorizationService authorizationService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws JsonProcessingException {
        String uri = request.getRequestURI();
        if (!excludeEndpoints.contains(uri)) {
            String authorizationString = request.getHeader("Authorization");
            AuthenticateDto authorization = objectMapper.readValue(authorizationString, AuthenticateDto.class);
            return authorizationService.authenticate(authorization);
        }

        return true;
    }

}