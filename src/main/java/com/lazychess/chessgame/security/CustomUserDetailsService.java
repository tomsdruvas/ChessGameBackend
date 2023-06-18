package com.lazychess.chessgame.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.lazychess.chessgame.repository.entity.ApplicationUser;
import com.lazychess.chessgame.repository.ApplicationUserRepository;

import jakarta.annotation.PostConstruct;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final WebApplicationContext applicationContext;

    @Autowired
    public CustomUserDetailsService(WebApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    private ApplicationUserRepository applicationUserRepository;

    @PostConstruct
    public void completeSetup() {
        applicationUserRepository = applicationContext.getBean(ApplicationUserRepository.class);
    }

    @Override
    public AppUserPrincipal loadUserByUsername(final String username) {
        final ApplicationUser appUser = applicationUserRepository.findByUsername(username);
        if (appUser == null) {
            throw new UsernameNotFoundException(username);
        }
        return new AppUserPrincipal(appUser);
    }
}
