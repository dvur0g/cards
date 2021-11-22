package ru.ruiners.cards.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.ruiners.cards.common.BusinessException;
import ru.ruiners.cards.controller.dto.GameDto;
import ru.ruiners.cards.controller.dto.GamePlayDto;
import ru.ruiners.cards.core.mapper.GameMapper;
import ru.ruiners.cards.core.model.Card;
import ru.ruiners.cards.core.model.Game;
import ru.ruiners.cards.core.model.Player;
import ru.ruiners.cards.core.model.Question;
import ru.ruiners.cards.core.model.enums.GameState;
import ru.ruiners.cards.core.repository.CardRepository;
import ru.ruiners.cards.core.repository.GameRepository;
import ru.ruiners.cards.core.repository.PlayerRepository;
import ru.ruiners.cards.core.repository.QuestionRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameService {

    private static final int MIN_PLAYERS_AMOUNT = 2;
    private static final int MAX_PLAYERS_AMOUNT = 10;
    private static final int STARTING_CARDS_AMOUNT = 10;

    private static final String TOPIC = "/topic/game-progress/";

    private final GameRepository repository;
    private final CardRepository cardRepository;
    private final QuestionRepository questionRepository;
    private final PlayerRepository playerRepository;

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final GameMapper mapper;

    private final Random random = new Random();


    @Transactional
    public GameDto createGame(Player player, Integer minPlayersAmount) {
        if (minPlayersAmount > MAX_PLAYERS_AMOUNT || minPlayersAmount < MIN_PLAYERS_AMOUNT) {
            throw new BusinessException("Invalid players amount");
        }
        Game game = new Game();
        game.setState(GameState.CREATED);
        game.setMinPlayersAmount(minPlayersAmount);

        saveNewPlayer(player);
        game.setPlayers(List.of(player));

        game = repository.save(game);
        return mapper.toDto(game);
    }

    @Transactional
    public GameDto connectToGame(Player player, Long gameId) {
        var game = getGameById(gameId);

        if (game.getPlayers().size() > MAX_PLAYERS_AMOUNT) {
            throw new BusinessException("Max players amount reached");
        }

        saveNewPlayer(player);
        game.getPlayers().add(player);

        if (game.getPlayers().size() >= game.getMinPlayersAmount()) {
            startGame(game);
        }
        repository.save(game);

        GameDto result = mapper.toDto(game);
        simpMessagingTemplate.convertAndSend(TOPIC + result.getId(), result);
        return result;
    }

    @Transactional
    public void disconnectFromGame(Player player) {
        if (player.getUsername() == null) {
            throw new BusinessException("Null username");
        }

        Long gameId = playerRepository.getGameIdByUsername(player.getUsername())
                .orElseThrow(() -> new BusinessException("Game id not found"));

        Game game = repository.findById(gameId)
                .orElseThrow(() -> new BusinessException("Game not found"));

        game.getPlayers().removeIf(p -> p.getUsername().equals(player.getUsername()));

        GameDto result = mapper.toDto(repository.save(game));
        simpMessagingTemplate.convertAndSend(TOPIC + result.getId(), result);
    }

    private void startGame(Game game) {
        game.getPlayers().forEach(player -> player.setCards(getRandomCards()));

        game.setCurrentQuestion(getRandomQuestion());
        game.setCurrentPlayer(game.getPlayers().get(random.nextInt(game.getPlayers().size())));

        game.setState(GameState.IN_PROGRESS);
        repository.save(game);
    }

    public GameDto gamePlay(GamePlayDto gamePlay) {
        Game game = repository.findById(gamePlay.getGameId()).orElseThrow(
                () -> new BusinessException("Game not found")
        );

        if (game.getPlayers().stream().noneMatch(player -> player.getUsername().equals(gamePlay.getUsername()))) {
            throw new BusinessException("No such player in the game");
        }

        GameDto result = mapper.toDto(repository.save(game));
        simpMessagingTemplate.convertAndSend(TOPIC + result.getId(), result);
        return result;
    }

    private void saveNewPlayer(Player player) {
        playerRepository.getGameIdByUsername(player.getUsername())
                .ifPresent(gameId -> repository.findById(gameId)
                        .get().getPlayers()
                        .removeIf(p -> p.getUsername().equals(player.getUsername()))
                );
        player.setScore(0);
        playerRepository.save(player);
    }

    public List<GameDto> getGamesToConnect() {
        return repository.findAllByStateIn(List.of(GameState.CREATED, GameState.IN_PROGRESS))
                .stream().map(mapper::toDto).collect(Collectors.toList());
    }

    private Game getGameById(Long gameId) {
        return repository.findById(gameId).orElseThrow(() -> new BusinessException("Game not found"));
    }

    private List<Card> getRandomCards() {
        return cardRepository.getRandomCards(STARTING_CARDS_AMOUNT);
    }

    private Question getRandomQuestion() {
        return questionRepository.getRandomQuestion();
    }

}
