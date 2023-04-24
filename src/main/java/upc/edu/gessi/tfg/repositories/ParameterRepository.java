package upc.edu.gessi.tfg.repositories;

import upc.edu.gessi.tfg.models.Parameter;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface ParameterRepository extends Neo4jRepository<Parameter, Long> {
    
}
