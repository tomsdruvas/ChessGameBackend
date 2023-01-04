package com.lazychess.chessgame.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Data
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RegistrationDto {

    @NonNull
    @NotBlank(message = "username is mandatory")
    private String username;

    @NonNull
    @NotBlank(message = "New password is mandatory")
    private String password;

    @NonNull
    @NotBlank(message = "Confirm Password is mandatory")
    private String confirmPassword;
}

