package com.techconative.demo.controller;


import com.techconative.demo.entity.Post;
import com.techconative.demo.constants.Constant;
import com.techconative.demo.service.PostServiceImpl;
import com.techconative.demo.service.UserServiceImpl;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    PostServiceImpl postServiceImpl;

    @Autowired
    UserServiceImpl userServiceImpl;

    private final Logger logger =  LoggerFactory.getLogger(PostController.class);

    /*
    createPost API creates the post and persist in the db.
    If the given userId is not present in the db, it will return status as BAD_REQUEST.
     */
    @PostMapping
    public ResponseEntity<String> createPost(@Valid @RequestBody Post post) {
        if (post.getUser() == null) {
            return new ResponseEntity<>(Constant.USER_OBJECT_NOT_FOUND, HttpStatus.BAD_REQUEST);
        }
        if(post.getUser().getId() == null){
            return new ResponseEntity<>(Constant.USER_ID_NOT_PRESENT, HttpStatus.BAD_REQUEST);
        }
        Long userId = post.getUser().getId();
        logger.info("Posted by the user Id: {} ",userId);
        if(userServiceImpl.isValidUserId(userId)) {
            logger.info("Created post content is: {}", post);
            postServiceImpl.createPost(post);
            return new ResponseEntity<>(post.toString(), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(Constant.USER_NOT_FOUND + userId, HttpStatus.BAD_REQUEST);
    }

    /*
     getAllPosts API retrieves all the post based on the given page number and page size.
     */
    @GetMapping
    public ResponseEntity<Page<Post>> getAllPosts(Pageable pageable) {
        logger.info("page number is: {} and  page size is: {}", pageable.getPageNumber(), pageable.getPageSize());
        Page<Post> posts = postServiceImpl.getAllPosts(pageable);
        return  new ResponseEntity<>(posts, HttpStatus.OK);
    }

    /*
    getPostByPostId API fetch and returns post based on postId.
    If given postId is not exists, It will return 404 as status code.
     */
    @GetMapping("/{postId}")
    public ResponseEntity<String> getPostByPostId(@PathVariable Long postId) {
        logger.info("Post id value for retrieval is: {}",postId);
        Post post = postServiceImpl.getPostById(postId);
        if (post != null) {
            return new ResponseEntity<>(post.toString(), HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constant.POST_NOT_FOUND + postId);
    }

    /*
    updatePost API fetch and updates the post based on postId.
    If the given postId is not exists, it will return 404 as status code.
     */
    @PutMapping("/{postId}")
    public ResponseEntity<String> updatePost(@PathVariable Long postId, @RequestBody Post updatedPost) {
        logger.info("Post id value for updation is: {}",postId);
        Post post = postServiceImpl.updatePost(postId, updatedPost);
        if (post != null) {
            return new ResponseEntity<>(post.toString(), HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constant.POST_NOT_FOUND + postId);
    }

    /*
    deletePost API deletes the post based on postId.
    If Given postId is not exists, It will return 404 as status code.
     */
    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long postId) {
        logger.info("Post Id value for deletion is: {}",postId);
        boolean deleted = postServiceImpl.deletePost(postId);

        if (deleted) {
            return new ResponseEntity<>(Constant.POST_DELETED + postId, HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constant.POST_NOT_FOUND + postId);
    }
}
