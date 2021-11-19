package ru.ruiners.cards.mapper;

import org.mapstruct.Mapper;
import ru.ruiners.cards.controller.dto.GameDto;
import ru.ruiners.cards.core.model.Game;

@Mapper(componentModel = "spring")
public interface GameMapper {

    GameDto toDto(Game game);

}
