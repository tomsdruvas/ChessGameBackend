package com.lazychess.chessgame.dto;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@ToString
public class RegistrationDto {

    @Size(min = 3, message = "Username needs to be minimum 3 characters")
    private String username;

    @NotBlank(message = "Password is mandatory")
    @Length(min = 5, message = "Password needs to be minimum 5 characters")
    private CharSequence password;

    @NotBlank(message = "Confirm Password is mandatory")
    private CharSequence confirmPassword;
}