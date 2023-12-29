package com.techconative.demo.controller;

import com.techconative.demo.entity.Comment;
import com.techconative.demo.entity.Post;
import com.techconative.demo.service.CommentServiceImpl;
import com.techconative.demo.service.PostServiceImpl;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.techconative.demo.constants.Constant.*;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    @Autowired
    private CommentServiceImpl commentService;

    @Autowired
    private PostServiceImpl postService;

    private final Logger logger =  LoggerFactory.getLogger(CommentController.class);

    /*
      This API adds comment for the given PostID. If PostID is invalid then returns 400 as status code
     */
    @PostMapping
    public ResponseEntity<String> addCommentToPost(@PathVariable Long postId, @Valid @RequestBody Comment comment) {
        logger.info("Post id value in addCommentToPost API is: {}",postId);

        boolean validPostId = postService.isValidPostId(postId);
        if(validPostId) {
            Post post = new Post();
            post.setId(postId);
            comment.setPost(post);
            Comment postedComment = commentService.addComment(comment);
            return new ResponseEntity<>(postedComment.toString(), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(POST_NOT_FOUND + postId, HttpStatus.BAD_REQUEST);
    }
}
