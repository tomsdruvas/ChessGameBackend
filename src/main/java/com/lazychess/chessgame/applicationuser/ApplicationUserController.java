package com.lazychess.chessgame.applicationuser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "api/investors")
public class ApplicationUserController {


    private final ApplicationUserService applicationUserService;

    @Autowired
    public ApplicationUserController(ApplicationUserService applicationUserService) {
        this.applicationUserService = applicationUserService;
    }

    @PostMapping()
    public ApplicationUser registerNewUser(@Valid @RequestBody ApplicationUser newApplicationUser) {
        return applicationUserService.save(newApplicationUser);
    }
}
