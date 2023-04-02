package com.lazychess.chessgame.applicationuser;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.lazychess.chessgame.dto.RegistrationDto;
import com.lazychess.chessgame.models.Role;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ApplicationUserService {


    private final ApplicationUserRepository applicationUserRepository;

    @Autowired
    public ApplicationUserService(ApplicationUserRepository applicationUserRepository) {
        this.applicationUserRepository = applicationUserRepository;
    }

    public ApplicationUser save(ApplicationUser newApplicationUser) {
        return applicationUserRepository.save(newApplicationUser);
    }

    public void saveUser(RegistrationDto registrationDto) {
        List<Role> allRoles = List.of(new Role("ROLE_ADMIN"));
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        ApplicationUser user = new ApplicationUser(registrationDto.getUsername(), passwordEncoder.encode(registrationDto.getPassword()), allRoles);
        applicationUserRepository.save(user);
    }
}
