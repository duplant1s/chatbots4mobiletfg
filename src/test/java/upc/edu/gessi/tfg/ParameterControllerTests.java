package upc.edu.gessi.tfg;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
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
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import upc.edu.gessi.tfg.models.Feature;
import upc.edu.gessi.tfg.models.FeatureIntegration;
import upc.edu.gessi.tfg.models.ParamType;
import upc.edu.gessi.tfg.models.Parameter;
import upc.edu.gessi.tfg.models.ParameterIntegration;
import upc.edu.gessi.tfg.models.User;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ParameterControllerTests {
    
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + port;
    }

    //PARAMETERS CRUD TESTS

    @ParameterizedTest
    @Order(1)
    @CsvSource({
        "testParameter, 201 CREATED",
        "location, 409 CONFLICT",
    })
    public void testCreateParameter(String identifier, String expectedStatusCode) {
        Parameter parameter = new Parameter(identifier, "new parameter", ParamType.Text);
        ResponseEntity<Parameter> response = restTemplate.postForEntity(baseUrl + "/parameters", parameter, Parameter.class);
        assertEquals(expectedStatusCode, response.getStatusCode().toString());
    }

    @ParameterizedTest
    @Order(2)
    @CsvSource({
        "testParameter, 200 OK",
        "nonexistent, 404 NOT_FOUND"
    })
    public void testGetParameter(String identifier, String expectedStatusCode) {
        ResponseEntity<Parameter> response = restTemplate.getForEntity(baseUrl + "/parameters/" + identifier, Parameter.class);
        assertEquals(expectedStatusCode, response.getStatusCode().toString());
    }

    @Test
    @Order(3)
    public void testGetAllParameters() {
        ResponseEntity<List<Parameter>> response = restTemplate.exchange(baseUrl + "/parameters", HttpMethod.GET, null, new ParameterizedTypeReference<List<Parameter>>() {});
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertThat(response.getBody().size()).isGreaterThan(0);
    }

    @ParameterizedTest
    @Order(4)
    @CsvSource({
        "testParameter, 200 OK",
        "nonexistent, 404 NOT_FOUND"
    })
    public void testUpdateParameter(String identifier, String expectedStatusCode) {
        Parameter parameter = new Parameter(identifier, "updated parameter", ParamType.Text);
        ResponseEntity<Parameter> response = restTemplate.exchange(
            baseUrl + "/parameters/" + identifier,
            HttpMethod.PUT,
            new HttpEntity<>(parameter),
            Parameter.class);

        assertEquals(expectedStatusCode, response.getStatusCode().toString());
    }

    @ParameterizedTest
    @Order(5)
    @CsvSource({
        "testParameter, 204 NO_CONTENT",
        "nonexistent, 404 NOT_FOUND"
    })
    public void testDeleteParameter(String identifier, String expectedStatusCode) {
        ResponseEntity<Parameter> response = restTemplate.exchange(
            baseUrl + "/parameters/" + identifier,
            HttpMethod.DELETE,
            null,
            Parameter.class);
        
        assertEquals(expectedStatusCode, response.getStatusCode().toString());
    }

    // PARAMETER INTEGRATION TESTS

    @ParameterizedTest
    @Order(6)
    @CsvSource({
        "test, test2, 201 CREATED",
        "event-name, route-name, 409 CONFLICT",
    })
    public void testCreateParameterIntegration(String source, String target, String expectedHttpStatus) {
        ParameterIntegration parameterIntegration = new ParameterIntegration(source, target);
        ResponseEntity<ParameterIntegration> response = restTemplate.postForEntity(baseUrl + "/parameters/integrations", parameterIntegration, ParameterIntegration.class);
        assertEquals(expectedHttpStatus, response.getStatusCode().toString());
    }

    @ParameterizedTest
    @Order(7)
    @CsvSource({
        "test, test2, 200 OK",
        "nonexistent, nonexistent2, 404 NOT_FOUND"
    })
    public void testGetParameterIntegration(String source, String target, String expectedHttpStatus) {
        ResponseEntity<ParameterIntegration> response = restTemplate.getForEntity(baseUrl + "/parameters/integrations/" + source + "-" + target, ParameterIntegration.class);
        assertEquals(expectedHttpStatus, response.getStatusCode().toString());
    }

    @Test
    @Order(8)
    public void testGetAllParameterIntegrations() {
        ResponseEntity<List<ParameterIntegration>> response = restTemplate.exchange(baseUrl + "/parameters/integrations", HttpMethod.GET, null, new ParameterizedTypeReference<List<ParameterIntegration>>() {});
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertThat(response.getBody().size()).isGreaterThan(0);
    }

    @ParameterizedTest
    @Order(9)
    @CsvSource({
        "test, test2, 200 OK",
        "nonexistent, nonexistent2, 404 NOT_FOUND"
    })
    public void testUpdateParameterIntegration(String source, String target, String expectedHttpStatus) {
        ParameterIntegration parameterIntegration = new ParameterIntegration(source, target);
        parameterIntegration.setName("new name!");
        ResponseEntity<ParameterIntegration> response = restTemplate.exchange(
            baseUrl + "/parameters/integrations/" + source + "-" + target,
            HttpMethod.PUT,
            new HttpEntity<>(parameterIntegration),
            ParameterIntegration.class);
        assertEquals(expectedHttpStatus, response.getStatusCode().toString());
    }

    @ParameterizedTest
    @Order(10)
    @CsvSource({
        "test, test2, 204 NO_CONTENT",
        "nonexistent, nonexistent2, 404 NOT_FOUND"
    })
    public void testDeleteParameterIntegration(String source, String target, String expectedHttpStatus) {
        ResponseEntity<ParameterIntegration> response = restTemplate.exchange(
            baseUrl + "/parameters/integrations/" + source + "-" + target,
            HttpMethod.DELETE,
            null,
            ParameterIntegration.class);
        
        assertEquals(expectedHttpStatus, response.getStatusCode().toString());
    }
}
