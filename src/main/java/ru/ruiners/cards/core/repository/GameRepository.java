package ru.ruiners.cards.core.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.ruiners.cards.core.model.Game;
import ru.ruiners.cards.core.model.enums.GameState;

import java.util.List;
import java.util.Set;

@Repository
public interface GameRepository extends CrudRepository<Game, Long> {

    List<Game> findAllByStateIn(Set<GameState> state);

}
