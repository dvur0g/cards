package ru.ruiners.cards.mapper;

import org.mapstruct.Mapper;
import ru.ruiners.cards.controller.dto.CardDto;
import ru.ruiners.cards.core.model.Card;

@Mapper(componentModel = "spring")
public interface CardMapper {

    CardDto toDto(Card card);

    Card toCard(CardDto cardDto);

}
