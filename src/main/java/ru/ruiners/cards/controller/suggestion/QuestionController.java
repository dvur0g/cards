package ru.ruiners.cards.controller.suggestion;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.ruiners.cards.controller.dto.authentication.ResponseDto;
import ru.ruiners.cards.controller.dto.game.QuestionDto;
import ru.ruiners.cards.controller.dto.suggestion.SuggestedQuestionDto;
import ru.ruiners.cards.suggestion.service.QuestionService;

import java.util.List;

import static ru.ruiners.cards.controller.dto.authentication.ResponseDto.SUCCESS;

@RestController
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping("/suggested/list")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SuggestedQuestionDto>> getSuggestedQuestionList(@RequestParam int page,
                                                                               @RequestParam int count) {
        return ResponseEntity.ok(questionService.getSuggestedQuestionList(page, count));
    }

    @PostMapping("/suggest/approve/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void approve(@PathVariable("id") long questionId) {
        questionService.approve(questionId);
    }

    @PostMapping("/suggest/disapprove/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void disapprove(@PathVariable("id") long questionId) {
        questionService.disapprove(questionId);
    }

    @PostMapping("/suggest")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ResponseDto> suggest(@RequestBody SuggestedQuestionDto suggestedQuestion) {
        questionService.suggest(suggestedQuestion);
        return ResponseEntity.ok(new ResponseDto().setMessage(SUCCESS));
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<QuestionDto>> getQuestionList(@RequestParam int page, @RequestParam int count) {
        return ResponseEntity.ok(questionService.getQuestionList(page, count));
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseDto> addQuestion(@RequestBody QuestionDto question) {
        questionService.addQuestion(question);
        return ResponseEntity.ok(new ResponseDto().setMessage(SUCCESS));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDto> deleteQuestion(@PathVariable("id") long questionId) {
        questionService.deleteQuestion(questionId);
        return ResponseEntity.ok(new ResponseDto().setMessage(SUCCESS));
    }

}
