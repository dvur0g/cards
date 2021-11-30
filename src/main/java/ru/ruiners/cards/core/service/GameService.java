package ru.ruiners.cards.core.service;

import liquibase.pro.packaged.P;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.ruiners.cards.common.BusinessException;
import ru.ruiners.cards.controller.dto.AuthorizationDto;
import ru.ruiners.cards.controller.dto.GameDto;
import ru.ruiners.cards.controller.dto.GamePlayDto;
import ru.ruiners.cards.controller.dto.SelectCardDto;
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
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameService {

    private static final int MIN_PLAYERS_AMOUNT = 2;
    private static final int MAX_PLAYERS_AMOUNT = 10;
    private static final int STARTING_CARDS_AMOUNT = 10;

    private static final String TOPIC = "/topic/game-progress/";
    private static final Set<GameState> playingGameStates = Set.of(GameState.CREATED,
            GameState.PROCESSING, GameState.SELECTING_ANSWERS, GameState.SHOW_VICTORIOUS_ANSWER);

    private final GameRepository repository;
    private final CardRepository cardRepository;
    private final QuestionRepository questionRepository;
    private final PlayerRepository playerRepository;

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final GameMapper mapper;

    private final Random random = new Random();
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    @Transactional
    public GameDto createGame(String username, Integer minPlayersAmount) {
        if (minPlayersAmount > MAX_PLAYERS_AMOUNT || minPlayersAmount < MIN_PLAYERS_AMOUNT) {
            throw new BusinessException("Invalid players amount");
        }
        Game game = new Game();
        game.setState(GameState.CREATED);
        game.setMinPlayersAmount(minPlayersAmount);

        Player player = preparePlayer(username, game);
        game.setPlayers(List.of(player));

        game = repository.save(game);
        return mapper.toDto(game);
    }

    @Transactional
    public GameDto connectToGame(String username, Long gameId) {
        var game = getGameById(gameId);

        if (game.getPlayers().size() > MAX_PLAYERS_AMOUNT) {
            throw new BusinessException("Max players amount reached");
        }

        Player player = preparePlayer(username, game);
        game.getPlayers().add(player);

        if (game.getPlayers().size() >= game.getMinPlayersAmount()) {
            if (game.getState().equals(GameState.CREATED)) {
                game.getPlayers().forEach(p -> p.setCards(getRandomCards()));
                game.setState(GameState.PROCESSING);

                executor.schedule(() -> setSelectingAnswersState(game.getId()), 5, TimeUnit.SECONDS);
            } else {
                player.setCards(getRandomCards());
            }
        }
        repository.save(game);

        GameDto result = mapper.toDto(game);
        simpMessagingTemplate.convertAndSend(TOPIC + result.getId(), result);
        return result;
    }

    @Transactional
    public void disconnectFromGame(String username) {
        if (username == null || username.equals("")) {
            throw new BusinessException("Null username");
        }

        Long gameId = playerRepository.getGameIdByUsername(username)
                .orElseThrow(() -> new BusinessException("Game id not found"));

        Game game = repository.findById(gameId)
                .orElseThrow(() -> new BusinessException("Game not found"));

        game.getPlayers().removeIf(p -> p.getUsername().equals(username));

        GameDto result = mapper.toDto(repository.save(game));
        simpMessagingTemplate.convertAndSend(TOPIC + result.getId(), result);
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

    public List<GameDto> getGamesToConnect() {
        return repository.findAllByStateIn(playingGameStates)
                .stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Transactional
    public void selectAnswer(SelectCardDto selectCard, AuthorizationDto authorizationDto) {
        Game game = getGameById(selectCard.getGameId());

        if (!game.getState().equals(GameState.SELECTING_ANSWERS)) {
            return;
        }

        Player player = game.getPlayers().stream()
                .filter(p -> p.getUsername().equals(authorizationDto.getUsername()))
                .findAny().orElseThrow(() -> new BusinessException("No such player in the game"));

        if (player.equals(game.getCurrentPlayer())) {
            throw new BusinessException("Player is host");
        }

        if (player.getSelectedAnswer() != null) {
            throw new BusinessException("Player already selected answer");
        }

        Card card = player.getCards().stream()
                .filter(c -> c.getId().equals(selectCard.getCardId()))
                .findAny().orElseThrow(() -> new BusinessException("Player has no such card"));

        player.getCards().remove(card);
        player.setSelectedAnswer(card);
        playerRepository.save(player);

        GameDto result = mapper.toDto(repository.save(game));
        simpMessagingTemplate.convertAndSend(TOPIC + result.getId(), result);
    }

    @Transactional
    public void setProcessingState(Long gameId) {
        log.info("set PROCESSING state for game {}", gameId);

        if (isFinishedState(gameId)) {
            return;
        }

        Game game = getGameById(gameId);
        game.setState(GameState.PROCESSING);
        repository.save(game);

        GameDto result = mapper.toDto(game);
        simpMessagingTemplate.convertAndSend(TOPIC + result.getId(), result);

        executor.schedule(() -> setSelectingAnswersState(game.getId()), 5, TimeUnit.SECONDS);
    }

    @Transactional
    public void setSelectingAnswersState(Long gameId) {
        log.info("set SELECTING_ANSWERS state for game {}", gameId);
        Game game = getGameById(gameId);

        game.setCurrentQuestion(getRandomQuestion());
        game.setCurrentPlayer(game.getPlayers().get(random.nextInt(game.getPlayers().size())));
        game.setState(GameState.SELECTING_ANSWERS);
        repository.save(game);

        GameDto result = mapper.toDto(game);
        simpMessagingTemplate.convertAndSend(TOPIC + result.getId(), result);

        executor.schedule(() -> setShowingVictoriousAnswerState(game.getId()), 20, TimeUnit.SECONDS);
    }

    @Transactional
    public void setShowingVictoriousAnswerState(Long gameId) {
        log.info("set SHOWING_VICTORIOUS_ANSWER state for game {}", gameId);
        Game game = getGameById(gameId);

        if (game.getState().equals(GameState.SHOW_VICTORIOUS_ANSWER)) {
            return;
        }

        Optional<Player> victoriousPlayer = game.getPlayers().stream()
                .filter(player -> player.getSelectedAnswer() != null)
                .findAny();

        if (victoriousPlayer.isPresent()) {
            game.setVictoriousAnswer(victoriousPlayer.get().getSelectedAnswer());
            victoriousPlayer.get().setSelectedAnswer(null);
        }
        game.setState(GameState.SHOW_VICTORIOUS_ANSWER);
        game.getPlayers().forEach(Player::removeSelectedAnswer);
        repository.save(game);

        GameDto result = mapper.toDto(game);
        simpMessagingTemplate.convertAndSend(TOPIC + result.getId(), result);

        executor.schedule(() -> setProcessingState(game.getId()), 10, TimeUnit.SECONDS);
    }

    @Transactional
    public boolean isFinishedState(Long gameId) {
        Game game = getGameById(gameId);

        if (game.getWinnerOptional().isEmpty()) {
            return false;
        }

        log.info("set FINISHED state for game {}", game.getId());

        game.setWinner(game.getWinnerOptional().get());
        game.setState(GameState.FINISHED);

        GameDto result = mapper.toDto(game);
        simpMessagingTemplate.convertAndSend(TOPIC + result.getId(), result);
        return true;
    }

    @Transactional
    public Player preparePlayer(String username, Game game) {
        Player player = playerRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException("Player not found"));

        playerRepository.getGameIdByUsername(username)
                .ifPresent(gameId -> repository.findById(gameId)
                        .get().getPlayers()
                        .removeIf(p -> p.getUsername().equals(username))
                );

        playerRepository.removeCardsByUsername(player.getUsername());
        player.setCards(null);
        player.setScore(0);
        return playerRepository.save(player);
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
