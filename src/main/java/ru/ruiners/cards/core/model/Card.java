package ru.ruiners.cards.core.model;

import lombok.Data;
import ru.ruiners.cards.core.model.enums.CensorType;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CensorType type = CensorType.OK;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private String username;

    @Override
    public String toString() {
        return "id=" + id + ", " + text;
    }

}
