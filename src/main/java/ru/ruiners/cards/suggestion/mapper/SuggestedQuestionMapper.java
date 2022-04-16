package ru.ruiners.cards.suggestion.mapper;

import org.mapstruct.Mapper;
import ru.ruiners.cards.controller.dto.suggestion.SuggestedQuestionDto;
import ru.ruiners.cards.core.model.Question;
import ru.ruiners.cards.suggestion.model.SuggestedQuestion;

@Mapper(componentModel = "spring")
public interface SuggestedQuestionMapper {

    SuggestedQuestionDto toDto(SuggestedQuestion suggestedQuestion);

    SuggestedQuestion toSuggestedQuestion(SuggestedQuestionDto dto);

    Question toQuestion(SuggestedQuestion suggestedQuestion);

}
