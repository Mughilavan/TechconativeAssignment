package com.techconative.demo.controller;

import com.techconative.demo.bo.Like;
import com.techconative.demo.bo.Post;
import com.techconative.demo.bo.User;
import com.techconative.demo.constants.Constant;
import com.techconative.demo.service.PostServiceImpl;
import com.techconative.demo.service.UserServiceImpl;
import org.apache.tomcat.util.bcel.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts/{postId}/likes")
public class LikeController {

    @Autowired
    PostServiceImpl postServiceImpl;

    @Autowired
    UserServiceImpl userService;

    private Logger logger =  LoggerFactory.getLogger(LikeController.class);

    @PutMapping
    public ResponseEntity<String> likePost(@PathVariable Long postId, @RequestBody Like like) {
        logger.info("Post id from like Post request is: {}", postId);
        Post post = postServiceImpl.getPostById(postId);
        if (post == null) {
            return new ResponseEntity<>(Constant.POST_NOT_FOUND + postId, HttpStatus.NOT_FOUND);
        }
        logger.info("user id for like post request is: {}", like.getUserId());
        if(like.getUserId() == null) {
            return new ResponseEntity<>(Constant.USER_NOT_FOUND + like.getUserId(), HttpStatus.NOT_FOUND);
        }
        User user = userService.getUserById(like.getUserId());
        if (user == null) {
            return new ResponseEntity<>(Constant.USER_NOT_FOUND + like.getUserId(), HttpStatus.NOT_FOUND);
        }
        if (post.getLikes().contains(user)) {
            return new ResponseEntity<>("User has already liked the post", HttpStatus.BAD_REQUEST);
        }

        post.getLikes().add(user);
        postServiceImpl.createPost(post);

        return new ResponseEntity<>("Post liked successfully", HttpStatus.OK);
    }
}
