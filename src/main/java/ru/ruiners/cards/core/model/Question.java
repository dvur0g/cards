package ru.ruiners.cards.core.model;

import ru.ruiners.cards.core.model.enums.CensorType;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Question {

    @Id
    private Long id;
    private String text;
    private CensorType type;
}
