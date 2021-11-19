package ru.ruiners.cards.controller.dto;

import lombok.Data;
import ru.ruiners.cards.controller.dto.CardDto;

import java.util.List;

@Data
public class PlayerDto {

    private Long id;
    private String username;
    private Integer score;
    private List<CardDto> cards;

}
