package upc.edu.gessi.tfg.repositories;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.repository.Repository;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import upc.edu.gessi.tfg.models.ParameterIntegration;

@org.springframework.stereotype.Repository
public class ParameterIntegrationRepository {
    private String repoURL = "http://localhost:7200/repositories/Chatbots4MobileTFG";
    private Repository repository;

    private final SimpleValueFactory vf = SimpleValueFactory.getInstance();
    
    //PARAMETER INTEGRATION IRIs
    private final IRI schemaParameterIntegrationClassIRI = vf.createIRI("https://schema.org/PropertyValue");
    private final IRI identifierPropertyIRI = vf.createIRI("https://schema.org/identifier");
    private final IRI sourcePropertyIRI = vf.createIRI("https://schema.org/name");
    private final IRI targetPropertyIRI = vf.createIRI("https:schema.org/value");


}
