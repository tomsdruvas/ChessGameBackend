package com.lazychess.chessgame.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@ToString
public class RegistrationDto {

    @NotBlank(message = "username is mandatory")
    private String username;

    @NotBlank(message = "Password is mandatory")
    private CharSequence password;

    @NotBlank(message = "Confirm Password is mandatory")
    private CharSequence confirmPassword;


}

