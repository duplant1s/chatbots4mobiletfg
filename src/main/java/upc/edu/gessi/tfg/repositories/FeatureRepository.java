package upc.edu.gessi.tfg.repositories;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import upc.edu.gessi.tfg.models.Feature;

public interface FeatureRepository extends Neo4jRepository<Feature, Long>{
    
}
