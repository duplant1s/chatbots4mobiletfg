package upc.edu.gessi.tfg.repositories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

    private ParameterRepository parameterRepository = new ParameterRepository();

    private final SimpleValueFactory vf = SimpleValueFactory.getInstance();

    //Features IRIs
    private final IRI schemaFeatureClassIRI = vf.createIRI("https://schema.org/DefinedTerm");
    private final IRI identifierPropertyIRI = vf.createIRI("https://schema.org/identifier");
    private final IRI namePropertyIRI = vf.createIRI("https://schema.org/name");
    private final IRI hasPartPropertyIRI = vf.createIRI("https://schema.org/hasPart");

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
                " schema:name ?name ; " +
                " schema:hasPart ?parameter . " +
            "}";

            TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
            TupleQueryResult result = tupleQuery.evaluate();
            Map<String, Feature> featureMap = new HashMap<>();
            while (result.hasNext()) {
                BindingSet bindingSet = result.next();
                String name = bindingSet.getValue("name").stringValue();
                String parameter = bindingSet.getValue("parameter").stringValue();
                Feature feature;
                if (featureMap.containsKey(name)) {
                    feature = featureMap.get(name);
                    feature.getParameters().add(parameter);
                } else {
                    feature = new Feature(name, name, new ArrayList<String>(Arrays.asList(parameter)));
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
            String queryString = String.format("PREFIX schema: <https://schema.org/>\n" +
            "SELECT ?name ?parameter WHERE { \n"+
                "<https://schema.org/DefinedTerm/%s> a schema:DefinedTerm ;"+ 
                //"       schema:identifier %s' ;"+ 
                "       schema:name ?name ;" + 
                "       schema:hasPart ?parameter }", id);

            TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
            TupleQueryResult result = tupleQuery.evaluate();

            List<String> parameters = new ArrayList<String>();
            while (result.hasNext()) {
                BindingSet bindingSet = result.next();
                String name = bindingSet.getValue("name").stringValue();
                String parameter = bindingSet.getValue("parameter").stringValue();
                parameters.add(parameter);
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
        modelBuilder.subject("schema:DefinedTerm/"+feature.getId())
            .add(RDF.TYPE, schemaFeatureClassIRI)
            //.add(identifierPropertyIRI, vf.createLiteral(feature.getId()))
            .add(namePropertyIRI, vf.createLiteral(feature.getName()));
        Iterator<String> it = feature.getParameters().iterator();
        while (it.hasNext()) {
            String parameter = it.next();
            String paramType = parameterRepository.getParameter(parameter).getType().toString();
            modelBuilder.add(hasPartPropertyIRI, vf.createIRI("https://schema.org/"+paramType+"/"+parameter));
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



    //TODOOOOOOOOOOOOOOOOOOOOOOO
    public void updateFeature(String id, Feature updatedFeature) {
        try (RepositoryConnection connection = repository.getConnection()) {
            String queryString = String.format("PREFIX schema: <https://schema.org/>\n" +
            "DELETE { ?feature schema:name ?name }" +
            "INSERT { ?feature schema:name '%s' }" +
            "WHERE { <https://schema.org/%s> a schema:DefinedTerm . ?feature schema:name ?name }", updatedFeature.getName(), id);

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
            "DELETE WHERE { ?feature a schema:DefinedTerm ; schema:name '%s' ; ?p ?o }", id);
           
            Update update = connection.prepareUpdate(QueryLanguage.SPARQL, queryString);
            update.execute();

        } catch(Exception e) {
            System.out.println(e.getMessage());
        } finally {
            repository.shutDown();
        }

    }

    // Story #2 - target app selection
    //     Request app integrations:
    //     description: request app integrations from selected target feature and previous user preferences
    //     input: selected target feature (story #1)
    //     expected output: app list ordered by user preferences of potential integrations from selected target feature with expected parameters and integration from source app

    public List<String> getAppsFromFeature(String feature) {
        List <String> apps = new ArrayList<String>();
        try (RepositoryConnection connection = repository.getConnection()) {
            String queryString = String.format("PREFIX schema: <https://schema.org/>\n"+
            "SELECT ?appName WHERE {"+
                "?app a schema:MobileApplication ."+
                "?app schema:name ?appName ."+
                "?app schema:keywords ?feature ."+
                "    ?feature a schema:DefinedTerm;"+
                "       schema:name '%s'"+
            "}", feature);

            TupleQuery tupleQuery = connection.prepareTupleQuery(queryString);
            TupleQueryResult result = tupleQuery.evaluate();

            while (result.hasNext()) {
                BindingSet bindingSet = result.next();
                String appName = bindingSet.getValue("appName").stringValue();
                apps.add(appName);
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            repository.shutDown();
        }
        return apps;
    }
}
