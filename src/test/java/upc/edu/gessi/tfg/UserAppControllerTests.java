package upc.edu.gessi.tfg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.catalina.connector.Response;
import org.apache.tomcat.util.modeler.FeatureInfo;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import upc.edu.gessi.tfg.models.App;
import upc.edu.gessi.tfg.models.AppCategory;
import upc.edu.gessi.tfg.models.AppIntegration;
import upc.edu.gessi.tfg.models.FeatureIntegration;
import upc.edu.gessi.tfg.models.ParameterIntegration;
import upc.edu.gessi.tfg.models.User;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserAppControllerTests {
    

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + port;
    }

    // USERS CRUD TESTS

    @ParameterizedTest
    @Order(1)
    @CsvSource({
        //a test user is created, it should return 201
        "testingID, 201 CREATED",
        //an already existing user is trying to be created, it should return 409
        "quim-motger@gessi.upc.edu, 409 CONFLICT"
    })
    public void testCreateUser(String username, String expectedStatus){
        User user = new User(username, "testmail@mail.com", "testGivenName", "testFamilyName");
        ResponseEntity<User> response = restTemplate.postForEntity(baseUrl + "/users", user, User.class);
        assertEquals(expectedStatus, response.getStatusCode().toString());
    }

    @Test
    @Order(2)
    public void testGetAllUsers() throws Exception {
        ResponseEntity<List<User>> response = restTemplate.exchange(
            baseUrl + "/users", HttpMethod.GET, null,new ParameterizedTypeReference<List<User>>() {});
        List<User> users = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(users);
        assertThat(users.size()).isGreaterThan(0);
    }

    @ParameterizedTest
    @Order(3)
    @CsvSource({
        //an existing user is trying to be accessed, it should return 200
        "testingID,200 OK",
        //a non existing user is trying to be accessed, it should return 404
        "notExistingId,404 NOT_FOUND"
    })
    public void testGetUserById(String id, String expectedStatus) {
        ResponseEntity<User> response = restTemplate.getForEntity(baseUrl + "/users/" + id, User.class);
        assertEquals(expectedStatus, response.getStatusCode().toString());
    }
    
    @ParameterizedTest
    @Order(4)
    @CsvSource({
        "testingID,200 OK",
        "notExistingId,404 NOT_FOUND"
    })
    public void testUpdateUser(String id, String expectedStatus) {
        User user = new User(id, "newmail@mail.com", "testGivenName", "testFamilyName");
        restTemplate.put(baseUrl + "/users/" + id, user);
        ResponseEntity<User> response = restTemplate.getForEntity(baseUrl + "/users/" + id, User.class);
        assertEquals(expectedStatus, response.getStatusCode().toString());
    }

    // PREFERRED INTEGRATIONS TESTS

    @ParameterizedTest
    @Order(5)
    @CsvSource({
        "testingID, 201 CREATED",
        "invalidUser, 404 NOT_FOUND"
    })
    public void testAddPreferredAppIntegration(String id, String expectedStatus) {
        AppIntegration appIntegration = new AppIntegration("com.strava.test", "com.google.android.calendar.test");
        ResponseEntity<User> response = restTemplate.postForEntity(baseUrl + "/users/" + id + "/integrations/apps", appIntegration, User.class);
        assertEquals(expectedStatus, response.getStatusCode().toString());
    }

    @ParameterizedTest
    @Order(10)
    @CsvSource({
        "testingID, 204 NO_CONTENT",
        "invalidUser, 404 NOT_FOUND"
    })
    public void testDeletePreferredAppIntegration(String id, String expectedStatus) {
        AppIntegration appIntegration = new AppIntegration("com.strava.test", "com.google.android.calendar.test");
        ResponseEntity<User> response = restTemplate.exchange(
            baseUrl + "/users/" + id + "/integrations/apps",
            HttpMethod.DELETE,
            new HttpEntity<>(appIntegration),
            User.class);
        assertEquals(expectedStatus, response.getStatusCode().toString());
    }

    @ParameterizedTest
    @Order(6)
    @CsvSource({
        "testingID, 201 CREATED",
        "invalidUser, 404 NOT_FOUND"
    })
    public void testAddPreferredFeatureIntegration(String id, String expectedStatus) {
        FeatureIntegration featureIntegration = new FeatureIntegration("testFeature", "testFeature2");
        ResponseEntity<User> response = restTemplate.postForEntity(baseUrl + "/users/" + id + "/integrations/features", featureIntegration, User.class);
        assertEquals(expectedStatus, response.getStatusCode().toString());
    }

    @ParameterizedTest
    @Order(11)
    @CsvSource({
        "testingID, 204 NO_CONTENT",
        "invalidUser, 404 NOT_FOUND"
    })
    public void testDeletePreferredFeatureIntegration(String id, String expectedStatus) {
        FeatureIntegration featureIntegration = new FeatureIntegration("testFeature", "testFeature2");
        ResponseEntity<User> response = restTemplate.exchange(
            baseUrl + "/users/" + id + "/integrations/features",
            HttpMethod.DELETE,
            new HttpEntity<>(featureIntegration),
            User.class);
        assertEquals(expectedStatus, response.getStatusCode().toString());
    }

    @ParameterizedTest
    @Order(7)
    @CsvSource({
        "testingID, 201 CREATED",
        "invalidUser, 404 NOT_FOUND"
    })
    public void testAddPreferredParameterIntegration(String id, String expectedStatus) {
        ParameterIntegration parameterIntegration = new ParameterIntegration("task-name", "route-name");
        ResponseEntity<User> response = restTemplate.postForEntity(baseUrl + "/users/" + id + "/integrations/parameters", parameterIntegration, User.class);
        assertEquals(expectedStatus, response.getStatusCode().toString());
    }

    @ParameterizedTest
    @Order(12)
    @CsvSource({
        "testingID, 204 NO_CONTENT",
        "invalidUser, 404 NOT_FOUND"
    })
    public void testDeletePreferredParameterIntegration(String id, String expectedStatus) {
        ParameterIntegration parameterIntegration = new ParameterIntegration("task-name", "route-name");
        ResponseEntity<User> response = restTemplate.exchange(
            baseUrl + "/users/" + id + "/integrations/parameters",
            HttpMethod.DELETE,
            new HttpEntity<>(parameterIntegration),
            User.class);
        assertEquals(expectedStatus, response.getStatusCode().toString());
    }

    //USER STORIES TESTS
    @ParameterizedTest
    @Order(8)
    @CsvSource({
        "testingID, testFeature, 200 OK",
        "invalidUser, testFeature, 404 NOT_FOUND"
    })
    public void testRequestFeatureIntegration(String id, String feature, String expectedStatus) {
        ResponseEntity<List<String>> response = restTemplate.exchange(baseUrl + "/users/" + id + "/integrations/features/" + feature, HttpMethod.GET, null, new ParameterizedTypeReference<List<String>>() {});	
        assertEquals(expectedStatus, response.getStatusCode().toString());
    }

    @ParameterizedTest
    @Order(9)
    @CsvSource({
        "testingID, testFeature, testFeature2, 200 OK",
        "invalidUser, testFeature, testFeature2, 404 NOT_FOUND"
    })
    public void testGetAppsFromFeature(String id, String feature, String feature2, String expectedStatus) {
        ResponseEntity<List<String>> response = restTemplate.exchange(baseUrl + "/users/" + id + "/integrations/apps/feature/" + feature + "/" + feature2, HttpMethod.GET, null, new ParameterizedTypeReference<List<String>>() {});	
        assertEquals(expectedStatus, response.getStatusCode().toString());
    }

    @ParameterizedTest
    @Order(13)
    @CsvSource({
        "testingID,204 NO_CONTENT",
        "notExistingId,404 NOT_FOUND"
    })
    public void testDeleteUser(String id, String expectedStatus) {
        ResponseEntity<User> response = restTemplate.exchange(
                baseUrl + "/users/" + id,
                HttpMethod.DELETE,
                null,
                User.class);
        
        assertEquals(expectedStatus, response.getStatusCode().toString());
    }

    // APPS CRUD TESTS
    //make the same tests that have been done for the user but for the apps in the userAppController class

    @ParameterizedTest
    @Order(14)
    @CsvSource({
        "com.app.test, 201 CREATED",
        "com.strava.test, 409 CONFLICT"
    })
    public void testCreateApp(String name, String expectedStatus){
        App app = new App("appName", name, "testDescription", "testSummary", "testReleaseNotes", AppCategory.CALENDAR, "testDatePublished", "2022-05-05", "testSoftwareVersion", new ArrayList<String>(Arrays.asList("testFeature")));
        ResponseEntity<App> response = restTemplate.postForEntity(baseUrl + "/apps", app, App.class);
        assertEquals(expectedStatus, response.getStatusCode().toString());
    }

    @Test
    @Order(15)
    public void testGetAllApps() throws Exception {
        ResponseEntity<List<App>> response = restTemplate.exchange(baseUrl + "/apps", HttpMethod.GET, null, new ParameterizedTypeReference<List<App>>() {});
        List<App> apps = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(apps);
        assertThat(apps.size()).isGreaterThan(0);
    }

    @ParameterizedTest
    @Order(16)
    @CsvSource({
        "com.app.test,200 OK",
        "not.an.app,404 NOT_FOUND"
    })
    public void testGetAppById(String id, String expectedStatus) {
        ResponseEntity<App> response = restTemplate.getForEntity(baseUrl + "/apps/" + id, App.class);
        assertEquals(expectedStatus, response.getStatusCode().toString());
    }

    // @ParameterizedTest
    // @Order(9)
    // @CsvSource({
    //     "com.app.test,200 OK",
    //     "not.an.app,404 NOT_FOUND"
    // })
    // public void testUpdateApp(String id, String expectedStatus) {
    //     App app = new App("appName", id, "testDescription", "testSummary", "testReleaseNotes", AppCategory.CALENDAR, "testDatePublished", "2022-05-05", "testSoftwareVersion", new ArrayList<String>(Arrays.asList("testFeature")));
    //     restTemplate.put(baseUrl + "/apps/" + id, app);
    //     ResponseEntity<App> response = restTemplate.getForEntity(baseUrl + "/apps/" + id, App.class);
    //     assertEquals(expectedStatus, response.getStatusCode().toString());
    // }

    @ParameterizedTest
    @Order(17)
    @CsvSource({
        "com.app.test,204 NO_CONTENT",
        "not.an.app,404 NOT_FOUND"
    })
    public void testDeleteApp(String id, String expectedStatus) {
        ResponseEntity<App> response = restTemplate.exchange(
                baseUrl + "/apps/" + id,
                HttpMethod.DELETE,
                null,
                App.class);
        
        assertEquals(expectedStatus, response.getStatusCode().toString());
    }

    // APP INTEGRATION TESTS

    @ParameterizedTest
    @Order(18)
    @CsvSource({
        "com.ticktick.task.test, com.simplemobiletools.calendar.test, 201 CREATED",
        "com.strava.test, com.simplemobiletools.calendar.test, 409 CONFLICT",
    })
    public void testCreateAppIntegration(String sourceApp, String targetApp, String expectedStatus){
        AppIntegration appIntegration = new AppIntegration(sourceApp, targetApp);
        ResponseEntity<AppIntegration> response = restTemplate.postForEntity(baseUrl + "/apps/integrations", appIntegration, AppIntegration.class);
        assertEquals(expectedStatus, response.getStatusCode().toString());
    }

    @Test
    @Order(19)
    public void testGetAllAppIntegrations() throws Exception {
        ResponseEntity<List<AppIntegration>> response = restTemplate.exchange(baseUrl + "/apps/integrations", HttpMethod.GET, null, new ParameterizedTypeReference<List<AppIntegration>>() {});
        List<AppIntegration> appIntegrations = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(appIntegrations);
        assertThat(appIntegrations.size()).isGreaterThan(0);
    }

    @ParameterizedTest
    @Order(20)
    @CsvSource({
        "com.ticktick.task.test-com.simplemobiletools.calendar.test, 200 OK",
        "com.strava.test-myfit.app.fitness.centric.app.test, 404 NOT_FOUND"
    })
    public void testGetAppIntegrationById(String id, String expectedStatus) {
        ResponseEntity<AppIntegration> response = restTemplate.getForEntity(baseUrl + "/apps/integrations/" + id, AppIntegration.class);
        assertEquals(expectedStatus, response.getStatusCode().toString());
    }

    @ParameterizedTest
    @Order(21)
    @CsvSource({
        "com.ticktick.task.test, com.simplemobiletools.calendar.test, 200 OK",
        "com.strava.test, myfit.app.fitness.centric.app.test, 404 NOT_FOUND"
    })
    public void testUpdateAppIntegration(String sourceApp, String targetApp, String expectedStatus) {
        AppIntegration appIntegration = new AppIntegration(sourceApp, targetApp);
        appIntegration.setName("newName");
        restTemplate.put(baseUrl + "/apps/integrations/" + appIntegration.getIdentifier(), appIntegration);
        ResponseEntity<AppIntegration> response = restTemplate.getForEntity(baseUrl + "/apps/integrations/" + sourceApp+"-"+targetApp, AppIntegration.class);
        assertEquals(expectedStatus, response.getStatusCode().toString());
    }

    @ParameterizedTest
    @Order(22)
    @CsvSource({
        "com.ticktick.task.test-com.simplemobiletools.calendar.test, 204 NO_CONTENT",
        "com.strava.test-myfit.app.fitness.centric.app.test, 404 NOT_FOUND"
    })
    public void testDeleteAppIntegration(String id, String expectedStatus) {
        ResponseEntity<AppIntegration> response = restTemplate.exchange(
                baseUrl + "/apps/integrations/" + id,
                HttpMethod.DELETE,
                null,
                AppIntegration.class);
        
        assertEquals(expectedStatus, response.getStatusCode().toString());
    }



}