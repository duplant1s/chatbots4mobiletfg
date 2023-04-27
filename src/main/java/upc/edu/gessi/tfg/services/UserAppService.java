package upc.edu.gessi.tfg.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upc.edu.gessi.tfg.models.App;
import upc.edu.gessi.tfg.models.User;
import upc.edu.gessi.tfg.repositories.AppRepository;
import upc.edu.gessi.tfg.repositories.UserRepository;

@Service
public class UserAppService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AppRepository appRepository;

    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }
    
    public User getUserById(String id) {
        return userRepository.getUser(id);
    }

    public void createUser(User user) {
        userRepository.createUser(user);
    }

    public void updateUser(String id, User user) {
        if (user.getIdentifier() == null)
            user.setIdentifier(id);
        userRepository.updateUser(id, user);
    }

    public void deleteUser(String id) {
        userRepository.deleteUser(id);
    }

    public List<App> getAllApps() {
        return appRepository.getAllApps();
    }

    public App getAppById(String id) {
        return appRepository.getApp(id);
    }

    public void createApp(App app) {
        appRepository.createApp(app);
    }

    public void updateApp(String id, App app) {
        if (app.getIdentifier() == null)
            app.setIdentifier(id);
        appRepository.updateApp(id, app);
    }

    public void deleteApp(String id) {
        appRepository.deleteApp(id);
    }
}
