package ru.ruiners.cards.controller.dto.authentication;

import lombok.Data;

@Data
public class RegistrationDto {

    private String name;
    private String surname;
    private String email;
    private String username;
    private String password;
    private String repeatedPassword;

}
