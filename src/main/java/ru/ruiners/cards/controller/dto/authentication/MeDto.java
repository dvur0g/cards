package ru.ruiners.cards.controller.dto.authentication;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

@Data
@Accessors(chain = true)
public class MeDto {

    private String username;
    private Set<String> roles;

}
