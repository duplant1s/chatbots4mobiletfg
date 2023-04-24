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

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public Optional<User> getUserById(long id) {
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(long id, User user) {
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser == null)
            return null;
        existingUser.setGivenName(user.getGivenName());
        existingUser.setFamilyName(user.getFamilyName());
        existingUser.setEmail(user.getEmail());
        return userRepository.save(existingUser);
    }

    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }
}
