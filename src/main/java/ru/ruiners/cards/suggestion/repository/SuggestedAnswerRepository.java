package ru.ruiners.cards.suggestion.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.ruiners.cards.suggestion.model.SuggestedAnswer;

import java.util.List;

@Repository
public interface SuggestedAnswerRepository extends CrudRepository<SuggestedAnswer, Long> {

    List<SuggestedAnswer> findAllByOrderByDateDesc(Pageable pageable);

}
