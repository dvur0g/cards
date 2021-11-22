package ru.ruiners.cards.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ruiners.cards.controller.dto.ConnectDto;
import ru.ruiners.cards.controller.dto.GameDto;
import ru.ruiners.cards.controller.dto.GamePlayDto;
import ru.ruiners.cards.controller.dto.PlayerDto;
import ru.ruiners.cards.core.mapper.PlayerMapper;
import ru.ruiners.cards.core.service.GameService;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;
    private final PlayerMapper playerMapper;

    private static final int MIN_PLAYERS_AMOUNT = 3;

    @PostMapping("/create")
    public ResponseEntity<GameDto> start(@RequestBody PlayerDto player) {
        log.info("create game request: {}", player);
        GameDto game = gameService.createGame(playerMapper.toPlayer(player), MIN_PLAYERS_AMOUNT);
        return ResponseEntity.ok(game);
    }

    @PostMapping("/connect")
    public ResponseEntity<GameDto> connect(@RequestBody ConnectDto request) {
        log.info("connect request: {}", request);
        GameDto game = gameService.connectToGame(
                playerMapper.toPlayer(request.getPlayer()), request.getGameId());
        log.info("send gameDto to all players {}", game);
        return ResponseEntity.ok(game);
    }

    @PostMapping("/disconnect")
    public void disconnect(@RequestBody PlayerDto player) {
        log.info("disconnect request: {}", player);
        gameService.disconnectFromGame(playerMapper.toPlayer(player));
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

}
