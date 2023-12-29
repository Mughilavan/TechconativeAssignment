package com.techconative.demo.serviceinterface;

import com.techconative.demo.entity.User;

public interface UserService {

    User registerAndSaveUser(User user);
    User getUserById(Long id);
    boolean isValidUserId(Long userId);
}
