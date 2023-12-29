package com.techconative.demo.controller.validate;

import com.techconative.demo.entity.Post;
import org.springframework.stereotype.Component;

import static com.techconative.demo.constants.Constant.*;

import java.util.Optional;

@Component
public class RequestValidator {

    public Optional<String> validateCreatePostRequest(Post post) {
        if (post.getUser() == null)
            return Optional.of(USER_OBJECT_NOT_FOUND);
        if (post.getUser().getId() == null)
            return Optional.of(USER_ID_NOT_PRESENT);
        return Optional.empty();
    }

}
