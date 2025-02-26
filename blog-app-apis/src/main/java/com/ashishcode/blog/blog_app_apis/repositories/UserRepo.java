package com.ashishcode.blog.blog_app_apis.repositories;

import com.ashishcode.blog.blog_app_apis.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Integer> {
		
	
	Optional<User> findByEmail(String email);
}
