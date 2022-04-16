package ru.ruiners.cards.core.mapper;

import org.mapstruct.Mapper;
import ru.ruiners.cards.core.model.Player;
import ru.ruiners.cards.controller.dto.game.PlayerDto;

@Mapper(componentModel = "spring", uses = CardMapper.class)
public interface PlayerMapper {

    Player toPlayer(PlayerDto request);

    Player toDto(Player player);

}
