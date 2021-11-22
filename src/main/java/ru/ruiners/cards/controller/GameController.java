package ru.ruiners.cards.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import ru.ruiners.cards.controller.dto.ConnectDto;
import ru.ruiners.cards.controller.dto.GameDto;
import ru.ruiners.cards.core.model.Game;
import ru.ruiners.cards.controller.dto.GamePlayDto;
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
    private static final String TOPIC = "/topic/game-progress/";

    @PostMapping("/create")
    public ResponseEntity<GameDto> start(@RequestBody PlayerDto player) {
        log.info("create game request: {}", player);
        Game game = gameService.createGame(playerMapper.toPlayer(player), MIN_PLAYERS_AMOUNT);

        return ResponseEntity.ok(gameMapper.toDto(game));
    }

    @PostMapping("/connect")
    public ResponseEntity<GameDto> connect(@RequestBody ConnectDto request) {
        log.info("connect request: {}", request);
        Game game = gameService.connectToGame(playerMapper.toPlayer(request.getPlayer()), request.getGameId());
        GameDto gameDto = gameMapper.toDto(game);

        log.info("send game to all players {}", game);
        log.info("send gameDto to all players {}", gameDto);
        simpMessagingTemplate.convertAndSend(TOPIC + gameDto.getId(), gameDto);

        return ResponseEntity.ok(gameDto);
    }

    @PostMapping("/disconnect")
    public void disconnect(@RequestBody PlayerDto player) {
        log.info("disconnect request: {}", player);
        GameDto game = gameMapper.toDto(gameService.disconnectFromGame(playerMapper.toPlayer(player)));
        simpMessagingTemplate.convertAndSend(TOPIC + game.getId(), game);
    }

    @GetMapping("/list")
    public ResponseEntity<List<GameDto>> getAvailableGamesList() {
        List<Game> availableGamesList = gameService.getGamesToConnect();
        log.info("get games list: {}", availableGamesList);

        return ResponseEntity.ok(availableGamesList.stream().map(gameMapper::toDto)
                .collect(Collectors.toList()));
    }

    @PostMapping("/gameplay")
    public ResponseEntity<GameDto> gamePlay(@RequestBody GamePlayDto request) {
        log.info("gameplay: {}", request);
        GameDto gameDto = gameMapper.toDto(gameService.gamePlay(request));

        simpMessagingTemplate.convertAndSend(TOPIC + gameDto.getId(), gameDto);
        return ResponseEntity.ok(gameDto);
    }

}
