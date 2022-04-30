package ru.ruiners.cards.core.model;

import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import ru.ruiners.cards.core.model.enums.GameState;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;

@Entity
@Data
public class Game {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "seq_game"
    )
    @SequenceGenerator(
            name = "seq_game",
            allocationSize = 1
    )
    private Long id;

    private String name;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.ALL)
    @OrderBy("id")
    private List<Player> players;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GameState state;

    @ManyToOne
    private Card victoriousAnswer;

    @ManyToOne
    private Question currentQuestion;

    private int round = 0;

    private Integer currentPlayerIndex = 0;

    @OneToOne
    private Player currentPlayer;

    @OneToOne
    private Player victoriousPlayer;

    private Integer minPlayersAmount;

    @ManyToOne
    private Player winner;

    public Optional<Player> getWinnerOptional() {
        return getPlayers().stream().filter(Player::isWinner).findAny();
    }

    public void setNextCurrentPlayer() {
        if (players.size() == 0) {
            currentPlayerIndex = 0;
            currentPlayer = null;
        }
        currentPlayerIndex = (++currentPlayerIndex) % players.size();
        currentPlayer = players.get(currentPlayerIndex);
    }

    public void incrementRound() {
        ++round;
    }

}
