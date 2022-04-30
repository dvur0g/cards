package ru.ruiners.cards.core.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Accessors(chain = true)
public class Player {

    private static final int MAX_SCORE = 10;

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "seq_player"
    )
    @SequenceGenerator(
            name = "seq_player",
            allocationSize = 1
    )
    private Long id;

    @Column(unique = true)
    private String username;

    private Integer score;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Card> cards;

    @ManyToOne
    private Card selectedAnswer;

    public void incrementScore() {
        ++score;
    }

    public void removeSelectedAnswer() {
        selectedAnswer = null;
    }

    public boolean isWinner() {
        return score == MAX_SCORE;
    }

    public boolean didSelectAnswer() {
        return selectedAnswer != null;
    }

}
