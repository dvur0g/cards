package ru.ruiners.cards.controller.dto.game;

import lombok.Data;
import ru.ruiners.cards.core.model.enums.GameState;

import java.util.List;

@Data
public class GameDto {

    private Long id;
    private String name;
    private Integer minPlayersAmount;
    private GameState state;
    private int round;
    private PlayerDto currentPlayer;
    private CardDto victoriousAnswer;
    private List<PlayerDto> players;
    private QuestionDto currentQuestion;
    private PlayerDto winner;
    private TimerDto timer;

}
