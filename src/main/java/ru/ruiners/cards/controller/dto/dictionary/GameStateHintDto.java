package ru.ruiners.cards.controller.dto.dictionary;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Accessors(chain = true)
public class GameStateHintDto {

    private Map<String, String> gameStateHintMap;

}
