package com.techconative.demo.controller;

import com.techconative.demo.entity.Comment;
import com.techconative.demo.entity.Post;
import com.techconative.demo.constants.Constant;
import com.techconative.demo.service.CommentServiceImpl;
import com.techconative.demo.service.PostServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    @Autowired
    private CommentServiceImpl commentService;

    @Autowired
    private PostServiceImpl postService;

    private final Logger logger =  LoggerFactory.getLogger(CommentController.class);

    @PostMapping
    public ResponseEntity<String> addCommentToPost(@PathVariable Long postId, @RequestBody Comment comment) {
        logger.info("Post id value to add comment is: {}",postId);

        boolean validPostId = postService.isValidPostId(postId);
        if(validPostId) {
            Post post = new Post();
            post.setId(postId);
            comment.setPost(post);
            commentService.addComment(comment);
            return new ResponseEntity<>(comment.toString(), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(Constant.POST_NOT_FOUND + postId, HttpStatus.BAD_REQUEST);
    }
}
