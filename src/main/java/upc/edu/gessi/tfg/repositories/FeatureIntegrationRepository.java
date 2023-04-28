package upc.edu.gessi.tfg.repositories;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.query.Update;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import upc.edu.gessi.tfg.models.FeatureIntegration;

@org.springframework.stereotype.Repository
public class FeatureIntegrationRepository{
    
    private String repoURL = "http://localhost:7200/repositories/Chatbots4MobileTFG";
    private Repository repository;

    private final SimpleValueFactory vf = SimpleValueFactory.getInstance();

    //Feature Integrations IRIs
    private final IRI schemaFeatureIntegrationClassIRI = vf.createIRI("https://schema.org/Action");
    private final IRI identifierPropertyIRI = vf.createIRI("https://schema.org/identifier");
    private final IRI namePropertyIRI = vf.createIRI("https://schema.org/name");
    private final IRI sourcePropertyIRI = vf.createIRI("https://schema.org/source");
    private final IRI targetPropertyIRI = vf.createIRI("https://schema.org/target");

    //Feature IRIs
    private final IRI featureIRI = vf.createIRI("https://schema.org/DefinedTerm");

    public FeatureIntegrationRepository() {
        this.repository = new HTTPRepository(repoURL);
        this.repository.init();
    }

    public List<FeatureIntegration> getAllFeatureIntegrations() {
        List<FeatureIntegration> featureIntegrations = new ArrayList<FeatureIntegration>();

        try (RepositoryConnection connection = repository.getConnection()) {
            String queryString = "PREFIX schema: <https://schema.org/>\n" +
            "SELECT ?id ?name ?source ?target"+
            "WHERE { \n"+
                "?featureIntegration a schema:Action ."+ 
                "?featureIntegration schema:identifier ?id ."+ 
                "?featureIntegration schema:name ?name ."+
                "?featureIntegration schema:source ?source ."+
                "?featureIntegration schema:target ?target"+
            "}";

            TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
            TupleQueryResult result = tupleQuery.evaluate();

            while(result.hasNext()) {
                BindingSet bindingSet = result.next();
                String id = bindingSet.getValue("id").stringValue();
                String name = bindingSet.getValue("name").stringValue();
                String source = bindingSet.getValue("source").stringValue();
                String target = bindingSet.getValue("target").stringValue();
                FeatureIntegration featureIntegration = new FeatureIntegration(id, name, source, target);
                featureIntegrations.add(featureIntegration);
            }
        } finally {
            repository.shutDown();
        }

        return featureIntegrations;
    }

    public FeatureIntegration getFeatureIntegration(String id) {
        FeatureIntegration featureIntegration = null;

        try (RepositoryConnection connection = repository.getConnection()) {
            String queryString = String.format("PREFIX schema: <https://schema.org/>\n" +
            "SELECT ?id ?name ?source ?target"+
            "WHERE { \n"+
                "?featureIntegration a schema:Action ."+ 
                "?featureIntegration schema:identifier '%s' ."+ 
                "?featureIntegration schema:name ?name ."+
                "?featureIntegration schema:source ?source ."+
                "?featureIntegration schema:target ?target"+
            "}", id);

            TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
            TupleQueryResult result = tupleQuery.evaluate();

            while(result.hasNext()) {
                BindingSet bindingSet = result.next();
                String name = bindingSet.getValue("name").stringValue();
                String source = bindingSet.getValue("source").stringValue();
                String target = bindingSet.getValue("target").stringValue();
                featureIntegration = new FeatureIntegration(id, name, source, target);
            }
        } finally {
            repository.shutDown();
        }

        return featureIntegration;
    }

    public void createFeatureIntegration(FeatureIntegration featureIntegration) {
        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.setNamespace("schema", "https://schema.org/");
        modelBuilder.subject("schema:Action/"+featureIntegration.getId())
            .add(RDF.TYPE, schemaFeatureIntegrationClassIRI)
            .add(identifierPropertyIRI, featureIntegration.getId())
            .add(namePropertyIRI, featureIntegration.getName())
            .add(sourcePropertyIRI, vf.createIRI(featureIRI+"/"+featureIntegration.getSourceFeature()))
            .add(targetPropertyIRI, vf.createIRI(featureIRI+"/"+featureIntegration.getTargetFeature()));
        
        Model model = modelBuilder.build();

        try(RepositoryConnection connection = repository.getConnection()) {
            connection.add(model);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        } finally {
            repository.shutDown();
        }
    }

    public void updateFeatureIntegration(String id, FeatureIntegration updatedFeatureIntegration) {
        try(RepositoryConnection connection = repository.getConnection()) {
            String queryString = String.format("PREFIX schema: <https://schema.org/>\n" +
            "DELETE { \n"+
                "?featureIntegration schema:name ?name ."+
                "?featureIntegration schema:source ?source ."+
                "?featureIntegration schema:target ?target"+
            "}\n"+
            "INSERT { \n"+
                "?featureIntegration schema:name '%s' ."+
                "?featureIntegration schema:source '%s' ."+
                "?featureIntegration schema:target '%s'"+
            "}\n"+
            "WHERE { \n"+
                "?featureIntegration a schema:Action ."+ 
                "?featureIntegration schema:identifier '%s' ."+ 
                "?featureIntegration schema:name ?name ."+
                "?featureIntegration schema:source ?source ."+
                "?featureIntegration schema:target ?target"+
            "}", updatedFeatureIntegration.getName(), updatedFeatureIntegration.getSourceFeature(), updatedFeatureIntegration.getTargetFeature(), id);

            Update update = connection.prepareUpdate(QueryLanguage.SPARQL, queryString);
            update.execute();
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        } finally {
            repository.shutDown();
        }
    }

    public void deleteFeatureIntegration(String id) {
        try(RepositoryConnection connection = repository.getConnection()) {
            connection.remove(vf.createIRI("https://schema.org/Action/"+id), null, null);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        } finally {
            repository.shutDown();
        }
    }
}
