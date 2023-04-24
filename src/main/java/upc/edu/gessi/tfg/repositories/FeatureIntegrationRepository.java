package upc.edu.gessi.tfg.repositories;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import upc.edu.gessi.tfg.models.FeatureIntegration;

public interface FeatureIntegrationRepository extends Neo4jRepository<FeatureIntegration, Long>{
    
}
