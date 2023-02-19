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


    public List<ApplicationUser> getAll() {
        return applicationUserRepository.findAll();
    }

    public ApplicationUser getById(long userId) throws EntityNotFoundException {

        boolean exists = applicationUserRepository.existsById(userId);
        if (!exists) {
            throw new EntityNotFoundException("User with " + userId + " doesn't exist");
        }

        return applicationUserRepository.findUserById(userId);
    }

    public ApplicationUser save(ApplicationUser newApplicationUser) {
        return applicationUserRepository.save(newApplicationUser);
    }

    public void removeUserByID(Long userId) throws EntityNotFoundException {
    boolean exists = applicationUserRepository.existsById(userId);
    if (!exists) {
        throw new EntityNotFoundException("User with " + userId + " doesn't exist");
    }
    applicationUserRepository.deleteById(userId);
    }


    public ApplicationUser updateById(long id, ApplicationUser updatedApplicationUserDetails) {
        boolean exists = applicationUserRepository.existsById(id);
        if (!exists) {
            throw new EntityNotFoundException("User with " + id + " doesn't exist");
        }
        ApplicationUser applicationUser = applicationUserRepository.findUserById(id);

        applicationUser.setUsername(updatedApplicationUserDetails.getUsername());

        return applicationUserRepository.save(applicationUser);
    }

    public ApplicationUser findByUsername(String username) {
        return applicationUserRepository.findByUsername(username);
    }

    public void saveUser(RegistrationDto registrationDto) {
        List<Role> allRoles = List.of(new Role("ROLE_ADMIN"));
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        ApplicationUser user = new ApplicationUser(registrationDto.getUsername(), passwordEncoder.encode(registrationDto.getPassword()), allRoles);
        applicationUserRepository.save(user);
    }
}
