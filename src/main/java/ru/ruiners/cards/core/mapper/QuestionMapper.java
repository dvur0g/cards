package ru.ruiners.cards.core.mapper;

import org.mapstruct.Mapper;
import ru.ruiners.cards.controller.dto.game.QuestionDto;
import ru.ruiners.cards.core.model.Question;

@Mapper(componentModel = "spring")
public interface QuestionMapper {

    QuestionDto toDto(Question question);

    Question toQuestion(QuestionDto dto);

}
