package ru.ruiners.cards.core.model;

import lombok.Data;
import ru.ruiners.cards.core.model.enums.GameState;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
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

    @OneToMany(cascade = CascadeType.ALL)
    private List<Player> players;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GameState state;

    @ManyToOne
    private Question currentQuestion;

    @ManyToOne
    private Player currentPlayer;

    private Integer minPlayersAmount;

    @ManyToOne
    private Player winner;

}
