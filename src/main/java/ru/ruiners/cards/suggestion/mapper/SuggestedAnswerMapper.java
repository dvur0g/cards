package ru.ruiners.cards.suggestion.mapper;

import org.mapstruct.Mapper;
import ru.ruiners.cards.controller.dto.suggestion.SuggestedAnswerDto;
import ru.ruiners.cards.core.model.Card;
import ru.ruiners.cards.suggestion.model.SuggestedAnswer;

@Mapper(componentModel = "spring")
public interface SuggestedAnswerMapper {

    SuggestedAnswerDto toDto(SuggestedAnswer suggestedAnswer);

    SuggestedAnswer toSuggestedAnswer(SuggestedAnswerDto dto);

    Card toCard(SuggestedAnswer suggestedAnswer);

}
