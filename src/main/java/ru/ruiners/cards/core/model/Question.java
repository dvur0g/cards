package ru.ruiners.cards.core.model;

import ru.ruiners.cards.core.model.enums.CensorType;

import javax.persistence.*;

@Entity
public class Question {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "seq_question"
    )
    @SequenceGenerator(
            name = "seq_question",
            allocationSize = 1
    )
    private Long id;
    private String text;
    private CensorType type;
}
