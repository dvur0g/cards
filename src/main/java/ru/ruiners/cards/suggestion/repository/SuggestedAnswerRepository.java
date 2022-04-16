package ru.ruiners.cards.suggestion.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.ruiners.cards.suggestion.model.SuggestedAnswer;
import ru.ruiners.cards.suggestion.model.SuggestedQuestion;

import java.util.List;

@Repository
public interface SuggestedAnswerRepository extends CrudRepository<SuggestedAnswer, Long> {

    List<SuggestedQuestion> findAllByDeletedOrderByDateDesc(boolean isDeleted, Pageable pageable);

}
