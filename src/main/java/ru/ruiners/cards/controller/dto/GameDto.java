package ru.ruiners.cards.controller.dto;

import lombok.Data;
import ru.ruiners.cards.core.model.enums.GameState;

import java.util.List;

@Data
public class GameDto {

    private Long id;
    private Integer minPlayersAmount;
    private GameState state;
    private PlayerDto currentPlayer;
    private CardDto victoriousAnswer;
    private List<PlayerDto> players;
    private QuestionDto currentQuestion;
    private PlayerDto winner;

}
