package ru.ruiners.cards.controller.dto;

import lombok.Data;

@Data
public class SelectVictoriousAnswerDto {

    private Long gameId;
    private Long victoriousPlayerId;

}
