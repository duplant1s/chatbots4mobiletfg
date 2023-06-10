package upc.edu.gessi.tfg.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    @Tag(name = "Users CRUD", description = "CRUD operations for users")
    @Operation(summary = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users found"),
    })
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userAppService.getAllUsers());
    }

    @Tag(name = "Users CRUD", description = "CRUD operations for users")
    @Operation(summary = "Get a user by id",
            parameters = @Parameter(name = "id", description = "User's id, typically an email", example = "example@essi.upc.edu"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        User user = userAppService.getUserById(id);
        if (user == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(user);
    }

    @Tag(name = "Users CRUD", description = "CRUD operations for users")
    @Operation(summary = "Create a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created"),
            @ApiResponse(responseCode = "409", description = "User already exists")
    })
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        if (userAppService.getUserById(user.getIdentifier()) != null)
            return ResponseEntity.status(409).build();
        userAppService.createUser(user);
        return ResponseEntity.created(null).build();
    }

    @Tag(name = "Users CRUD", description = "CRUD operations for users")
    @Operation(summary = "Update a user", 
            parameters = @Parameter(name = "id", description = "User's id, typically an email", example = "example@essi.upc.edu"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated"),
            @ApiResponse(responseCode = "404", description = "There is no user with that id")
    })
    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User user) {
        if (userAppService.getUserById(id) == null)
            return ResponseEntity.notFound().build();
        userAppService.updateUser(id, user);
        return ResponseEntity.ok().build();
    }

    @Tag(name = "Users CRUD", description = "CRUD operations for users")
    @Operation(summary = "Delete a user",
            parameters = @Parameter(name = "id", description = "User's id, typically an email", example = "example@essi.upc.edu"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted"),
            @ApiResponse(responseCode = "404", description = "There is no user with that id")
    })
    @DeleteMapping("/users/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable String id) {
        if (userAppService.getUserById(id) == null)
            return ResponseEntity.notFound().build();
        userAppService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    //PREFFERED INTEGRATIONS OPERATIONS

    @Tag(name = "User preferences", description = "Operations for managing user preferences")
    @Operation(summary = "Add a preferred app integrations for a particular user",
            description = "Adds a new preference for a (source app, target app) integration pair",
            parameters =  @Parameter(name = "id", description = "User's id, typically an email", example = "example@essi.upc.edu"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Preferred app integration added"),
            @ApiResponse(responseCode = "404", description = "There is no user with that id")
    })
    @PostMapping("/users/{id}/integrations/apps")
    public ResponseEntity<User> addPreferredAppIntegration(@PathVariable String id, @RequestBody AppIntegration appIntegration) {
        if (userAppService.getUserById(id) == null)
            return ResponseEntity.notFound().build();
        userAppService.addPreferredAppIntegration(id, appIntegration);
        return ResponseEntity.created(null).build();
    }

    @Tag(name = "User preferences", description = "Operations for managing user preferences")
    @Operation(summary = "Delete a preferred app integrations for a particular user",
            description = "Removes a previously existing preference for a (source app, target app)",
            parameters =  @Parameter(name = "id", description = "User's id, typically an email", example = "example@essi.upc.edu"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Preferred app integration deleted"),
            @ApiResponse(responseCode = "404", description = "There is no user with that id")
    })
    @DeleteMapping("/users/{id}/integrations/apps")
    public ResponseEntity<User> deletePreferredAppIntegration(@PathVariable String id, @RequestBody AppIntegration appIntegration) {
        if (userAppService.getUserById(id) == null)
            return ResponseEntity.notFound().build();
        userAppService.deletePreferredAppIntegration(id, appIntegration);
        return ResponseEntity.noContent().build();
    }

    @Tag(name = "User preferences", description = "Operations for managing user preferences")
    @Operation(summary = "Add a preferred feature integrations for a particular user",
            description = "Adds a new preference for a (source feature, target feature) integration pair",
            parameters =  @Parameter(name = "id", description = "User's id, typically an email", example = "example@essi.upc.edu"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Preferred feature integration added"),
            @ApiResponse(responseCode = "404", description = "There is no user with that id")
    })
    @PostMapping("/users/{id}/integrations/features")
    public ResponseEntity<User> addPreferredFeatureIntegration(@PathVariable String id, @RequestBody FeatureIntegration featureIntegration) {
        if (userAppService.getUserById(id) == null)
            return ResponseEntity.notFound().build();
        userAppService.addPreferredFeatureIntegration(id, featureIntegration);
        return ResponseEntity.created(null).build();
    }

    @Tag(name = "User preferences", description = "Operations for managing user preferences")
    @Operation(summary = "Delete a preferred feature integrations for a particular user",
            description = "removes a previously existing preference for a (source feature, target feature) integration pair",
            parameters =  @Parameter(name = "id", description = "User's id, typically an email", example = "example@essi.upc.edu"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Preferred feature integration deleted"),
            @ApiResponse(responseCode = "404", description = "There is no user with that id")
    })
    @DeleteMapping("/users/{id}/integrations/features")
    public ResponseEntity<User> deletePreferredFeatureIntegration(@PathVariable String id, @RequestBody FeatureIntegration featureIntegration) {
        if (userAppService.getUserById(id) == null)
            return ResponseEntity.notFound().build();
        userAppService.deletePreferredFeatureIntegration(id, featureIntegration);
        return ResponseEntity.noContent().build();
    }

    @Tag(name = "User preferences", description = "Operations for managing user preferences")
    @Operation(summary = "Add a preferred parameter integrations for a particular user",
            description = "Adds a new preference for a (source parameter, target parameter) integration pair",
            parameters =  @Parameter(name = "id", description = "User's id, typically an email", example = "example@essi.upc.edu"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Preferred parameter integration added"),
            @ApiResponse(responseCode = "404", description = "There is no user with that id")
    })
    @PostMapping("/users/{id}/integrations/parameters")
    public ResponseEntity<User> addPreferredParameterIntegration(@PathVariable String id, @RequestBody ParameterIntegration parameterIntegration) {
        if (userAppService.getUserById(id) == null)
            return ResponseEntity.notFound().build();
        userAppService.addPreferredParameterIntegration(id, parameterIntegration);
        return ResponseEntity.created(null).build();
    }

    @Tag(name = "User preferences", description = "Operations for managing user preferences")
    @Operation(summary = "Delete a preferred parameter integrations for a particular user",
            description = "removes a previously existing preference for a (source parameter, target parameter) integration pair",
            parameters =  @Parameter(name = "id", description = "User's id, typically an email", example = "example@essi.upc.edu"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Preferred parameter integration deleted"),
            @ApiResponse(responseCode = "404", description = "There is no user with that id")
    })
    @DeleteMapping("/users/{id}/integrations/parameters")
    public ResponseEntity<User> deletePreferredParameterIntegration(@PathVariable String id, @RequestBody ParameterIntegration parameterIntegration) {
        if (userAppService.getUserById(id) == null)
            return ResponseEntity.notFound().build();
        userAppService.deletePreferredParameterIntegration(id, parameterIntegration);
        return ResponseEntity.noContent().build();
    }

    //APP OPERATIONS
    @Tag(name = "Apps CRUD", description = "CRUD operations for mobile applications")
    @Operation(summary = "Get all apps")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Apps found")
    })
    @GetMapping("/apps")
    public ResponseEntity<List<App>> getAllApps() {
        return ResponseEntity.ok(userAppService.getAllApps());
    }

    @Tag(name = "Apps CRUD", description = "CRUD operations for mobile applications")
    @Operation(summary = "Get an app by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "App found"),
            @ApiResponse(responseCode = "404", description = "There is no app with that id")
    })
    @GetMapping("/apps/{id}")
    public ResponseEntity<App> getAppById(@PathVariable String id) {
        App app = userAppService.getAppById(id);
        if (app == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(app);
    }

    @Tag(name = "Apps CRUD", description = "CRUD operations for mobile applications")
    @Operation(summary = "Create an app")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "App created"),
            @ApiResponse(responseCode = "409", description = "There is already an app with that id")

    })
    @PostMapping("/apps")
    public ResponseEntity<App> createApp(@RequestBody App app) {
        if (userAppService.getAppById(app.getIdentifier()) != null)
            return ResponseEntity.status(409).build();
        userAppService.createApp(app);
        return ResponseEntity.created(null).build();
    }

    @Tag(name = "Apps CRUD", description = "CRUD operations for mobile applications")
    @Operation(summary = "Update an app")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "App updated"),
            @ApiResponse(responseCode = "404", description = "There is no app with that id")
    })
    @PutMapping("/apps/{id}")
    public ResponseEntity<App> updateApp(@PathVariable String id, @RequestBody App app) {
        if (userAppService.getAppById(id) == null)
            return ResponseEntity.notFound().build();
        userAppService.updateApp(id, app);
        return ResponseEntity.ok().build();
    }

    @Tag(name = "Apps CRUD", description = "CRUD operations for mobile applications")
    @Operation(summary = "Delete an app")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "App deleted"),
            @ApiResponse(responseCode = "404", description = "There is no app with that id")
    })
    @DeleteMapping("/apps/{id}")
    public ResponseEntity<App> deleteApp(@PathVariable String id) {
        if (userAppService.getAppById(id) == null)
            return ResponseEntity.notFound().build();
        userAppService.deleteApp(id);
        return ResponseEntity.noContent().build();
    }

    //APP INTEGRATION OPERATIONS
    @Tag(name = "App integrations CRUD", description = "CRUD operations for app integrations")
    @Operation(summary = "Get all app integrations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "App integrations found")
    })
    @GetMapping("/apps/integrations")
    public ResponseEntity<List<AppIntegration>> getAllAppsIntegrations() {
        return ResponseEntity.ok(userAppService.getAllAppIntegrations());
    }

    @Tag(name = "App integrations CRUD", description = "CRUD operations for app integrations")
    @Operation(summary = "Get an app integration by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "App integration found"),
            @ApiResponse(responseCode = "404", description = "There is no app integration with that id")
    })
    @GetMapping("/apps/integrations/{id}")
    public ResponseEntity<AppIntegration> getAppIntegrationById(@PathVariable String id) {
        AppIntegration app = userAppService.getAppIntegrationById(id);
        if (app == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(app);
    }

    @Tag(name = "App integrations CRUD", description = "CRUD operations for app integrations")
    @Operation(summary = "Create an app integration")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "App integration created"),
            @ApiResponse(responseCode = "409", description = "There is already an app integration with that id")

    })
    @PostMapping("/apps/integrations")
    public ResponseEntity<AppIntegration> createAppIntegration(@RequestBody AppIntegration app) {
        app.setIdentifier(app.getSourceApp() + "-" + app.getTargetApp());
        if (userAppService.getAppIntegrationById(app.getIdentifier()) != null)
            return ResponseEntity.status(409).build();
        userAppService.createAppIntegration(app);
        return ResponseEntity.created(null).build();
    }

    @Tag(name = "App integrations CRUD", description = "CRUD operations for app integrations")
    @Operation(summary = "Update an app integration")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "App integration updated"),
            @ApiResponse(responseCode = "404", description = "There is no app integration with that id")
    })
    @PutMapping("/apps/integrations/{id}")
    public ResponseEntity<AppIntegration> updateAppIntegration(@PathVariable String id, @RequestBody AppIntegration app) {
        if (userAppService.getAppIntegrationById(id) == null)
            return ResponseEntity.notFound().build();
        userAppService.updateAppIntegration(id, app);
        return ResponseEntity.ok().build();
    }

    @Tag(name = "App integrations CRUD", description = "CRUD operations for app integrations")
    @Operation(summary = "Delete an app integration")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "App integration deleted"),
            @ApiResponse(responseCode = "404", description = "There is no app integration with that id")
    })
    @DeleteMapping("/apps/integrations/{id}")
    public ResponseEntity<AppIntegration> deleteAppIntegration(@PathVariable String id) {
        if (userAppService.getAppIntegrationById(id) == null)
            return ResponseEntity.notFound().build();
        userAppService.deleteAppIntegration(id);
        return ResponseEntity.noContent().build();
    }

    //USER STORY #1
    @Tag(name = "User stories", description = "User stories operations")
    @Operation(summary = "USER STORY #1: Request feature integrations from source features and previous user preferences")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Feature integrations' target features retrieved"),
            @ApiResponse(responseCode = "404", description = "There is no user with that id")
    })
    @GetMapping("/users/{id}/integrations/features/{sourceFeature}")
    public ResponseEntity<List<String>> requestFeatureIntegration(@PathVariable String id, @PathVariable String sourceFeature) {
        if (userAppService.getUserById(id) == null)
            return ResponseEntity.notFound().build();
        List<String> features = userAppService.requestFeatureIntegration(id, sourceFeature);
        if (features == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(features);
    }
  
    //USER STORY #2
    @Tag(name = "User stories", description = "User stories operations")
    @Operation(summary = "USER STORY #2: Request app integrations from selected target feature and previous user preferences") 
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "App integrations' target apps retrieved"),
            @ApiResponse(responseCode = "404", description = "There is no user with that id")
    })
    @GetMapping("/users/{id}/integrations/apps/feature/{sourceFeature}/{targetFeature}")
    public ResponseEntity<List<String>> getAppsFromFeature(@PathVariable String id, @PathVariable String sourceFeature, @PathVariable String targetFeature) {
        if (userAppService.getUserById(id) == null)
            return ResponseEntity.notFound().build();
        List<String> apps = userAppService.getAppsFromFeature(id, sourceFeature, targetFeature);
        if (apps == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(apps);
    }
}
