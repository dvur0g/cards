package ru.ruiners.cards.model;

import lombok.Data;

@Data
public class Game {

    private String gameId;
    private PlayerRequest player1;
    private PlayerRequest player2;
    private GameStatus status;
    private int[][] board;
    private TicToe winner;

}
