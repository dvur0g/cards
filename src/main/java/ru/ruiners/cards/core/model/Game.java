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

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.ALL)
    private List<Player> players;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GameState state;

    @ManyToOne
    private Card victoriousAnswer;

    @ManyToOne
    private Question currentQuestion;

    private Integer currentPlayerIndex = 0;

    private Integer minPlayersAmount;

    @ManyToOne
    private Player winner;

    public Optional<Player> getWinnerOptional() {
        return getPlayers().stream().filter(Player::isWinner).findAny();
    }

    public Player getCurrentPlayer() {
        if (currentPlayerIndex >= players.size()) {
            currentPlayerIndex = 0;
        }
        if (players.size() == 0) {
            currentPlayerIndex = null;
            return null;
        }
        return players.get(currentPlayerIndex);
    }

}
