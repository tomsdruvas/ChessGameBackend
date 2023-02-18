package com.lazychess.chessgame.auth;

import java.io.IOException;
import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lazychess.chessgame.applicationuser.ApplicationUser;
import com.lazychess.chessgame.applicationuser.ApplicationUserService;
import com.lazychess.chessgame.dto.RegistrationDto;
import com.lazychess.chessgame.security.TokenService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Controller
public class AuthController {
    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);

    private final TokenService tokenService;
    private final ApplicationUserService applicationUserService;
    public AuthController(TokenService tokenService, ApplicationUserService applicationUserService) {
        this.tokenService = tokenService;
        this.applicationUserService = applicationUserService;
    }

    @GetMapping("/")
    public String home(Principal principal) {
        return "Hello, " + principal.getName();
    }

    @RequestMapping("/revoke")
    public void revokeJwtToken(HttpServletResponse response) throws IOException {
        response.addCookie(new Cookie("access_token", null));
        response.addCookie(new Cookie("JSESSIONID", null));

        response.sendRedirect("/login");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/secure")
    public String secure() {
        return "This is secured!";
    }

    @PostMapping("/token")
    public ResponseEntity<AccessTokenDto> token(Authentication authentication) {

        LOG.debug("Token requested for user: '{}'", authentication.getName());
        String token = tokenService.generateToken(authentication);
        LOG.debug("Token granted: {}", token);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Authorization","Bearer "+
            token);

        return ResponseEntity.ok()
            .headers(responseHeaders)
            .body(new AccessTokenDto(token));
    }

    @PostMapping("/register/save")
    public String register(@Valid @ModelAttribute("applicationUser") RegistrationDto registrationDto,
                           BindingResult result, Model model) {
        ApplicationUser existingUserUsername = applicationUserService.findByUsername(registrationDto.getUsername());
        if(existingUserUsername != null && existingUserUsername.getUsername() != null && !existingUserUsername.getUsername().isEmpty()) {
            return "redirect:/register?fail";
        }
        if(result.hasErrors()) {
            model.addAttribute("registrationDto", registrationDto);
            return "register";
        }
        if (!registrationDto.getPassword().equals(registrationDto.getConfirmPassword())) {
            return "redirect:/register?passworderror=true";
        }
        applicationUserService.saveUser(registrationDto);
        return "redirect:/login?regsuccess=true";
    }
}
