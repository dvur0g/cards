package ru.ruiners.cards.core.model;

import ru.ruiners.cards.core.model.enums.CensorType;

import javax.persistence.*;

@Entity
public class Card {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "seq_card"
    )
    @SequenceGenerator(
            name = "seq_card",
            allocationSize = 1
    )
    private Long id;

    private String text;

    private CensorType type = CensorType.OK;
}
