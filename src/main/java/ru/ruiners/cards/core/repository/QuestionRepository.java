package ru.ruiners.cards.core.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.ruiners.cards.core.model.Question;

public interface QuestionRepository extends CrudRepository<Question, Long> {

    @Query(value = "SELECT * FROM question ORDER BY random() LIMIT 1", nativeQuery = true)
    Question getRandomQuestion();
}
