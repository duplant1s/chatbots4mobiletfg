package upc.edu.gessi.tfg.repositories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Value;
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
import upc.edu.gessi.tfg.utils.IRIS;

@org.springframework.stereotype.Repository
public class FeatureRepository {

    private String repoURL = "http://localhost:7200/repositories/Chatbots4MobileTFG";
    private Repository repository;

    private ParameterRepository parameterRepository = new ParameterRepository();

    public FeatureRepository() {
        this.repository = new HTTPRepository(repoURL);
        this.repository.init();
    }

    public List<Feature> getAllFeatures() {
        List<Feature> features = new ArrayList<Feature>();

        try (RepositoryConnection connection = repository.getConnection()) {
            String queryString = "PREFIX schema: <https://schema.org/>\n" +
            "SELECT ?id ?name ?parameter WHERE {\n" +
                "?feature a schema:DefinedTerm ;" +   
                " schema:identifier ?id ;" +
                " schema:name ?name . " +
                " OPTIONAL { ?feature schema:hasPart ?parameter } " +
            "}";

            TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
            TupleQueryResult result = tupleQuery.evaluate();
            Map<String, Feature> featureMap = new HashMap<>();
            while (result.hasNext()) {
                BindingSet bindingSet = result.next();
                String id = bindingSet.getValue("id").stringValue();
                String name = bindingSet.getValue("name").stringValue();
                Value parameter = bindingSet.getValue("parameter");
                Feature feature;
                if (featureMap.containsKey(name)) {
                    feature = featureMap.get(name);
                    if (parameter != null && !feature.getParameters().contains(parameter.toString()))
                        feature.getParameters().add(parameter.toString());
                } else {
                    if (parameter != null) {
                        feature = new Feature(id, name, new ArrayList<String>(Arrays.asList(parameter.toString())));
                    } else {
                        feature = new Feature(id, name, new ArrayList<String>());
                    }
                    featureMap.put(name, feature);
                }
            }

            features.addAll(featureMap.values());
            
        } finally {
            repository.shutDown();
        }

        return features;
    }

    public Feature getFeature(String id) {
        Feature feature = null;
        try (RepositoryConnection connection = repository.getConnection()) {
            String queryString = "PREFIX schema: <https://schema.org/>\n" +
            "SELECT ?name ?parameter WHERE { \n"+
                "?feature a schema:DefinedTerm ;"+ 
                "       schema:identifier '"+id+"' ;"+ 
                "       schema:name ?name ;" + 
                "OPTIONAL { ?feature schema:hasPart ?parameter } }";

            TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
            TupleQueryResult result = tupleQuery.evaluate();

            List<String> parameters = new ArrayList<String>();
            while (result.hasNext()) {
                BindingSet bindingSet = result.next();
                String name = bindingSet.getValue("name").stringValue();
                Value parameter = bindingSet.getValue("parameter");
                if (parameter != null) {
                    if (!parameters.contains(parameter.toString()))
                        parameters.add(parameter.toString());
                }
                feature = new Feature(id, name, parameters);
            }
        } finally {
            repository.shutDown();
        }

        return feature;
    }

    public void createFeature(Feature feature) {
        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.setNamespace("schema", "https://schema.org/");
        modelBuilder.subject("schema:DefinedTerm/"+feature.getIdentifier())
            .add(RDF.TYPE, IRIS.feature)
            .add(IRIS.identifier, IRIS.createLiteral(feature.getIdentifier()))
            .add(IRIS.name, IRIS.createLiteral(feature.getName()));
        Iterator<String> it = feature.getParameters().iterator();
        while (it.hasNext()) {
            String parameter = it.next();
            String paramType = parameterRepository.getParameter(parameter).getType().toString();
            modelBuilder.add(IRIS.hasPart, IRIS.createCustomIRI("https://schema.org/"+paramType+"/"+parameter));
        }
        
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
            StringBuilder queryString = new StringBuilder();
            queryString.append("PREFIX schema: <https://schema.org/>\n");
            queryString.append("DELETE { \n");
            queryString.append("?feature schema:name ?name ; \n");
            queryString.append("         schema:hasPart ?parameter . \n");
            queryString.append("} \n");
            queryString.append("INSERT { \n");
            queryString.append("?feature schema:name '"+updatedFeature.getName()+"' ; \n");
            if (updatedFeature.getParameters() != null) {
                Iterator<String> it = updatedFeature.getParameters().iterator();
                while (it.hasNext()) {
                    String parameter = it.next();
                    String paramType = parameterRepository.getParameter(parameter).getType().toString();
                    queryString.append("         schema:hasPart <https://schema.org/"+paramType+"/"+parameter+"> ; \n");
                }
            }
            queryString.append("} \n");
            queryString.append("WHERE { \n");
            queryString.append("?feature a schema:DefinedTerm ; \n");
            queryString.append("         schema:identifier '"+id+"' ; \n");
            queryString.append("         schema:name ?name . \n");
            queryString.append("OPTIONAL{?feature schema:hasPart ?parameter} \n");
            queryString.append("} \n");

            Update update = connection.prepareUpdate(QueryLanguage.SPARQL, queryString.toString());
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
            "DELETE WHERE { ?feature a schema:DefinedTerm ; schema:name '%s' ; ?p ?o }", id);
           
            Update update = connection.prepareUpdate(QueryLanguage.SPARQL, queryString);
            update.execute();

        } catch(Exception e) {
            System.out.println(e.getMessage());
        } finally {
            repository.shutDown();
        }

    }
}
