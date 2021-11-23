package ru.ruiners.cards.core.model;

import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@SQLDelete(sql = "UPDATE player SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
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

    private String password;

    private Integer score;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Card> cards;

    private boolean deleted = false;

}
