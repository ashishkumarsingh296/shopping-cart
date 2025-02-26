//
//
package com.ashishcode.blog.blog_app_apis.controllers;
//
//import java.security.Principal;
//import com.ashishcode.blog.blog_app_apis.entities.User;
//import com.ashishcode.blog.blog_app_apis.exceptions.ApiException;
//import com.ashishcode.blog.blog_app_apis.payloads.JwtAuthRequest;
//import com.ashishcode.blog.blog_app_apis.payloads.JwtAuthResponse;
//import com.ashishcode.blog.blog_app_apis.payloads.UserDto;
//import com.ashishcode.blog.blog_app_apis.repositories.UserRepo;
////import com.ashishcode.blog.blog_app_apis.security.JwtTokenHelper;
//import com.ashishcode.blog.blog_app_apis.security.JwtUtil;
//import com.ashishcode.blog.blog_app_apis.services.UserService;
//import io.swagger.v3.oas.annotations.Operation;
//import jakarta.validation.Valid;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.web.bind.annotation.*;
//
//
//@RestController
//@RequestMapping("/api/v1/auth/")
//public class AuthController {
//
//	@Autowired
//	private JwtUtil jwtTokenHelper;
//
//	@Autowired
//	private UserDetailsService userDetailsService;
//
////	@Autowired
////	private AuthenticationManager authenticationManager;
//
//	@Autowired
//	private UserService userService;
//
//	@PostMapping("/login")
//	@Operation(summary = "Logged in")
//	public ResponseEntity<JwtAuthResponse> createToken(@RequestBody JwtAuthRequest request) throws Exception {
//		this.authenticate(request.getUsername(), request.getPassword());
//		UserDetails userDetails = this.userDetailsService.loadUserByUsername(request.getUsername());
//		String token = this.jwtTokenHelper.generateToken(userDetails.getUsername());
//
//		JwtAuthResponse response = new JwtAuthResponse();
//		response.setToken(token);
//		response.setUser(this.mapper.map((User) userDetails, UserDto.class));
//		return new ResponseEntity<JwtAuthResponse>(response, HttpStatus.OK);
//	}
//
//	private void authenticate(String username, String password) throws Exception {
//
//		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
//				password);
//
//		try {
//
//			this.authenticationManager.authenticate(authenticationToken);
//
//		} catch (BadCredentialsException e) {
//			System.out.println("Invalid Detials !!");
//			throw new ApiException("Invalid username or password !!");
//		}
//
//	}
//
//	// register new user api
//
//	@PostMapping("/register")
//	@Operation(summary = "register in")
//
//	public ResponseEntity<UserDto> registerUser(@Valid @RequestBody UserDto userDto) {
//		UserDto registeredUser = this.userService.registerNewUser(userDto);
//		return new ResponseEntity<UserDto>(registeredUser, HttpStatus.CREATED);
//	}
//
//	// get loggedin user data
//	@Autowired
//	private UserRepo userRepo;
//	@Autowired
//	private ModelMapper mapper;
//
//	@GetMapping("/current-user/")
//	@Operation(summary = "User in")
//
//	public ResponseEntity<UserDto> getUser(Principal principal) {
//		User user = this.userRepo.findByEmail(principal.getName()).get();
//		return new ResponseEntity<UserDto>(this.mapper.map(user, UserDto.class), HttpStatus.OK);
//	}
//
//}




import java.security.Principal;
import java.util.Optional;

import com.ashishcode.blog.blog_app_apis.entities.User;
import com.ashishcode.blog.blog_app_apis.payloads.JwtAuthRequest;
import com.ashishcode.blog.blog_app_apis.payloads.JwtAuthResponse;
import com.ashishcode.blog.blog_app_apis.payloads.UserDto;
import com.ashishcode.blog.blog_app_apis.repositories.UserRepo;
import com.ashishcode.blog.blog_app_apis.security.JwtUtil;
import com.ashishcode.blog.blog_app_apis.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/v1/auth/")
public class AuthController {

	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private JwtUtil jwtTokenHelper;

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private ModelMapper mapper;

	@PostMapping("/login")
	@Operation(summary = "User login (without authentication)")
	public ResponseEntity<JwtAuthResponse> createToken(@RequestBody JwtAuthRequest request) {
		String token = jwtTokenHelper.generateToken(request.getUsername());

		JwtAuthResponse response = new JwtAuthResponse();
		response.setToken(token);

		Optional<User> userOpt = userRepo.findByEmail(request.getUsername());
		if (userOpt.isPresent()) {
			response.setUser(mapper.map(userOpt.get(), UserDto.class));
		}

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// Register new user API
	@PostMapping("/register")
	@Operation(summary = "Register a new user")
	public ResponseEntity<UserDto> registerUser(@Valid @RequestBody UserDto userDto) {
		UserDto registeredUser = userService.registerNewUser(userDto);
		return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
	}

	// Get logged-in user data
	@GetMapping("/current-user")
	@Operation(summary = "Get currently logged-in user")
	public ResponseEntity<UserDto> getUser(Principal principal) {
		Optional<User> userOpt = userRepo.findByEmail(principal.getName());

		if (userOpt.isEmpty()) {
			logger.warn("User not found: {}", principal.getName());
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		UserDto userDto = mapper.map(userOpt.get(), UserDto.class);
		return new ResponseEntity<>(userDto, HttpStatus.OK);
	}
}
