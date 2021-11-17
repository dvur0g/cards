package ru.ruiners.cards.core.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Player {

    @Id
    private Long id;
    private String username;
    private Integer score;

    @ManyToMany
    private List<Card> cards;
}
