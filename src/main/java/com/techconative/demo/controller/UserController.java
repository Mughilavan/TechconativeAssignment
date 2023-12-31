package com.techconative.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techconative.demo.entity.User;
import com.techconative.demo.service.UserServiceImpl;
import jakarta.validation.Valid;

import static com.techconative.demo.constants.Constant.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserServiceImpl userServiceImpl;
	private final Logger logger =  LoggerFactory.getLogger(UserController.class);

	/*
	 registerUser API validates and registered the User.
	 */
	@PostMapping
	public ResponseEntity<String> registerUser(@Valid @RequestBody User user) {
		logger.info("User input for registerUser API is: {}", user);
		User registeredUser = userServiceImpl.registerAndSaveUser(user);
		logger.info("Saved register user value is:  {}", registeredUser);
		return new ResponseEntity<>(registeredUser.toString(), HttpStatus.CREATED);
	}

	/*
	 getUserProfile API fetch the user detail from db and returns.
	 If given userId is not found, it returns 404 as status code.
	 */
	@GetMapping("/{userId}")
	public ResponseEntity<String> getUserProfile(@PathVariable Long userId) {
		logger.info("User Id value in getUserProfile API is: {}", userId);
		User user = userServiceImpl.getUserById(userId);
		if (user != null) {
			return new ResponseEntity<>(user.toString(), HttpStatus.OK);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(USER_NOT_FOUND + userId);
	}
	
	
}
