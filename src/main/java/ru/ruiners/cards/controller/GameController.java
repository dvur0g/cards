package ru.ruiners.cards.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import ru.ruiners.cards.controller.dto.ConnectDto;
import ru.ruiners.cards.controller.dto.GameDto;
import ru.ruiners.cards.core.model.Game;
import ru.ruiners.cards.core.service.CardsGameService;
import ru.ruiners.cards.mapper.GameMapper;
import ru.ruiners.cards.mapper.PlayerMapper;
import ru.ruiners.cards.controller.dto.PlayerDto;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/game")
public class GameController {

    private final CardsGameService gameService;
    private final GameMapper gameMapper;
    private final PlayerMapper playerMapper;

    private final SimpMessagingTemplate simpMessagingTemplate;

    private static final int MIN_PLAYERS_AMOUNT = 2;

    @PostMapping("/start")
    public ResponseEntity<GameDto> start(@RequestBody PlayerDto player) {
        log.info("start game request: {}", player);
        Game game = gameService.createGame(playerMapper.toPlayer(player), MIN_PLAYERS_AMOUNT);
        return ResponseEntity.ok(gameMapper.toDto(game));
    }

    @PostMapping("/connect")
    public ResponseEntity<GameDto> connect(@RequestBody ConnectDto request) {
        log.info("connect request: {}", request);
        Game game = gameService.connectToGame(playerMapper.toPlayer(request.getPlayer()), request.getGameId());
        return ResponseEntity.ok(gameMapper.toDto(game));
    }

    @PostMapping("/disconnect")
    public void disconnect(@RequestBody PlayerDto player) {
        log.info("disconnect request: {}", player);
        gameService.disconnectFromGame(playerMapper.toPlayer(player));
    }

    @PostMapping("/connect/random")
    public ResponseEntity<GameDto> connectRandom(@RequestBody PlayerDto player) {
        log.info("connect random {}", player);
        Game game = gameService.connectToRandomGame(playerMapper.toPlayer(player));
        return ResponseEntity.ok(gameMapper.toDto(game));
    }

    @GetMapping("/list")
    public ResponseEntity<List<GameDto>> getAvailableGamesList() {
        List<Game> availableGamesList = gameService.getGamesToConnect();
        log.info("get games list: {}", availableGamesList);
        return ResponseEntity.ok(availableGamesList.stream().map(gameMapper::toDto).collect(Collectors.toList()));
    }

//    @PostMapping("/gameplay")
//    public ResponseEntity<Game> gamePlay(@RequestBody GamePlay request) throws NotFoundException, InvalidGameException {
//        log.info("gameplay: {}", request);
//        Game game = gameService.gamePlay(request);
//        simpMessagingTemplate.convertAndSend("/topic/game-progress/" + game.getGameId(), game);
//        return ResponseEntity.ok(game);
//    }
}
