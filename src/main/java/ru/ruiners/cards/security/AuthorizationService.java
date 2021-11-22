package ru.ruiners.cards.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ruiners.cards.common.BusinessException;
import ru.ruiners.cards.controller.dto.AuthenticateDto;
import ru.ruiners.cards.core.mapper.PlayerMapper;
import ru.ruiners.cards.core.model.Player;
import ru.ruiners.cards.core.repository.PlayerRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private final PlayerRepository playerRepository;
    private final PlayerMapper playerMapper;

    public boolean authenticate(AuthenticateDto authenticateDto) {
        if (authenticateDto.getUsername() == null || authenticateDto.getPassword() == null) {
            throw new BusinessException("No username or password");
        }

        Optional<Player> player = playerRepository.findByUsername(authenticateDto.getUsername());

        if (player.isPresent()) {
            return player.get().getPassword().equals(authenticateDto.getPassword());
        }

        playerRepository.save(playerMapper.toPlayer(authenticateDto));
        return true;
    }

}
