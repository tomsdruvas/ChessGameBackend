package com.lazychess.chessgame.auth;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.lazychess.chessgame.dto.RegistrationDto;


@Controller
public class AuthTemplate {

    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        RegistrationDto applicationUser = new RegistrationDto();
        model.addAttribute("applicationUser", applicationUser);
        return "register";
    }
}
