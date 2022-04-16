package ru.ruiners.cards.core.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.ruiners.cards.core.model.Card;

import java.util.List;

@Repository
public interface CardRepository extends CrudRepository <Card, Long> {

    @Query(value = "SELECT * FROM card ORDER BY random() LIMIT (:amount)", nativeQuery = true)
    List<Card> getRandomCards(@Param("amount") int amount);

}
