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
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import upc.edu.gessi.tfg.models.Feature;
import upc.edu.gessi.tfg.models.FeatureIntegration;
import upc.edu.gessi.tfg.models.User;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FeatureControllerTests {
    
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + port;
    }

    //FEATURES CRUD TESTS

    @ParameterizedTest
    @Order(1)
    @CsvSource({
        "newFeatureTEST, 201 CREATED",
        "testFeature, 409 CONFLICT"
    })
    public void testCreateFeature(String identifier, String expectedStatus) {
        Feature feature = new Feature(identifier, "Feature name", new ArrayList<>());
        ResponseEntity<Feature> response = restTemplate.postForEntity(baseUrl + "/features", feature, Feature.class);
        assertEquals(expectedStatus, response.getStatusCode().toString());
    }

    @Test
    @Order(2)
    public void testGetAllFeatures() {
        ResponseEntity<List<Feature>> response = restTemplate.exchange(baseUrl + "/features", HttpMethod.GET, null, new ParameterizedTypeReference<List<Feature>>() {});           
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertThat(response.getBody().size()).isGreaterThan(0);
    }

    @ParameterizedTest
    @Order(3)
    @CsvSource({
        "newFeatureTEST, 200 OK",
        "notExistingFeature, 404 NOT_FOUND"
    })
    public void testGetFeatureById(String identifier, String expectedStatus) {
        ResponseEntity<Feature> response = restTemplate.getForEntity(baseUrl + "/features/" + identifier, Feature.class);
        assertEquals(expectedStatus, response.getStatusCode().toString());
    }

    @ParameterizedTest
    @Order(4)
    @CsvSource({
        "newFeatureTEST, 200 OK",
        "notExistingFeature, 404 NOT_FOUND"
    })
    public void testUpdateFeature(String identifier, String expectedStatus) {
        Feature feature = new Feature("newFeature", "changedName", new ArrayList<>());
        restTemplate.put(baseUrl + "/features/" + identifier, feature);
        ResponseEntity<Feature> response = restTemplate.getForEntity(baseUrl + "/features/" + identifier, Feature.class);
        assertEquals(expectedStatus, response.getStatusCode().toString());
    }

    @ParameterizedTest
    @Order(5)
    @CsvSource({
        "newFeatureTEST, 204 NO_CONTENT",
        "notExistingFeature, 404 NOT_FOUND"
    })
    public void testDeleteFeature(String identifier, String expectedStatus) {
        ResponseEntity<Feature> response = restTemplate.exchange(
                baseUrl + "/features/" + identifier,
                HttpMethod.DELETE,
                null,
                Feature.class);
        
        assertEquals(expectedStatus, response.getStatusCode().toString());
    }

    // FEATURE INTEGRATION TESTS

    @ParameterizedTest
    @Order(6)
    @CsvSource({
        "testFeature2, testFeature, 201 CREATED",
        "testingPlanRoute, testingScheduleEvent, 409 CONFLICT"
    })
    public void testCreateFeatureIntegration(String source, String target, String expectedStatus) {
        FeatureIntegration feature = new FeatureIntegration(source, target);
        ResponseEntity<FeatureIntegration> response = restTemplate.postForEntity(baseUrl + "/features/integrations", feature, FeatureIntegration.class);
        assertEquals(expectedStatus, response.getStatusCode().toString());
    }

    @Test
    @Order(7)
    public void testGetAllFeatureIntegrations() {
        ResponseEntity<List<FeatureIntegration>> response = restTemplate.exchange(baseUrl + "/features/integrations", HttpMethod.GET, null, new ParameterizedTypeReference<List<FeatureIntegration>>() {});           
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertThat(response.getBody().size()).isGreaterThan(0);
    }

    @ParameterizedTest
    @Order(8)
    @CsvSource({
        "testFeature2, testFeature, 200 OK",
        "testingPlanRoute, CreateTask, 404 NOT_FOUND"
    })
    public void testGetFeatureIntegrationById(String source, String target, String expectedStatus) {
        ResponseEntity<FeatureIntegration> response = restTemplate.getForEntity(baseUrl + "/features/integrations/" + source + "-" + target, FeatureIntegration.class);
        assertEquals(expectedStatus, response.getStatusCode().toString());
    }

    @ParameterizedTest
    @Order(9)
    @CsvSource({
        "testFeature2, testFeature, 200 OK",
        "testingPlanRoute, CreateTask, 404 NOT_FOUND"
    })
    public void testUpdateFeatureIntegration(String source, String target, String expectedStatus) {
        FeatureIntegration feature = new FeatureIntegration(source, target);
        feature.setName("newName");
        restTemplate.put(baseUrl + "/features/integrations/" + source + "-" + target, feature);
        ResponseEntity<FeatureIntegration> response = restTemplate.getForEntity(baseUrl + "/features/integrations/" + source + "-" + target, FeatureIntegration.class);
        assertEquals(expectedStatus, response.getStatusCode().toString());
    }

    @ParameterizedTest
    @Order(10)
    @CsvSource({
        "testFeature2, testFeature, 204 NO_CONTENT",
        "testingPlanRoute, CreateTask, 404 NOT_FOUND"
    })
    public void testDeleteFeatureIntegration(String source, String target, String expectedStatus) {
        ResponseEntity<FeatureIntegration> response = restTemplate.exchange(
                baseUrl + "/features/integrations/" + source + "-" + target,
                HttpMethod.DELETE,
                null,
                FeatureIntegration.class);
        
        assertEquals(expectedStatus, response.getStatusCode().toString());
    }
}
