package ru.ruiners.cards.suggestion.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.ruiners.cards.common.BusinessException;
import ru.ruiners.cards.controller.dto.game.CardDto;
import ru.ruiners.cards.controller.dto.suggestion.SuggestedAnswerDto;
import ru.ruiners.cards.core.mapper.CardMapper;
import ru.ruiners.cards.core.model.Card;
import ru.ruiners.cards.core.repository.CardRepository;
import ru.ruiners.cards.service.AuthenticationService;
import ru.ruiners.cards.suggestion.mapper.SuggestedAnswerMapper;
import ru.ruiners.cards.suggestion.model.SuggestedAnswer;
import ru.ruiners.cards.suggestion.repository.SuggestedAnswerRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final SuggestedAnswerRepository suggestedAnswerRepository;
    private final SuggestedAnswerMapper suggestedAnswerMapper;

    private final CardMapper cardMapper;
    private final CardRepository repository;

    private final AuthenticationService authenticationService;

    public List<SuggestedAnswerDto> getSuggestedAnswerList(int page, int count, boolean isDeleted) {
        return suggestedAnswerRepository.findAllByDeletedOrderByDateDesc(isDeleted, PageRequest.of(page, count))
                .stream().map(suggestedAnswerMapper::toDto).collect(Collectors.toList());
    }

    public void suggest(SuggestedAnswerDto dto) {
        SuggestedAnswer suggestedAnswer = suggestedAnswerMapper.toSuggestedAnswer(dto);

        suggestedAnswer.setUsername(authenticationService.getUsername());
        suggestedAnswer.setDate(LocalDateTime.now());
        suggestedAnswerRepository.save(suggestedAnswer);
    }

    public void approve(long id) {
        SuggestedAnswer suggestedAnswer = suggestedAnswerRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Suggested Answer not found"));

        Card card = suggestedAnswerMapper.toCard(suggestedAnswer);
        repository.save(card);

        suggestedAnswer.setDeleted(true);
        suggestedAnswerRepository.save(suggestedAnswer);
    }

    public void disapprove(long id) {
        SuggestedAnswer suggestedAnswer = suggestedAnswerRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Suggested Answer not found"));
        suggestedAnswer.setDeleted(true);
        suggestedAnswerRepository.save(suggestedAnswer);
    }

    public List<CardDto> getAnswerList(int page, int count) {
        return repository.findAll(PageRequest.of(page, count))
                .stream().map(cardMapper::toDto).collect(Collectors.toList());
    }

    public void addAnswer(CardDto cardDto) {
        Card card = cardMapper.toCard(cardDto);
        card.setDate(LocalDateTime.now());
        card.setUsername(authenticationService.getUsername());
        repository.save(card);
    }

    public void deleteAnswer(long id) {
        repository.deleteById(id);
    }
    
}
