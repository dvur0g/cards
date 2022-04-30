package ru.ruiners.cards.suggestion.model;

import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@SQLDelete(sql = "update suggested_answer set deleted = true where id = ?")
@Where(clause = "deleted = false")
public class SuggestedAnswer {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "seq_suggested_answer"
    )
    @SequenceGenerator(
            name = "seq_suggested_answer",
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

    @Override
    public String toString() {
        return "id=" + id + ", " + text;
    }

}
