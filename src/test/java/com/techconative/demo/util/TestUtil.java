package com.techconative.demo.util;

import com.techconative.demo.entity.User;

public class TestUtil {

    public static User getUserObject(String userName, String email, String password, String mobileNumber, String aadharNumber) {
        User user = new User();

        user.setUserName(userName);
        user.setEmail(email);
        user.setPassword(password);
        user.setMobileNumber(mobileNumber);
        user.setAadharNumber(aadharNumber);
        return user;
    }
}
