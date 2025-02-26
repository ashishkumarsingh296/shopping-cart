package com.ashishcode.blog.blog_app_apis;

import com.ashishcode.blog.blog_app_apis.config.AppConstants;
import com.ashishcode.blog.blog_app_apis.entities.Role;
import com.ashishcode.blog.blog_app_apis.repositories.RoleRepo;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Blocki API",
				version = "1.0",
				description = "API for user blocking system"
		)
)
public class BlogAppApisApplication implements CommandLineRunner {

//	private final PasswordEncoder passwordEncoder;
	private final RoleRepo roleRepo;

	@Autowired
	public BlogAppApisApplication(RoleRepo roleRepo) {
//		this.passwordEncoder = passwordEncoder;
		this.roleRepo = roleRepo;
	}

	public static void main(String[] args) {
		SpringApplication.run(BlogAppApisApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Override
	public void run(String... args) {
//		System.out.println("Encoded password: " + this.passwordEncoder.encode("xyz"));

		try {
			Role roleAdmin = new Role();
			roleAdmin.setId(AppConstants.ADMIN_USER);
			roleAdmin.setName("ROLE_ADMIN");

			Role roleUser = new Role();
			roleUser.setId(AppConstants.NORMAL_USER);
			roleUser.setName("ROLE_NORMAL");

			List<Role> roles = Arrays.asList(roleAdmin, roleUser); // ✅ Ensures compatibility with Java 8+

			List<Role> savedRoles = this.roleRepo.saveAll(roles);

			savedRoles.forEach(role -> System.out.println("Saved Role: " + role.getName()));

		} catch (Exception e) {
			e.printStackTrace(); // ✅ Logs the exception properly
		}
	}
}
