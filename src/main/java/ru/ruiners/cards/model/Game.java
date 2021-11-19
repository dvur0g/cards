package ru.ruiners.cards.model;

import lombok.Data;
import ru.ruiners.cards.controller.dto.PlayerDto;

@Data
public class Game {

    private String gameId;
    private PlayerDto player1;
    private PlayerDto player2;
    private GameStatus status;
    private int[][] board;
    private TicToe winner;

}
