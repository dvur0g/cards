package ru.ruiners.cards.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ruiners.cards.common.BusinessException;
import ru.ruiners.cards.controller.dto.AuthorizationDto;
import ru.ruiners.cards.core.mapper.PlayerMapper;
import ru.ruiners.cards.core.model.Player;
import ru.ruiners.cards.core.repository.PlayerRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private final PlayerRepository playerRepository;
    private final PlayerMapper playerMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public boolean authenticate(HttpServletRequest request) throws JsonProcessingException {
        AuthorizationDto authorizationDto = getAuthenticateDto(request);

        if (authorizationDto.getUsername() == null || authorizationDto.getPassword() == null) {
            throw new BusinessException("No username or password");
        }

        Optional<Player> player = playerRepository.findByUsername(authorizationDto.getUsername());

        if (player.isPresent()) {
            return player.get().getPassword().equals(authorizationDto.getPassword());
        }

        playerRepository.save(playerMapper.toPlayer(authorizationDto));
        return true;
    }

    public AuthorizationDto getAuthenticateDto(HttpServletRequest request) throws JsonProcessingException {
        String authorizationString = request.getHeader("Authorization");
        return objectMapper.readValue(authorizationString, AuthorizationDto.class);
    }

}
