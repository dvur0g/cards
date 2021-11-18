package ru.ruiners.cards.core.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Player {

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

    private Long gameId;

    @ManyToMany
    private List<Card> cards;
}
