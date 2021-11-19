package ru.ruiners.cards.mapper;

import org.mapstruct.Mapper;
import ru.ruiners.cards.core.model.Player;
import ru.ruiners.cards.controller.dto.PlayerDto;

@Mapper(componentModel = "spring", uses = CardMapper.class)
public interface PlayerMapper {

    Player toPlayer(PlayerDto request);
}
