package upc.edu.gessi.tfg.controllers;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import upc.edu.gessi.tfg.models.User;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserAppServiceTests {
    

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + port;
    }

     //i want to define tests for the UserAppController class following the approach 2 defined here https://medium.com/javarevisited/spring-boot-2-junit-5-mockito-d8e2e5c8a90d

    @Test
    @Order(1)
    public void testCreateUser() throws Exception {
        User user = new User("testId", "testmail@mail.com", "testUsername", "testFamilyName");
        ResponseEntity<User> response = restTemplate.postForEntity(baseUrl + "/users", user, User.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void testGetAllUsers() throws Exception {
        ResponseEntity<List<User>> response = restTemplate.exchange(baseUrl + "/users", HttpMethod.GET, null, new ParameterizedTypeReference<List<User>>() {});
        List<User> users = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(users);
        assertThat(users.size()).isGreaterThan(0);
    }

    @ParameterizedTest
    @CsvSource({
        "testId,200 OK",
        "notExistingId,404 NOT_FOUND"
    })
    public void testGetUserById(String id, String expectedStatus) {
        ResponseEntity<User> response = restTemplate.getForEntity(baseUrl + "/users/" + id, User.class);
        assertEquals(expectedStatus, response.getStatusCode().toString());
    }


    @Test
    public void testUpdateUser() throws Exception {
        User user = new User("testId", "newmail@mail.com", "testUsername", "testFamilyName");
        restTemplate.put(baseUrl + "/users/testId", user);
        ResponseEntity<User> response = restTemplate.getForEntity(baseUrl + "/users/testId", User.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(response.getBody().getEmail(), "newmail@mail.com");
    }

    @Test
    public void testDeleteUser() throws Exception {
        restTemplate.delete(baseUrl + "/users/testId");
        ResponseEntity<User> response = restTemplate.getForEntity(baseUrl + "/users/testId", User.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }





}