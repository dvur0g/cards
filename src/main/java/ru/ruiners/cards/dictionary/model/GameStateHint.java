package ru.ruiners.cards.dictionary.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class GameStateHint {

    @Id
    private String gameState;

    private String hint;

}
