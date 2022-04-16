package ru.ruiners.cards.core.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.ruiners.cards.core.model.Player;

import java.util.Optional;

@Repository
public interface PlayerRepository extends CrudRepository<Player, Long> {

    Optional<Player> findByUsername(String username);

    @Query(value = "SELECT gp.game_id FROM game_players gp " +
            "JOIN player p ON p.id = gp.players_id " +
            "WHERE p.username = (:username)", nativeQuery = true)
    Optional<Long> getGameIdByUsername(@Param("username") String username);

    @Modifying
    @Query(value = "DELETE FROM player_cards pc " +
            "WHERE pc.player_id = (SELECT id FROM player WHERE username = (:username))", nativeQuery = true)
    void removeCardsByUsername(@Param("username") String username);

}
