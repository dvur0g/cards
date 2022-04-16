package ru.ruiners.cards.core.model;

import lombok.Data;
import ru.ruiners.cards.core.model.enums.CensorType;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CensorType type = CensorType.OK;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private String username;

}
