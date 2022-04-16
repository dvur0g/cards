package ru.ruiners.cards.core.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.ruiners.cards.core.model.Question;

import java.util.List;

@Repository
public interface QuestionRepository extends CrudRepository<Question, Long> {

    @Query(value = "SELECT * FROM question ORDER BY random() LIMIT 1", nativeQuery = true)
    Question getRandomQuestion();

    List<Question> findAll(Pageable pageable);

}
