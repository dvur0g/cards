package ru.ruiners.cards.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ruiners.cards.controller.dto.*;
import ru.ruiners.cards.core.service.GameService;
import ru.ruiners.cards.security.AuthorizationService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;
    private final AuthorizationService authorizationService;

    private static final int MIN_PLAYERS_AMOUNT = 2;

    @PostMapping("/create")
    public ResponseEntity<GameDto> start(@RequestBody RequestDto request) {
        log.info("create game request: {}", request);
        GameDto game = gameService.createGame(request.getUsername(), MIN_PLAYERS_AMOUNT);
        return ResponseEntity.ok(game);
    }

    @PostMapping("/connect")
    public ResponseEntity<GameDto> connect(@RequestBody RequestDto request) {
        log.info("connect request: {}", request);
        GameDto game = gameService.connectToGame(request.getUsername(), request.getGameId());
        log.info("send gameDto to all players {}", game);
        return ResponseEntity.ok(game);
    }

    @PostMapping("/disconnect")
    public void disconnect(@RequestBody RequestDto request) {
        log.info("disconnect request: {}", request);
        gameService.disconnectFromGame(request.getUsername());
    }

    @GetMapping("/list")
    public ResponseEntity<List<GameDto>> getAvailableGamesList() {
        return ResponseEntity.ok(gameService.getGamesToConnect());
    }

    @PostMapping("/gameplay")
    public ResponseEntity<GameDto> gamePlay(@RequestBody GamePlayDto request) {
        log.info("gameplay: {}", request);
        return ResponseEntity.ok(gameService.gamePlay(request));
    }

    @PostMapping("/select-card")
    public void selectCard(@RequestBody SelectCardDto selectCard, HttpServletRequest request) throws JsonProcessingException {
        log.info("select card: {}", selectCard);
        gameService.selectAnswer(selectCard, authorizationService.getAuthenticateDto(request));
    }

    @PostMapping("/select-victorious-answer")
    public void selectVictoriousAnswer(@RequestBody SelectVictoriousAnswerDto selectVictoriousAnswer,
                                       HttpServletRequest request) throws JsonProcessingException {
        log.info("select victorious answer: {}", selectVictoriousAnswer);
        gameService.selectVictoriousAnswer(selectVictoriousAnswer, authorizationService.getAuthenticateDto(request));
    }

}
