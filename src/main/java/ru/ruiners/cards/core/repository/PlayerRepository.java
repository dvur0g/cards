package ru.ruiners.cards.core.repository;

import org.springframework.data.repository.CrudRepository;
import ru.ruiners.cards.core.model.Player;

public interface PlayerRepository extends CrudRepository<Player, Long> {

    boolean existsByUsername(String username);

    void deleteByUsername(String username);

}
