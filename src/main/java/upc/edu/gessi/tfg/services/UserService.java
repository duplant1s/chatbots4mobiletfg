package upc.edu.gessi.tfg.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upc.edu.gessi.tfg.models.User;
import upc.edu.gessi.tfg.repositories.GraphDBService;

@Service
public class UserService {
    
    @Autowired
    private GraphDBService graphDBService;

    public User getUserById(long id) {
        return graphDBService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    public User createUser(User user) {
        return graphDBService.save(user);
    }

    public User updateUser(long id, User user) {
        User existingUser = graphDBService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        return graphDBService.save(existingUser);
    }

    public User deleteUser(long id) {
        User existingUser = graphDBService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        graphDBService.delete(existingUser);
        return existingUser;
    }
}
