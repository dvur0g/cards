package ru.ruiners.cards.controller.dto;

import lombok.Data;

@Data
public class ConnectDto {

    private PlayerDto player;
    private Long gameId;

}
