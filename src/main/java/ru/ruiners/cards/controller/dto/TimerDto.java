package ru.ruiners.cards.controller.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TimerDto {

    private Integer countdown;

}
