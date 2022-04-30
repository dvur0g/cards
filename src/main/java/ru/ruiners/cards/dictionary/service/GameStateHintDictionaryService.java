package ru.ruiners.cards.dictionary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ruiners.cards.controller.dto.dictionary.GameStateHintDto;
import ru.ruiners.cards.dictionary.repository.GameStateHintRepository;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GameStateHintDictionaryService {

    private final GameStateHintRepository repository;

    public GameStateHintDto getGameStateHints() {
        Map<String, String> gameStateHints = new HashMap<>();
        repository.findAll().forEach(gameStateHint -> gameStateHints.put(gameStateHint.getGameState(), gameStateHint.getHint()));
        return new GameStateHintDto().setGameStateHintMap(gameStateHints);
    }

}
