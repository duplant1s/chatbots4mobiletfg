package upc.edu.gessi.tfg.repositories;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.rdf4j.model.Model;
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
import upc.edu.gessi.tfg.models.FeatureIntegration;
import upc.edu.gessi.tfg.utils.IRIS;

@org.springframework.stereotype.Repository
public class FeatureIntegrationRepository{
    
    private String repoURL = "http://localhost:7200/repositories/Chatbots4MobileTFG";
    private Repository repository;

    public FeatureIntegrationRepository() {
        this.repository = new HTTPRepository(repoURL);
        this.repository.init();
    }

    public List<FeatureIntegration> getAllFeatureIntegrations() {
        List<FeatureIntegration> featureIntegrations = new ArrayList<FeatureIntegration>();

        try (RepositoryConnection connection = repository.getConnection()) {
            String queryString = "PREFIX schema: <https://schema.org/>\n" +
            "SELECT ?identifier ?name ?source ?target "+
            "WHERE { \n"+
                "?featureIntegration a schema:Action ."+ 
                "?featureIntegration schema:identifier ?identifier ."+
                "?featureIntegration schema:name ?name ."+
                "?featureIntegration schema:source ?source ."+
                "?featureIntegration schema:target ?target"+
            "}";

            TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
            TupleQueryResult result = tupleQuery.evaluate();

            while(result.hasNext()) {
                BindingSet bindingSet = result.next();
                String id = bindingSet.getValue("identifier").stringValue();
                String name = bindingSet.getValue("name").stringValue();
                String source = bindingSet.getValue("source").stringValue();
                String target = bindingSet.getValue("target").stringValue();
                FeatureIntegration featureIntegration = new FeatureIntegration(source, target);
                featureIntegration.setIdentifier(id);
                featureIntegration.setName(name);
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
            "SELECT ?id ?name ?source ?target "+
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
                featureIntegration = new FeatureIntegration(source, target);
                featureIntegration.setIdentifier(id);
                featureIntegration.setName(name);
            }
        } finally {
            repository.shutDown();
        }

        return featureIntegration;
    }

