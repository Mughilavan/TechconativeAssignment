package com.techconative.demo.controller;

import com.techconative.demo.bo.User;
import com.techconative.demo.constants.Constant;
import com.techconative.demo.repository.CommentRepository;
import com.techconative.demo.repository.PostRepository;
import com.techconative.demo.repository.UserRepository;
import com.techconative.demo.util.TestUtil;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

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

    private final Logger logger =  LoggerFactory.getLogger(UserControllerTest.class);

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
    public void testRegisterUserAndTestGetUserProfileSuccessCase() {
        ResponseEntity<String> responseEntity = createPostValidCase();
        Assertions.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        Assertions.assertTrue(responseEntity.getBody().contains("userName='Mughilavan1', email='mughilavanceg1@gmail.com', password='teststa', aadharNumber='917456321025', mobileNumber='1234567291'"));

        Long userId = userRepository.findLatestUserId();
        logger.info("latest user id is: {}", userId);
        String getUserProfileUrl = "http://localhost:" + port + "/socialmediaapp/api/users/{userId}";
        ResponseEntity<String> responseEntityForGetUser = restTemplate.getForEntity(getUserProfileUrl, String.class, userId);
        Assertions.assertEquals(HttpStatus.OK, responseEntityForGetUser.getStatusCode());
        Assertions.assertTrue(responseEntityForGetUser.getBody().contains("userName='Mughilavan1', email='mughilavanceg1@gmail.com', password='teststa', aadharNumber='917456321025', mobileNumber='1234567291'"));

    }

    @Test
    public void testGetUserProfileFailureCase() {
        Long userId = Long.MAX_VALUE;
        String getUserProfileUrl = "http://localhost:" + port + "/socialmediaapp/api/users/{userId}";
        ResponseEntity<String> responseEntityForGetUser = restTemplate.getForEntity(getUserProfileUrl, String.class, userId);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseEntityForGetUser.getStatusCode());
        Assertions.assertTrue(responseEntityForGetUser.getBody().contains("User is not found for given userID:"));
    }

    @Test
    public void testRegisterUserFailureCase() {
        User user = TestUtil.getUserObject("Mug", "gmail.com", "test", "1234567", "91745635");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<User> requestEntity = new HttpEntity<>(user, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:" + port + "/socialmediaapp/api/users", requestEntity, String.class);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        Assertions.assertTrue(responseEntity.getBody().contains("Password must be at least 6 and maximum of 20 characters long"));
        Assertions.assertTrue(responseEntity.getBody().contains("Email must be at least 10 and maximum of 50 characters long"));
        Assertions.assertTrue(responseEntity.getBody().contains("UserName must be at least 4 and maximum of 30 characters long"));
        Assertions.assertTrue(responseEntity.getBody().contains("Aadhar number must be 12 digits"));
        Assertions.assertTrue(responseEntity.getBody().contains("Mobile number must be 10 digits"));

        ResponseEntity<String> validUserResponseEntity = createPostValidCase();
        Assertions.assertEquals(HttpStatus.CREATED, validUserResponseEntity.getStatusCode());
        Assertions.assertTrue(validUserResponseEntity.getBody().contains("userName='Mughilavan1', email='mughilavanceg1@gmail.com', password='teststa', aadharNumber='917456321025', mobileNumber='1234567291'"));

        User existingUserName = TestUtil.getUserObject("Mughilavan1", "mughilavanceg1@gmail.com", "teststa", "1234567291", "917456321025");
        headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<User> existingUserRequestEntity = new HttpEntity<>(existingUserName, headers);
        ResponseEntity<String> existingUserResponseEntity = restTemplate.postForEntity("http://localhost:" + port + "/socialmediaapp/api/users", existingUserRequestEntity, String.class);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, existingUserResponseEntity.getStatusCode());
        Assertions.assertTrue(existingUserResponseEntity.getBody().contains(Constant.USER_ALREADY_EXIST));

        User existingEmail = TestUtil.getUserObject("Mughilavan11", "mughilavanceg1@gmail.com", "teststa", "1234567291", "917456321025");
        headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<User> existingEmailRequestEntity = new HttpEntity<>(existingEmail, headers);
        ResponseEntity<String> existingEmailResponseEntity = restTemplate.postForEntity("http://localhost:" + port + "/socialmediaapp/api/users", existingEmailRequestEntity, String.class);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, existingEmailResponseEntity.getStatusCode());
        Assertions.assertTrue(existingEmailResponseEntity.getBody().contains(Constant.EMAIL_ALREADY_EXIST));

        User existingMobileNumber = TestUtil.getUserObject("Mughilavan11", "mughilavanceg1a@gmail.com", "teststa", "1234567291", "917456321025");
        headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<User> existingMobileRequestEntity = new HttpEntity<>(existingMobileNumber, headers);
        ResponseEntity<String> existingMobileResponseEntity = restTemplate.postForEntity("http://localhost:" + port + "/socialmediaapp/api/users", existingMobileRequestEntity, String.class);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, existingMobileResponseEntity.getStatusCode());
        Assertions.assertTrue(existingMobileResponseEntity.getBody().contains(Constant.MOBILE_NUMBER_ALREADY_EXIST));

        User existingAadharNumber = TestUtil.getUserObject("Mughilavan11", "mughilavanceg1a@gmail.com", "teststa", "1234567297", "917456321025");
        headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<User> existingAadharRequestEntity = new HttpEntity<>(existingAadharNumber, headers);
        ResponseEntity<String> existingAadharResponseEntity = restTemplate.postForEntity("http://localhost:" + port + "/socialmediaapp/api/users", existingAadharRequestEntity, String.class);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, existingAadharResponseEntity.getStatusCode());
        Assertions.assertTrue(existingAadharResponseEntity.getBody().contains(Constant.AADHAR_NUMBER_ALREADY_EXIST));
    }

    public ResponseEntity<String> createPostValidCase() {
        User user = TestUtil.getUserObject("Mughilavan1", "mughilavanceg1@gmail.com", "teststa", "1234567291", "917456321025");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<User> requestEntity = new HttpEntity<>(user, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:" + port + "/socialmediaapp/api/users", requestEntity, String.class);
        return responseEntity;
    }
}
