package upc.edu.gessi.tfg.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import upc.edu.gessi.tfg.models.User;
import upc.edu.gessi.tfg.models.App;
import upc.edu.gessi.tfg.models.AppIntegration;
import upc.edu.gessi.tfg.models.FeatureIntegration;
import upc.edu.gessi.tfg.models.ParameterIntegration;
import upc.edu.gessi.tfg.services.UserAppService;

@RestController
public class UserAppController {

    @Autowired
    private UserAppService userAppService;

    //USER OPERATIONS 
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userAppService.getAllUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        User user = userAppService.getUserById(id);
        if (user == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(user);
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        userAppService.createUser(user);
        return ResponseEntity.created(null).build();
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User user) {
        userAppService.updateUser(id, user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable String id) {
        userAppService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    //PREFFERED INTEGRATIONS OPERATIONS

    @PostMapping("/users/{id}/integrations/apps")
    public ResponseEntity<User> addPreferredPAppIntegration(@PathVariable String id, @RequestBody AppIntegration appIntegration) {
        userAppService.addPreferredAppIntegration(id, appIntegration);
        return ResponseEntity.created(null).build();
    }

    @DeleteMapping("/users/{id}/integrations/apps")
    public ResponseEntity<User> deletePreferredAppIntegration(@PathVariable String id, @RequestBody AppIntegration appIntegration) {
        userAppService.deletePreferredAppIntegration(id, appIntegration);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/users/{id}/integrations/features")
    public ResponseEntity<User> addPreferredFeatureIntegration(@PathVariable String id, @RequestBody FeatureIntegration featureIntegration) {
        userAppService.addPreferredFeatureIntegration(id, featureIntegration);
        return ResponseEntity.created(null).build();
    }

    @DeleteMapping("/users/{id}/integrations/features")
    public ResponseEntity<User> deletePreferredFeatureIntegration(@PathVariable String id, @RequestBody FeatureIntegration featureIntegration) {
        userAppService.deletePreferredFeatureIntegration(id, featureIntegration);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/users/{id}/integrations/parameters")
    public ResponseEntity<User> addPreferredParameterIntegration(@PathVariable String id, @RequestBody ParameterIntegration parameterIntegration) {
        userAppService.addPreferredParameterIntegration(id, parameterIntegration);
        return ResponseEntity.created(null).build();
    }

    @DeleteMapping("/users/{id}/integrations/parameters")
    public ResponseEntity<User> deletePreferredParameterIntegration(@PathVariable String id, @RequestBody ParameterIntegration parameterIntegration) {
        userAppService.deletePreferredParameterIntegration(id, parameterIntegration);
        return ResponseEntity.noContent().build();
    }

    //APP OPERATIONS
    @GetMapping("/apps")
    public ResponseEntity<List<App>> getAllApps() {
        return ResponseEntity.ok(userAppService.getAllApps());
    }

    @GetMapping("/apps/{id}")
    public ResponseEntity<App> getAppById(@PathVariable String id) {
        App app = userAppService.getAppById(id);
        if (app == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(app);
    }

    @PostMapping("/apps")
    public ResponseEntity<App> createApp(@RequestBody App app) {
        userAppService.createApp(app);
        return ResponseEntity.created(null).build();
    }

    @PutMapping("/apps/{id}")
    public ResponseEntity<App> updateApp(@PathVariable String id, @RequestBody App app) {
        userAppService.updateApp(id, app);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/apps/{id}")
    public ResponseEntity<App> deleteApp(@PathVariable String id) {
        userAppService.deleteApp(id);
        return ResponseEntity.noContent().build();
    }

    //APP INTEGRATION OPERATIONS
    @GetMapping("/apps/integrations")
    public ResponseEntity<List<AppIntegration>> getAllAppsIntegrations() {
        return ResponseEntity.ok(userAppService.getAllAppIntegrations());
    }

    @GetMapping("/apps/integrations/{id}")
    public ResponseEntity<AppIntegration> getAppIntegrationById(@PathVariable String id) {
        AppIntegration app = userAppService.getAppIntegrationById(id);
        if (app == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(app);
    }

    @PostMapping("/apps/integrations")
    public ResponseEntity<AppIntegration> createAppIntegration(@RequestBody AppIntegration app) {
        app.setId(app.getSourceApp() + "-" + app.getTargetApp());
        userAppService.createAppIntegration(app);
        return ResponseEntity.created(null).build();
    }

    @PutMapping("/apps/integrations/{id}")
    public ResponseEntity<AppIntegration> updateAppIntegration(@PathVariable String id, @RequestBody AppIntegration app) {
        userAppService.updateAppIntegration(id, app);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/apps/integrations/{id}")
    public ResponseEntity<AppIntegration> deleteAppIntegration(@PathVariable String id) {
        userAppService.deleteAppIntegration(id);
        return ResponseEntity.noContent().build();
    }

    //USER STORY #1
    @GetMapping("/users/{id}/integrations/features/{sourceFeature}")
    public ResponseEntity<List<String>> requestFeatureIntegration(@PathVariable String id, @PathVariable String sourceFeature) {
        List<String> features = userAppService.requestFeatureIntegration(id, sourceFeature);
        if (features == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(features);
    }
    //USER STORY #2
    @GetMapping("/users/{id}/integrations/apps/feature/{sourceFeature}/{targetFeature}")
    public ResponseEntity<List<String>> getAppsFromFeature(@PathVariable String id, @PathVariable String sourceFeature, @PathVariable String targetFeature) {
        List<String> apps = userAppService.getAppsFromFeature(id, sourceFeature, targetFeature);
        if (apps == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(apps);
    }

}
