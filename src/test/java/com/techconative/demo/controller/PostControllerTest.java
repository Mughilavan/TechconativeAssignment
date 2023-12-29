package com.techconative.demo.controller;

import com.techconative.demo.entity.Post;
import com.techconative.demo.entity.User;
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
public class PostControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @LocalServerPort
    private int port;

    @Autowired
    private CommentRepository commentRepository;

    private HttpHeaders headers = getHttpHeader();

    private final Logger logger =  LoggerFactory.getLogger(PostControllerTest.class);

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
    void testCreateRetrieveUpdateAndDeletePostSuccessCase() {
        User user = TestUtil.getUserObject("Mughilavan1", "mughilavanceg1@gmail.com", "teststa", "1234567291", "917456321025");
        HttpEntity<User> requestEntity = new HttpEntity<>(user, headers);
        restTemplate.postForEntity("http://localhost:" + port + "/socialmediaapp/api/users", requestEntity, String.class);

        //create post success case
        Long userId = userRepository.findLatestUserId();
        logger.info("latest user id is: {}", userId);
        user = new User();
        user.setId(userId);
        Post post = new Post();
        post.setContent("Sample post");
        post.setUser(user);
        HttpEntity<Post> requestEntityForCreatePost = new HttpEntity<>(post, headers);
        ResponseEntity<String> responseEntityForCreatePost = restTemplate.postForEntity("http://localhost:" + port + "/socialmediaapp/api/posts", requestEntityForCreatePost, String.class);
        Assertions.assertEquals(HttpStatus.CREATED, responseEntityForCreatePost.getStatusCode());
        Assertions.assertTrue(responseEntityForCreatePost.getBody().contains("Sample post"));

        //retrieve post success case
        Long latestPostId = postRepository.findLatestPostId();
        logger.info("Latest post id is: {}",latestPostId);
        String getPostUrl = "http://localhost:" + port + "/socialmediaapp/api/posts/{postId}";
        ResponseEntity<String> responseEntityForGetPost = restTemplate.getForEntity(getPostUrl, String.class, latestPostId);
        Assertions.assertEquals(HttpStatus.OK, responseEntityForGetPost.getStatusCode());
        Assertions.assertTrue(responseEntityForGetPost.getBody().contains("Sample post"));
        Assertions.assertTrue(responseEntityForGetPost.getBody().contains("userName='Mughilavan1', email='mughilavanceg1@gmail.com', password='teststa', aadharNumber='917456321025', mobileNumber='1234567291'"));

        //update post success case
        User updatedUser = new User();
        updatedUser.setId(userId);
        Post updatePost = new Post();
        updatePost.setContent("Post content updated");
        updatePost.setUser(updatedUser);
        HttpEntity<Post> requestEntityForUpdatePost = new HttpEntity<>(updatePost, headers);
        String updatePostUrl = "http://localhost:" + port + "/socialmediaapp/api/posts/"+ latestPostId;
        ResponseEntity<String> responseEntityForUpdatePost = restTemplate.exchange(updatePostUrl, HttpMethod.PUT, requestEntityForUpdatePost, String.class);
        Assertions.assertEquals(HttpStatus.OK, responseEntityForUpdatePost.getStatusCode());
        Assertions.assertTrue(responseEntityForUpdatePost.getBody().contains("Post content updated"));

        //Delete Post success case
        String deletePostUrl = "http://localhost:" + port + "/socialmediaapp/api/posts/" + latestPostId;
        ResponseEntity<String> responseEntityForDeletePost = restTemplate.exchange(deletePostUrl, HttpMethod.DELETE, null, String.class);
        Assertions.assertEquals(HttpStatus.OK, responseEntityForDeletePost.getStatusCode());
        Assertions.assertTrue(responseEntityForDeletePost.getBody().contains(Constant.POST_DELETED+latestPostId));


    }

    @Test
    void testCreatePostFailureCase() {
        Long userId = Long.MAX_VALUE;
        logger.info("latest user id is: {}", userId);
        User user = new User();
        user.setId(userId);
        Post post = new Post();
        post.setContent("Sample post");
        post.setUser(user);
        HttpEntity<Post> requestEntityForCreatePost = new HttpEntity<>(post, headers);
        ResponseEntity<String> responseEntityForCreatePost = restTemplate.postForEntity("http://localhost:" + port + "/socialmediaapp/api/posts", requestEntityForCreatePost, String.class);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntityForCreatePost.getStatusCode());
        Assertions.assertTrue(responseEntityForCreatePost.getBody().contains(Constant.USER_NOT_FOUND));

        post = new Post();
        post.setContent("Sample post");
        HttpEntity<Post> requestEntityForCreatePostInvalid = new HttpEntity<>(post, headers);
        ResponseEntity<String> responseEntityForCreatePostInvalid = restTemplate.postForEntity("http://localhost:" + port + "/socialmediaapp/api/posts", requestEntityForCreatePostInvalid, String.class);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntityForCreatePostInvalid.getStatusCode());
        Assertions.assertTrue(responseEntityForCreatePostInvalid.getBody().contains(Constant.USER_OBJECT_NOT_FOUND));

        post = new Post();
        post.setContent("Sample post");
        post.setUser(new User());
        HttpEntity<Post> requestEntityForCreatePostInvalid2 = new HttpEntity<>(post, headers);
        ResponseEntity<String> responseEntityForCreatePostInvalid2 = restTemplate.postForEntity("http://localhost:" + port + "/socialmediaapp/api/posts", requestEntityForCreatePostInvalid2, String.class);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntityForCreatePostInvalid2.getStatusCode());
        Assertions.assertTrue(responseEntityForCreatePostInvalid2.getBody().contains(Constant.USER_ID_NOT_PRESENT));

    }

    @Test
    void testGetPostFailureCase() {
        Long userId = Long.MAX_VALUE;
        String getPostUrl = "http://localhost:" + port + "/socialmediaapp/api/posts/{postId}";
        ResponseEntity<String> responseEntityForGetPost = restTemplate.getForEntity(getPostUrl, String.class, userId);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseEntityForGetPost.getStatusCode());
        Assertions.assertTrue(responseEntityForGetPost.getBody().contains(Constant.POST_NOT_FOUND));
    }

    @Test
    void testDeletePostFailureCase() {
        String deletePostUrl = "http://localhost:" + port + "/socialmediaapp/api/posts/" + Long.MAX_VALUE;
        ResponseEntity<String> responseEntityForDeletePost = restTemplate.exchange(deletePostUrl, HttpMethod.DELETE, null, String.class);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseEntityForDeletePost.getStatusCode());
        Assertions.assertTrue(responseEntityForDeletePost.getBody().contains(Constant.POST_NOT_FOUND+Long.MAX_VALUE));
    }

    @Test
    void testUpdatePostFailureCase() {
        Post updatePost = new Post();
        updatePost.setContent("Post content updated");
        HttpEntity<Post> requestEntityForUpdatePost = new HttpEntity<>(updatePost, headers);
        String updatePostUrl = "http://localhost:" + port + "/socialmediaapp/api/posts/"+ Long.MAX_VALUE;
        ResponseEntity<String> responseEntityForUpdatePost = restTemplate.exchange(updatePostUrl, HttpMethod.PUT, requestEntityForUpdatePost, String.class);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseEntityForUpdatePost.getStatusCode());
        Assertions.assertTrue(responseEntityForUpdatePost.getBody().contains(Constant.POST_NOT_FOUND+Long.MAX_VALUE));

    }

    private HttpHeaders getHttpHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        return headers;
    }

}
