package ru.ruiners.cards.suggestion.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.ruiners.cards.common.BusinessException;
import ru.ruiners.cards.controller.dto.game.QuestionDto;
import ru.ruiners.cards.controller.dto.suggestion.SuggestedQuestionDto;
import ru.ruiners.cards.core.mapper.QuestionMapper;
import ru.ruiners.cards.core.model.Question;
import ru.ruiners.cards.core.model.enums.CensorType;
import ru.ruiners.cards.core.repository.QuestionRepository;
import ru.ruiners.cards.security.service.AuthenticationService;
import ru.ruiners.cards.suggestion.mapper.SuggestedQuestionMapper;
import ru.ruiners.cards.suggestion.model.SuggestedQuestion;
import ru.ruiners.cards.suggestion.repository.SuggestedQuestionRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final SuggestedQuestionRepository suggestedQuestionRepository;
    private final SuggestedQuestionMapper suggestedQuestionMapper;

    private final QuestionMapper questionMapper;
    private final QuestionRepository repository;

    private final AuthenticationService authenticationService;

    public List<SuggestedQuestionDto> getSuggestedQuestionList(int page, int count, boolean isDeleted) {
        return suggestedQuestionRepository.findAllByDeletedOrderByDateDesc(isDeleted, PageRequest.of(page, count))
                .stream().map(suggestedQuestionMapper::toDto).collect(Collectors.toList());
    }

    public void suggest(SuggestedQuestionDto dto) {
        SuggestedQuestion suggestedQuestion = suggestedQuestionMapper.toSuggestedQuestion(dto);

        suggestedQuestion.setUsername(authenticationService.getUsername());
        suggestedQuestion.setDate(LocalDateTime.now());
        suggestedQuestion.setDeleted(false);
        suggestedQuestionRepository.save(suggestedQuestion);
    }

    public void approve(long id) {
        SuggestedQuestion suggestedQuestion = suggestedQuestionRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Suggested question not found"));

        add(suggestedQuestionMapper.toQuestion(suggestedQuestion));

        suggestedQuestion.setDeleted(true);
        suggestedQuestionRepository.save(suggestedQuestion);
    }

    public void disapprove(long id) {
        SuggestedQuestion suggestedQuestion = suggestedQuestionRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Suggested question not found"));
        suggestedQuestion.setDeleted(true);
        suggestedQuestionRepository.save(suggestedQuestion);
    }

    public List<QuestionDto> getQuestionList(int page, int count) {
        return repository.findAll(PageRequest.of(page, count))
                .stream().map(questionMapper::toDto).collect(Collectors.toList());
    }

    public void addQuestion(QuestionDto questionDto) {
        add(questionMapper.toQuestion(questionDto));
    }

    public void deleteQuestion(long id) {
        repository.deleteById(id);
    }

    private void add(Question question) {
        question.setDate(LocalDateTime.now());
        if (question.getUsername() == null) {
            question.setUsername(authenticationService.getUsername());
        }
        question.setType(CensorType.OK);
        repository.save(question);
    }

}
