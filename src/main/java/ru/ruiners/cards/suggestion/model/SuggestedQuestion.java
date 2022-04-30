package ru.ruiners.cards.suggestion.model;

import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@SQLDelete(sql = "update suggested_question set deleted = true where id = ?")
@Where(clause = "deleted = false")
public class SuggestedQuestion {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "seq_suggested_question"
    )
    @SequenceGenerator(
            name = "seq_suggested_question",
            allocationSize = 1
    )
    private Long id;

    @Column(nullable = false, unique = true)
    private String text;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private Boolean deleted;

}
