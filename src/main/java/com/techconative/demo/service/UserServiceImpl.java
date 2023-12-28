package com.techconative.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techconative.demo.bo.User;
import com.techconative.demo.repository.UserRepository;

@Service
public class UserServiceImpl {

	@Autowired
	private UserRepository userRepo;

    public User registerAndSaveUser(User user) {
    	return userRepo.save(user);
    }

    public User getUserById(Long id) {
    	Optional<User> userOptional = userRepo.findById(id);
    	return userOptional.orElse(null);
    }

	public boolean isValidUserId(Long userId) {
		return userRepo.findById(userId).isPresent();
	}

}
