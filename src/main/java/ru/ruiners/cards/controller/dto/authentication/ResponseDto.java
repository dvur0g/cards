package ru.ruiners.cards.controller.dto.authentication;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ResponseDto {

    public static final String SUCCESS = "Success";

    private String message;

}
