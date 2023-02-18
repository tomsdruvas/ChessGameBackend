package com.lazychess.chessgame;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.lazychess.chessgame.applicationuser.ApplicationUser;
import com.lazychess.chessgame.applicationuser.ApplicationUserRepository;
import com.lazychess.chessgame.models.Role;
import com.lazychess.chessgame.security.RsaKeyProperties;

@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication
public class ChessGameApplication {

	@Autowired
	private ApplicationUserRepository applicationUserRepository;

	public static void main(String[] args) {
		SpringApplication.run(ChessGameApplication.class, args);
	}

	@Profile("!test")
	@Bean
	public CommandLineRunner loadDataForUsers() {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		List<Role> allRoles = List.of(new Role("ROLE_ADMIN"));
//		ApplicationUser applicationUser = new ApplicationUser("admin", passwordEncoder.encode("admin"), allRoles);
		ApplicationUser applicationUser2 = new ApplicationUser("admin2", passwordEncoder.encode("admin2"), allRoles);

		return args -> applicationUserRepository.saveAllAndFlush((List.of(applicationUser2)));
	}
}
