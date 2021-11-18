package ru.ruiners.cards.controller.dto;

import ru.ruiners.cards.model.PlayerRequest;
import lombok.Data;

@Data
public class ConnectRequest {
    private PlayerRequest player;
    private Long gameId;
}
