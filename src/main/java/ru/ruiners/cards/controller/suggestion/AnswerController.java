package ru.ruiners.cards.controller.suggestion;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.ruiners.cards.controller.dto.authentication.ResponseDto;
import ru.ruiners.cards.controller.dto.game.CardDto;
import ru.ruiners.cards.controller.dto.suggestion.SuggestedAnswerDto;
import ru.ruiners.cards.suggestion.service.AnswerService;

import java.util.List;

import static ru.ruiners.cards.controller.dto.authentication.ResponseDto.SUCCESS;

@RestController
@RequestMapping("/answer")
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;

    @GetMapping("/suggested/list")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SuggestedAnswerDto>> getSuggestedAnswerList(@RequestParam int page,
                                                                               @RequestParam int count) {
        return ResponseEntity.ok(answerService.getSuggestedAnswerList(page, count));
    }

    @PostMapping("/suggest/approve/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void approve(@PathVariable("id") long answerId) {
        answerService.approve(answerId);
    }

    @PostMapping("/suggest/disapprove/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void disapprove(@PathVariable("id") long answerId) {
        answerService.disapprove(answerId);
    }

    @PostMapping("/suggest")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ResponseDto> suggest(@RequestBody SuggestedAnswerDto suggestedAnswer) {
        answerService.suggest(suggestedAnswer);
        return ResponseEntity.ok(new ResponseDto().setMessage(SUCCESS));
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CardDto>> getAnswerList(@RequestParam int page, @RequestParam int count) {
        return ResponseEntity.ok(answerService.getAnswerList(page, count));
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseDto> addAnswer(@RequestBody CardDto cardDto) {
        answerService.addAnswer(cardDto);
        return ResponseEntity.ok(new ResponseDto().setMessage(SUCCESS));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDto> deleteAnswer(@PathVariable("id") long answerId) {
        answerService.deleteAnswer(answerId);
        return ResponseEntity.ok(new ResponseDto().setMessage(SUCCESS));
    }
    
}