    public void createFeatureIntegration(FeatureIntegration featureIntegration) {
        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.setNamespace("schema", IRIS.root);
        modelBuilder.subject("schema:Action/"+featureIntegration.getSourceFeature()+"-"+featureIntegration.getTargetFeature())
            .add(RDF.TYPE, IRIS.featureIntegration)
            .add(IRIS.identifier, featureIntegration.getIdentifier())
            .add(IRIS.name, featureIntegration.getName())
            .add(IRIS.source, IRIS.createCustomIRI(IRIS.feature+"/"+featureIntegration.getSourceFeature()))
            .add(IRIS.target, IRIS.createCustomIRI(IRIS.feature+"/"+featureIntegration.getTargetFeature()));
        
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
            "}\n"+
            "INSERT { \n"+
                "?featureIntegration schema:name '%s' ."+
            "}\n"+
            "WHERE { \n"+
                "?featureIntegration a schema:Action ."+ 
                "?featureIntegration schema:identifier '%s' ."+ 
                "?featureIntegration schema:name ?name ."+

            "}",  updatedFeatureIntegration.getName(), id);

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
            connection.remove(IRIS.createCustomIRI("https://schema.org/Action/"+id), null, null);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        } finally {
            repository.shutDown();
        }
    }

    ///////////////////////////////////////////////////////////////

    // Story #1 - target feature selection
    //     Request feature integrations:
    //     description: request feature integrations from source features and previous user preferences
    //     input: source feature
    //     expected output: feature list ordered by user preferences of potential integrations from source feature

    //     Example:
    //     request_feature_integrations(“plan_route”) → [“create a task”, “schedule event”, “send an email”]
    //     PRIORITY
    //     1. user preferred-integrations
    //     2. features from user's preferred app integrations
    //     3. else 

    public List<String> requestIntegrationsTargetFeatures(String userID, String sourceFeature) {
        List<String> sortedTargetFeatures = new ArrayList<String>();
        Map<String, Integer> targetFeaturesMap = new HashMap<String, Integer>();

        try (RepositoryConnection connection = repository.getConnection()) {

            //Firstly, we check for the user's preferred integrations, 
            //assigning them the highest priority of 3.
            String queryString = String.format("PREFIX schema: <https://schema.org/>\n" +
            "SELECT ?targetName "+
            "WHERE {\n"+
                "?user a schema:Person ."+ 
                "?user schema:identifier '%s' ."+ 
                "?user schema:Action ?preferredIntegration ."+ 
                "?preferredIntegration a schema:Action ."+ 
                "?preferredIntegration schema:source ?sourceFeature ."+ 
                "?sourceFeature schema:identifier '%s' ."+ 
                "?preferredIntegration schema:target ?targetFeature ."+ 
                "?targetFeature schema:identifier ?targetName ."+
            "}", userID, sourceFeature);
            
            TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
            TupleQueryResult result = tupleQuery.evaluate();

            while(result.hasNext()) {
                BindingSet bindingSet = result.next();
                String targetName = bindingSet.getValue("targetName").stringValue();
                targetFeaturesMap.put(targetName, 3);
            }

            //Secondly, we check for the user's preferred apps integrations, considering 
            //that the source app has to have the sourceFeature as a feature.
            //We will assign this a value of 2.
            queryString = String.format("PREFIX schema: <https://schema.org/>\n" +
            "SELECT ?targetName "+
            "WHERE {\n"+
                "?user a schema:Person ."+
                "?user schema:identifier '%s' ."+
                "?user schema:AppIntegration ?appIntegration ."+
                "?appIntegration a schema:AppIntegration ."+
                "?appIntegration schema:source ?sourceApp ."+
                "?sourceApp a schema:MobileApplication ."+
                "?sourceApp schema:keywords ?sourceFeature ."+
                "?sourceFeature schema:identifier '%s' ."+
                "?appIntegration schema:target ?targetApp ."+
                "?targetApp a schema:MobileApplication ."+
                "?targetApp schema:keywords ?targetFeature ."+
                "?targetFeature schema:identifier ?targetName ."+
            "}", userID, sourceFeature);

            tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
            result = tupleQuery.evaluate();

            while(result.hasNext()) {
                BindingSet bindingSet = result.next();
                String target = bindingSet.getValue("targetName").stringValue();
                //the target obtained is a reference to the feature's IRI, so we need to get the feature name only to obtain its properties
                //target = target.substring(target.lastIndexOf('/') + 1);
                targetFeaturesMap.put(target, targetFeaturesMap.get(target) + 2);
            }

            //Lastly, we obtain all the target features that are part of a feature integration
            //with the source feature. We will assign this a value of 1.
            queryString = String.format("PREFIX schema: <https://schema.org/>\n" +
            "SELECT ?targetName "+
            "WHERE { \n"+
                "?featureIntegration a schema:Action ."+ 
                "?featureIntegration schema:source ?source ."+
                "?source a schema:DefinedTerm;"+
                    "schema:identifier '%s' ."+
                "?featureIntegration schema:target ?target."+
                "?target a schema:DefinedTerm;"+
                    "schema:identifier ?targetName ."+
            "}", sourceFeature);

            tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
            result = tupleQuery.evaluate();

            while(result.hasNext()) {
                BindingSet bindingSet = result.next();
                String target = bindingSet.getValue("targetName").stringValue();
                //the target obtained is a reference to the feature's IRI, so we need to get the feature name only to obtain its properties
                //target = target.substring(target.lastIndexOf('/') + 1);
                targetFeaturesMap.put(target, targetFeaturesMap.get(target) + 1);
            }

            //We sort the target features by their priority
            List<Map.Entry<String, Integer>> list = new ArrayList<>(targetFeaturesMap.entrySet());

            list.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

            for (Map.Entry<String, Integer> entry : list) {
                sortedTargetFeatures.add(entry.getKey());
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            repository.shutDown();
        }

        return sortedTargetFeatures;
    }


    //USER PREFERENCES
    public void addPreferredFeatureIntegration(String user, FeatureIntegration featureIntegration) {
        if (getFeatureIntegration(featureIntegration.getIdentifier()) == null) {
            createFeatureIntegration(featureIntegration);
        }

        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.setNamespace("schema", IRIS.root);
        modelBuilder.subject("schema:Person/"+user)
            .add(IRIS.featureIntegration, IRIS.createCustomIRI(IRIS.featureIntegration+"/"+featureIntegration.getIdentifier()));

        Model model = modelBuilder.build();

        try(RepositoryConnection connection = repository.getConnection()) {
            connection.add(model);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        } finally {
            repository.shutDown();
        }
    }

    public void removePreferredFeatureIntegration(String user, FeatureIntegration featureIntegration){
        try (RepositoryConnection connection = repository.getConnection()) {
            String queryString = String.format("PREFIX schema: <https://schema.org/>\n" +
            "DELETE { \n"+
                "?user schema:Action ?featureIntegration ."+
            "}\n"+
            "WHERE { \n"+
                "?user a schema:Person ."+ 
                "?user schema:identifier '%s' ."+ 
                "?user schema:Action ?featureIntegration ."+
                "?featureIntegration schema:identifier '%s' ."+
            "}", user, featureIntegration.getIdentifier());
            Update update = connection.prepareUpdate(QueryLanguage.SPARQL, queryString);
            update.execute();
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        } finally {
            repository.shutDown();
        }
    }
}
