package upc.edu.gessi.tfg.controllers;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
public class UserAppServiceTests {

    @LocalServerPort
    private int port = 8080;

    private String url;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    public void setUp() throws Exception {
        url = String.format("http://localhost:%d/", port);
    }

    @Test
    public void getAllUsers() throws Exception {
        //Arrange
        url += "users";

        //Act
        ResponseEntity<List<User>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<User>>() {
                });

        //Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<User> users = response.getBody();
        assertNotNull(users);

    }
    // public void getUserById() throws Exception {

    //     //Arrange
    //     User user = new User("testID", "testEMAIL", "testGIVENNAME", "testFAMILYNAME");
    //     url += "users/" + user.getIdentifier();

    //     //Act
    //     ResponseEntity<User> response = restTemplate.exchange(url, HttpMethod.GET, null, User.class);

    //     //Assert
    //     assertEquals(HttpStatus.OK, response.getStatusCode());
    //     assertEquals(response.getBody().getIdentifier(), user.getIdentifier());
    // }


}
