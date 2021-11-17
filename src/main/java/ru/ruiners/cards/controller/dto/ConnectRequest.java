package ru.ruiners.cards.controller.dto;

import ru.ruiners.cards.model.Player;
import lombok.Data;

@Data
public class ConnectRequest {
    private Player player;
    private String gameId;
}
