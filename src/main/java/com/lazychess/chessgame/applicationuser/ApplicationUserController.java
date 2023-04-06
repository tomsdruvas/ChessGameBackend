package com.lazychess.chessgame.applicationuser;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.lazychess.chessgame.dto.RegistrationDto;
import com.lazychess.chessgame.dto.RegistrationResponseDto;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/")
public class ApplicationUserController {

    private final ApplicationUserService applicationUserService;

    public ApplicationUserController(ApplicationUserService applicationUserService) {
        this.applicationUserService = applicationUserService;
    }

    @PostMapping(path = "registration")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public RegistrationResponseDto registerNewUser(@Valid @RequestBody RegistrationDto registrationDto) {
        return applicationUserService.saveUser(registrationDto);
    }
}
