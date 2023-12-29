package com.techconative.demo.service;

import java.util.Optional;

import com.techconative.demo.serviceinterface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techconative.demo.entity.User;
import com.techconative.demo.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

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
