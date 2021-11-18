package ru.ruiners.cards.core.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.ruiners.cards.common.BusinessException;
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

    public Game createGame(Player player, Integer minPlayersAmount) {
        if (minPlayersAmount > MAX_PLAYERS_AMOUNT || minPlayersAmount < MIN_PLAYERS_AMOUNT) {
            throw new BusinessException("Invalid players amount");
        }
        Game game = new Game();
        game.setState(GameState.CREATED);
        game.setPlayers(List.of(player));
        game.setMinPlayersAmount(minPlayersAmount);

        saveNewPlayer(player, game.getId());
        game = repository.save(game);

        return game;
    }

    public Game connectToRandomGame(Player player) {
        List<Game> gamesToConnect = getGamesToConnect();
        if (gamesToConnect.size() == 0) {
            throw new BusinessException("No games to connect to");
        }
        return connectToGame(player, gamesToConnect.get(random.nextInt(gamesToConnect.size())).getId());
    }

    @Transactional
    public Game connectToGame(Player player, Long gameId) {
        var game = getGameById(gameId);

        if (!game.getState().equals(GameState.CREATED)) {
            throw new BusinessException("Invalid game state");
        }
        if (game.getPlayers().size() > MAX_PLAYERS_AMOUNT) {
            throw new BusinessException("Max players amount reached");
        }

        game.getPlayers().add(player);
        saveNewPlayer(player, game.getId());

        if (game.getPlayers().size() >= game.getMinPlayersAmount()) {
            startGame(game);
        }
        repository.save(game);
        return game;
    }

    private void startGame(Game game) {
        game.getPlayers().forEach(player -> player.setCards(getRandomCards()));

        game.setCurrentQuestion(getRandomQuestion());
        game.setCurrentPlayer(game.getPlayers().get(random.nextInt(game.getPlayers().size())));

        game.setState(GameState.IN_PROGRESS);
    }

    private void saveNewPlayer(Player player, Long gameId) {
        player.setGameId(gameId);
        player.setScore(0);
        player.setCards(getRandomCards());
        playerRepository.save(player);
    }

    private List<Card> getRandomCards() {
        return cardRepository.getRandomCards(STARTING_CARDS_AMOUNT);
    }

    private Question getRandomQuestion() {
        return questionRepository.getRandomQuestion();
    }

    public List<Game> getGamesToConnect() {
        return repository.findAllByState(GameState.CREATED);
    }

    private Game getGameById(Long gameId) {
        return repository.findById(gameId).orElseThrow(() -> new BusinessException("Game not found"));
    }
}
