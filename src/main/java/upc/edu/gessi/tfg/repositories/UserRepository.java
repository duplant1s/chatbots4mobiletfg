package upc.edu.gessi.tfg.repositories;

import upc.edu.gessi.tfg.models.User;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface UserRepository extends Neo4jRepository<User, Long> {
    
}
