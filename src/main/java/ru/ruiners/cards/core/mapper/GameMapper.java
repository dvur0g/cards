package ru.ruiners.cards.core.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.ruiners.cards.controller.dto.GameDto;
import ru.ruiners.cards.controller.dto.TimerDto;
import ru.ruiners.cards.core.model.Game;

@Mapper(componentModel = "spring", uses = {QuestionMapper.class, PlayerMapper.class})
public interface GameMapper {

    GameDto toDto(Game game);

    @Mapping(source = "countdown", target = "timer", qualifiedByName = "toTimer")
    GameDto toDto(Game game, Integer countdown);

    @Named("toTimer")
    default TimerDto countdownToTimer(int countdown) {
        return new TimerDto().setCountdown(countdown);
    }
}
