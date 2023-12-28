package com.techconative.demo.controller;

import com.techconative.demo.bo.Comment;
import com.techconative.demo.bo.Post;
import com.techconative.demo.bo.User;
import com.techconative.demo.constants.Constant;
import com.techconative.demo.repository.CommentRepository;
import com.techconative.demo.repository.PostRepository;
import com.techconative.demo.repository.UserRepository;
import com.techconative.demo.util.TestUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentControllerTest {


    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @LocalServerPort
    private int port;

    private Logger logger =  LoggerFactory.getLogger(CommentControllerTest.class);

    @AfterEach
    public void tearDown() {
        commentRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @BeforeEach
    public void setUp() {
        commentRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testAddCommentToPostSuccessCase() {
        User user = TestUtil.getUserObject("Mughilavan1", "mughilavanceg1@gmail.com", "teststa", "1234567291", "917456321025");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<User> requestEntity = new HttpEntity<>(user, headers);
        restTemplate.postForEntity("http://localhost:" + port + "/socialmediaapp/api/users", requestEntity, String.class);

        Long userId = userRepository.findLatestUserId();
        logger.info("latest user id is: {}", userId);
        user = new User();
        user.setId(userId);
        Post post = new Post();
        post.setContent("Sample post");
        post.setUser(user);
        headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<Post> requestEntityForCreatePost = new HttpEntity<>(post, headers);
        restTemplate.postForEntity("http://localhost:" + port + "/socialmediaapp/api/posts", requestEntityForCreatePost, String.class);

        Long latestPostId = postRepository.findLatestPostId();
        logger.info("Latest post id is: {}",latestPostId);

        String addCommentUrl = "http://localhost:" + port + "/socialmediaapp/api/posts/" + latestPostId + "/comments";
        headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Comment comment = new Comment();
        comment.setContent("Added comment to Post");
        HttpEntity<Comment> requestEntityForAddComment = new HttpEntity<>(comment, headers);
        ResponseEntity<String> responseEntityForAddComment = restTemplate.exchange(addCommentUrl, HttpMethod.POST, requestEntityForAddComment, String.class);
        Assertions.assertEquals(HttpStatus.CREATED, responseEntityForAddComment.getStatusCode());
        Assertions.assertTrue(responseEntityForAddComment.getBody().contains("Added comment to Post"));
    }

    @Test
    void testAddCommentToPostFailureCase() {
        String addCommentUrl = "http://localhost:" + port + "/socialmediaapp/api/posts/" + Long.MAX_VALUE + "/comments";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Comment comment = new Comment();
        comment.setContent("Added comment to Post");
        HttpEntity<Comment> requestEntityForAddComment = new HttpEntity<>(comment, headers);
        ResponseEntity<String> responseEntityForAddComment = restTemplate.exchange(addCommentUrl, HttpMethod.POST, requestEntityForAddComment, String.class);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntityForAddComment.getStatusCode());
        Assertions.assertTrue(responseEntityForAddComment.getBody().contains(Constant.POST_NOT_FOUND));
    }
}
