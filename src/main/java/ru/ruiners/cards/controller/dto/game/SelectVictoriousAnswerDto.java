package ru.ruiners.cards.controller.dto.game;

import lombok.Data;

@Data
public class SelectVictoriousAnswerDto {

    private Long gameId;
    private Long victoriousPlayerId;

}
