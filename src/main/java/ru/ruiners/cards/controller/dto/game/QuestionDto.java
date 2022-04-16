package ru.ruiners.cards.controller.dto.game;

import lombok.Data;
import ru.ruiners.cards.core.model.enums.CensorType;

@Data
public class QuestionDto {

    private Long id;
    private String text;
    private CensorType type;

}
