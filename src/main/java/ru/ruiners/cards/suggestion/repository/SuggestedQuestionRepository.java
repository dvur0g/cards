package ru.ruiners.cards.suggestion.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.ruiners.cards.suggestion.model.SuggestedQuestion;

import java.util.List;

@Repository
public interface SuggestedQuestionRepository extends CrudRepository<SuggestedQuestion, Long> {

    List<SuggestedQuestion> findAllByOrderByDateDesc(Pageable pageable);

}
