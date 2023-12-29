package com.techconative.demo.controller;

import com.techconative.demo.entity.Like;
import com.techconative.demo.entity.Post;
import com.techconative.demo.entity.User;
import com.techconative.demo.service.PostServiceImpl;
import com.techconative.demo.service.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.techconative.demo.constants.Constant.*;

@RestController
@RequestMapping("/api/posts/{postId}/likes")
public class LikeController {

    @Autowired
    PostServiceImpl postServiceImpl;

    @Autowired
    UserServiceImpl userService;

    private final Logger logger =  LoggerFactory.getLogger(LikeController.class);

    /*
    This API likes the post. If postId is invalid or user id is invalid then it return 400 as status code.
     */
    @PutMapping
    public ResponseEntity<String> likePost(@PathVariable Long postId, @RequestBody Like like) {
        logger.info("Post id value in  likePost API request is: {}", postId);
        Post post = postServiceImpl.getPostById(postId);
        if (post == null) {
            return new ResponseEntity<>(POST_NOT_FOUND + postId, HttpStatus.NOT_FOUND);
        }
        logger.info("user id for like post request is: {}", like.getUserId());
        if(like.getUserId() == null) {
            return new ResponseEntity<>(USER_NOT_FOUND + like.getUserId(), HttpStatus.NOT_FOUND);
        }
        User user = userService.getUserById(like.getUserId());
        if (user == null) {
            return new ResponseEntity<>(USER_NOT_FOUND + like.getUserId(), HttpStatus.NOT_FOUND);
        }
        if (post.getLikes().contains(user)) {
            return new ResponseEntity<>(ALREADY_LIKED, HttpStatus.BAD_REQUEST);
        }

        post.getLikes().add(user);
        postServiceImpl.createPost(post);

        return new ResponseEntity<>("Post liked successfully by userId: " + user.getId() + " for the postId: " + postId, HttpStatus.OK);
    }
}
