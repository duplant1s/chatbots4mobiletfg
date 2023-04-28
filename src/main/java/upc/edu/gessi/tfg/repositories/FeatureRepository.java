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
import upc.edu.gessi.tfg.models.Feature;

@org.springframework.stereotype.Repository
public class FeatureRepository {

    private String repoURL = "http://localhost:7200/repositories/Chatbots4MobileTFG";
    private Repository repository;

    private final SimpleValueFactory vf = SimpleValueFactory.getInstance();

    //Features IRIs
    private final IRI schemaFeatureClassIRI = vf.createIRI("https://schema.org/DefinedTerm");
    private final IRI identifierPropertyIRI = vf.createIRI("https://schema.org/identifier");
    private final IRI namePropertyIRI = vf.createIRI("https://schema.org/name");

    public FeatureRepository() {
        this.repository = new HTTPRepository(repoURL);
        this.repository.init();
    }

    public List<Feature> getAllFeatures() {
        List<Feature> features = new ArrayList<Feature>();

        try (RepositoryConnection connection = repository.getConnection()) {
            String queryString = "PREFIX schema: <https://schema.org/>\n" +
            "SELECT ?id ?name"+
            "WHERE { \n"+
                "?feature a schema:DefinedTerm ."+ 
                "?feature schema:identifier ?id ."+ 
                "?feature schema:name ?name"
            +"}";

            TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
            TupleQueryResult result = tupleQuery.evaluate();

            while (result.hasNext()) {
                BindingSet bindingSet = result.next();
                String id = bindingSet.getValue("id").stringValue();
                String name = bindingSet.getValue("name").stringValue();
                Feature feature = new Feature(id, name);
                features.add(feature);
            }
        } finally {
            repository.shutDown();
        }

        return features;
    }

    public Feature getFeature(String id) {
        Feature feature = null;
        try (RepositoryConnection connection = repository.getConnection()) {
            String queryString = String.format("PREFIX schema: <https://schema.org/>\n" +
            "SELECT ?name"+
            "WHERE { \n"+
                "?feature a schema:DefinedTerm ."+ 
                "?feature schema:identifier '%s' ."+ 
                "?feature schema:name ?name }", id);

            TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
            TupleQueryResult result = tupleQuery.evaluate();

            while (result.hasNext()) {
                BindingSet bindingSet = result.next();
                String name = bindingSet.getValue("name").stringValue();
                feature = new Feature(id, name);
            }
        } finally {
            repository.shutDown();
        }

        return feature;
    }

    public void createFeature(Feature feature) {
        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.setNamespace("schema", "https://schema.org/");
        modelBuilder.subject("schema:DefinedTerm/"+feature.getId())
            .add(RDF.TYPE, schemaFeatureClassIRI)
            .add(identifierPropertyIRI, vf.createLiteral(feature.getId()))
            .add(namePropertyIRI, vf.createLiteral(feature.getName()));
        
        Model model = modelBuilder.build();
        try (RepositoryConnection connection = repository.getConnection()) {
            connection.add(model);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            repository.shutDown();
        }
    }

    public void updateFeature(String id, Feature updatedFeature) {
        try (RepositoryConnection connection = repository.getConnection()) {
            String queryString = String.format("PREFIX schema: <https://schema.org/>\n" +
            "DELETE { ?feature schema:name ?name }" +
            "INSERT { ?feature schema:name \"%s\" }" +
            "WHERE { ?feature schema:identifier \"%s\" . ?feature schema:name ?name }", updatedFeature.getName(), id);

            Update update = connection.prepareUpdate(QueryLanguage.SPARQL, queryString);
            update.execute();
        } 
        catch(Exception e) {
            System.out.println(e.getMessage());
        } finally {
            repository.shutDown();
        }
    }

    public void deleteFeature(String id) {
        try(RepositoryConnection connection = repository.getConnection()) {
            String queryString = String.format("PREFIX schema: <https://schema.org/>\n" +
            "DELETE WHERE { ?feature a schema:DefinedTerm ; schema:identifier '%s' ; ?p ?o }", id);
           
            Update update = connection.prepareUpdate(QueryLanguage.SPARQL, queryString);
            update.execute();

        } catch(Exception e) {
            System.out.println(e.getMessage());
        } finally {
            repository.shutDown();
        }

    }
}
