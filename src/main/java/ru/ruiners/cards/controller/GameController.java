package ru.ruiners.cards.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.ruiners.cards.controller.dto.game.*;
import ru.ruiners.cards.core.service.GameService;
import ru.ruiners.cards.security.service.AuthenticationService;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;
    private final AuthenticationService authenticationService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<GameDto> create(@RequestBody CreateGameDto dto) {
        String username = authenticationService.getUsername();
        log.info("create game request from: {}", username);

        GameDto game = gameService.createGame(username, dto.getName());
        log.info("created game: {}", game);

        return ResponseEntity.ok(game);
    }

    @PostMapping("/connect")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<GameDto> connect(@RequestBody RequestDto request) {
        log.info("connect request: {}", request);
        GameDto game = gameService.connectToGame(authenticationService.getUsername(), request.getGameId());
        log.info("send gameDto to all players {}", game);
        return ResponseEntity.ok(game);
    }

    @PostMapping("/disconnect")
    @PreAuthorize("hasRole('USER')")
    public void disconnect() {
        String username = authenticationService.getUsername();
        log.info("disconnect request from: {}", username);
        gameService.disconnectFromGame(username);
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<GameDto>> getAvailableGamesList() {
        return ResponseEntity.ok(gameService.getGamesToConnect());
    }

    @PostMapping("/gameplay")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<GameDto> gamePlay(@RequestBody GamePlayDto body) {
        log.info("gameplay: {}", body);

        return ResponseEntity.ok(gameService.gamePlay(body, authenticationService.getUsername()));
    }

    @PostMapping("/select-card")
    @PreAuthorize("hasRole('USER')")
    public void selectCard(@RequestBody SelectCardDto selectCard) {
        log.info("select card: {}", selectCard);
        gameService.selectAnswer(selectCard, authenticationService.getUsername());
    }

    @PostMapping("/select-victorious-answer")
    @PreAuthorize("hasRole('USER')")
    public void selectVictoriousAnswer(@RequestBody SelectVictoriousAnswerDto selectVictoriousAnswer) {
        log.info("select victorious answer: {}", selectVictoriousAnswer);
        gameService.selectVictoriousAnswer(selectVictoriousAnswer, authenticationService.getUsername());
    }

}
