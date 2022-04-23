package ru.ruiners.cards.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ruiners.cards.controller.dto.dictionary.GameStateHintDto;
import ru.ruiners.cards.dictionary.service.GameStateHintDictionaryService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dictionary")
public class DictionaryController {

    private final GameStateHintDictionaryService service;

    @GetMapping("/game-state-hints")
    public ResponseEntity<GameStateHintDto> gameHints() {
        return ResponseEntity.ok(service.getGameStateHints());
    }

}
