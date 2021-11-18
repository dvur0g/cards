package ru.ruiners.cards.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import ru.ruiners.cards.controller.dto.ConnectRequest;
import ru.ruiners.cards.core.model.Game;
import ru.ruiners.cards.core.service.CardsGameService;
import ru.ruiners.cards.mapper.PlayerMapper;
import ru.ruiners.cards.model.PlayerRequest;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/game")
public class GameController {

    private final CardsGameService gameService;
    private final PlayerMapper playerMapper;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping("/start")
    public ResponseEntity<Game> start(@RequestBody PlayerRequest player) {
        log.info("start game request: {}", player);
        return ResponseEntity.ok(gameService.createGame(playerMapper.toPlayer(player), 3));
    }

    @PostMapping("/connect")
    public ResponseEntity<Game> connect(@RequestBody ConnectRequest request) {
        log.info("connect request: {}", request);
        return ResponseEntity.ok(gameService.connectToGame(playerMapper.toPlayer(request.getPlayer()), request.getGameId()));
    }

    @PostMapping("/connect/random")
    public ResponseEntity<Game> connectRandom(@RequestBody PlayerRequest player) {
        log.info("connect random {}", player);
        return ResponseEntity.ok(gameService.connectToRandomGame(playerMapper.toPlayer(player)));
    }

//    @PostMapping("/gameplay")
//    public ResponseEntity<Game> gamePlay(@RequestBody GamePlay request) throws NotFoundException, InvalidGameException {
//        log.info("gameplay: {}", request);
//        Game game = gameService.gamePlay(request);
//        simpMessagingTemplate.convertAndSend("/topic/game-progress/" + game.getGameId(), game);
//        return ResponseEntity.ok(game);
//    }
}
