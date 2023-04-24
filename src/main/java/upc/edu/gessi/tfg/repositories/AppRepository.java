package upc.edu.gessi.tfg.repositories;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import upc.edu.gessi.tfg.models.App;

public interface AppRepository extends Neo4jRepository<App, Long> {
    
}
