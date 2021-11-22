package ru.ruiners.cards.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ruiners.cards.common.BusinessException;
import ru.ruiners.cards.controller.dto.GamePlayDto;
import ru.ruiners.cards.core.model.*;
import ru.ruiners.cards.core.model.enums.GameState;
import ru.ruiners.cards.core.repository.CardRepository;
import ru.ruiners.cards.core.repository.GameRepository;
import ru.ruiners.cards.core.repository.PlayerRepository;
import ru.ruiners.cards.core.repository.QuestionRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CardsGameService {

    private static final int MIN_PLAYERS_AMOUNT = 2;
    private static final int MAX_PLAYERS_AMOUNT = 10;

    private static final int STARTING_CARDS_AMOUNT = 10;

    private final GameRepository repository;
    private final CardRepository cardRepository;
    private final QuestionRepository questionRepository;
    private final PlayerRepository playerRepository;

    private final Random random = new Random();

    @Transactional
    public Game createGame(Player player, Integer minPlayersAmount) {
        if (minPlayersAmount > MAX_PLAYERS_AMOUNT || minPlayersAmount < MIN_PLAYERS_AMOUNT) {
            throw new BusinessException("Invalid players amount");
        }
        Game game = new Game();
        game.setState(GameState.CREATED);
        game.setMinPlayersAmount(minPlayersAmount);

        saveNewPlayer(player);
        game.setPlayers(List.of(player));

        game = repository.save(game);
        return game;
    }

    @Transactional
    public Game connectToGame(Player player, Long gameId) {
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
        return game;
    }

    @Transactional
    public Game disconnectFromGame(Player player) {
        Long gameId = playerRepository.getGameIdByUsername(player.getUsername());
        Game game = repository.findById(gameId).orElseThrow(() -> new BusinessException("Game not found"));

        game.getPlayers().removeIf(p -> p.getUsername().equals(player.getUsername()));
        repository.save(game);
        return game;
    }

    private void startGame(Game game) {
        game.getPlayers().forEach(player -> player.setCards(getRandomCards()));

        game.setCurrentQuestion(getRandomQuestion());
        game.setCurrentPlayer(game.getPlayers().get(random.nextInt(game.getPlayers().size())));

        game.setState(GameState.IN_PROGRESS);
        repository.save(game);
    }

    public Game gamePlay(GamePlayDto gamePlay) {
        Game game = repository.findById(gamePlay.getGameId()).orElseThrow(
                () -> new BusinessException("Game not found")
        );

        if (game.getPlayers().stream().noneMatch(player -> player.getUsername().equals(gamePlay.getUsername()))) {
            throw new BusinessException("No such player in the game");
        }

        return game;
    }

    private void saveNewPlayer(Player player) {
        if (playerRepository.existsByUsername(player.getUsername())) {
            throw new BusinessException("Player with this username playing at the moment");
        }

        player.setScore(0);
        playerRepository.save(player);
    }

    private List<Card> getRandomCards() {
        return cardRepository.getRandomCards(STARTING_CARDS_AMOUNT);
    }

    private Question getRandomQuestion() {
        return questionRepository.getRandomQuestion();
    }

    public List<Game> getGamesToConnect() {
        return repository.findAllByStateIn(List.of(GameState.CREATED, GameState.IN_PROGRESS));
    }

    private Game getGameById(Long gameId) {
        return repository.findById(gameId).orElseThrow(() -> new BusinessException("Game not found"));
    }
}
