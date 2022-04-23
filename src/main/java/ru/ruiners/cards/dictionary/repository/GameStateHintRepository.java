package ru.ruiners.cards.dictionary.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.ruiners.cards.dictionary.model.GameStateHint;

@Repository
public interface GameStateHintRepository extends CrudRepository<GameStateHint, Long> {

}
