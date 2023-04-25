package upc.edu.gessi.tfg.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upc.edu.gessi.tfg.models.User;
import upc.edu.gessi.tfg.repositories.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }
    
    public User getUserById(long id) {
        return userRepository.getUser(id);
    }

    public void createUser(User user) {
        userRepository.createUser(user);
    }

    public void updateUser(long id, User user) {
        userRepository.updateUser(id, user);
    }

    public void deleteUser(long id) {
        userRepository.deleteUser(id);
    }
}
