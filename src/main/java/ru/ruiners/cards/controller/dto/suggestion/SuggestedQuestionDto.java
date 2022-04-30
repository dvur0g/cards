package ru.ruiners.cards.controller.dto.suggestion;

import lombok.Data;

import java.util.Date;

@Data
public class SuggestedQuestionDto {

    private Long id;
    private String text;
    private Date date;
    private String username;

}
