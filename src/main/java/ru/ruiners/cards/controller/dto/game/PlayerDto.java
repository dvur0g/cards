package ru.ruiners.cards.controller.dto.game;

import lombok.Data;

import java.util.List;

@Data
public class PlayerDto {

    private Long id;
    private String username;
    private Integer score;
    private List<CardDto> cards;
    private CardDto selectedAnswer;

}
