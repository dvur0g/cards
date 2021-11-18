package ru.ruiners.cards.mapper;

import org.mapstruct.Mapper;
import ru.ruiners.cards.core.model.Player;
import ru.ruiners.cards.model.PlayerRequest;

@Mapper(componentModel = "spring")
public interface PlayerMapper {

    Player toPlayer(PlayerRequest request);
}
