package upc.edu.gessi.tfg.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import upc.edu.gessi.tfg.models.User;
import upc.edu.gessi.tfg.models.App;
import upc.edu.gessi.tfg.services.UserAppService;

@RestController
public class UserAppController {

    @Autowired
    private UserAppService userAppService;

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

    @PostMapping
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
}
