package upc.edu.gessi.tfg.repositories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
import upc.edu.gessi.tfg.models.ParameterIntegration;
import upc.edu.gessi.tfg.utils.IRIS;

@org.springframework.stereotype.Repository
public class ParameterIntegrationRepository {
    private String repoURL = "http://localhost:7200/repositories/Chatbots4MobileTFG";
    private Repository repository;
    private ParameterRepository parameterRepository = new ParameterRepository();
    private FeatureRepository featureRepository = new FeatureRepository();
    
    public ParameterIntegrationRepository() {
        this.repository = new HTTPRepository(repoURL);
        this.repository.init();
    }

    public List<ParameterIntegration> getAllParameterIntegrations() {
        List<ParameterIntegration> parameterIntegrations = new ArrayList<ParameterIntegration>();

        try(RepositoryConnection connection = repository.getConnection()) {
            String queryString = "PREFIX schema: <https://schema.org/>\n" +
            "SELECT ?id ?source ?target "+
            "WHERE { \n"+
                "?parameterIntegration a schema:PropertyValue ;"+ 
                "                        schema:identifier ?id ;"+ 
                "                        schema:name ?source ;"+
                "                        schema:value ?target"+
            "}";
            TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
            TupleQueryResult result = tupleQuery.evaluate();

            while (result.hasNext()) {
                BindingSet bindingSet = result.next();
                String id = bindingSet.getValue("id").stringValue();
                String source = bindingSet.getValue("source").stringValue();
                String target = bindingSet.getValue("target").stringValue();
                parameterIntegrations.add(new ParameterIntegration(id, source, target));
                
            }
        } finally {
            repository.shutDown();
        }

        return parameterIntegrations;
    }

    public ParameterIntegration getParameterIntegration(String id) {
        ParameterIntegration parameterIntegration = null;

        try(RepositoryConnection connection = repository.getConnection()) {
            String queryString = String.format("PREFIX schema: <https://schema.org/>\n" +
            "SELECT ?id ?source ?target "+
            "WHERE { \n"+
                "?parameterIntegration a schema:PropertyValue ;"+ 
                "                      schema:identifier '%s' ;"+ 
                "                      schema:name ?source ;"+
                "                      schema:value ?target"+
            "}", id);
            
            TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL,queryString);
            TupleQueryResult result = tupleQuery.evaluate();

            while (result.hasNext()) {
                BindingSet bindingSet = result.next();
                String source = bindingSet.getValue("source").stringValue();
                String target = bindingSet.getValue("target").stringValue();
                parameterIntegration = new ParameterIntegration(id, source, target);
            }
        } finally {
            repository.shutDown();
        }

        return parameterIntegration;
    }
    
    public void createParameterIntegration(ParameterIntegration parameterIntegration) {
        ModelBuilder builder = new ModelBuilder();
        builder.setNamespace("schema", IRIS.root);
        builder.subject("schema:PropertyValue/"+parameterIntegration.getIdentifier())
                .add(RDF.TYPE, IRIS.parameterIntegration)
                .add(IRIS.identifier, IRIS.createLiteral(parameterIntegration.getIdentifier()));
                String parameterType = parameterRepository.getParameter(parameterIntegration.getSourceParameter()).getType().toString();
                builder.add(IRIS.name, IRIS.createCustomIRI(IRIS.root+parameterType+"/"+parameterIntegration.getSourceParameter()));
                parameterType = parameterRepository.getParameter(parameterIntegration.getTargetParameter()).getType().toString();
                builder.add(IRIS.value, IRIS.createCustomIRI(IRIS.root+parameterType+"/"+parameterIntegration.getTargetParameter()));

        Model model = builder.build();
        try (RepositoryConnection connection = repository.getConnection()) {
            connection.add(model);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        } finally {
            repository.shutDown();
        }
    }

    public void updateParameterIntegration(String id, ParameterIntegration parameterIntegration) {
        try (RepositoryConnection connection = repository.getConnection()) {
            String queryString = String.format("PREFIX schema: <https://schema.org/>\n" +
            "DELETE { \n"+
                "?parameterIntegration schema:name ?source ."+
                "?parameterIntegration schema:value ?target"+
            "}\n"+
            "INSERT { \n"+
                "?parameterIntegration schema:name '%s'. "+
                "?parameterIntegration schema:value '%s' "+
            "}\n"+
            "WHERE { \n"+
                "?parameterIntegration a schema:PropertyValue ."+ 
                "?parameterIntegration schema:identifier '%s' ."+ 
                "?parameterIntegration schema:name ?source ."+
                "?parameterIntegration schema:value ?target"+
            "}", parameterIntegration.getSourceParameter(), parameterIntegration.getTargetParameter(), id);

            Update update = connection.prepareUpdate(QueryLanguage.SPARQL, queryString);
            update.execute();
            
        } finally {
            repository.shutDown();
        }
    }

    public void deleteParameterIntegration(String id) {
        try (RepositoryConnection connection = repository.getConnection()) {
            String queryString = String.format("PREFIX schema: <https://schema.org/>\n" +
            "DELETE WHERE { \n"+
                "?parameterIntegration a schema:PropertyValue ;"+ 
                "schema:identifier '%s' ;"+ 
                "?p ?o"+
            "}", id);
            Update update = connection.prepareUpdate(QueryLanguage.SPARQL, queryString);
            update.execute();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            repository.shutDown();
        }
    }


    // Story #3 - parameter integration
    // Request Parameter Integration:
    //     description: request source-target parameter integrations for selected app
    //     input: source app, source feature, selected target app, selected target feature
    //     expected output: List of (source parameter, target parameter) 
    public List<List<Object>> requestParameterIntegration(String sourceApp, String sourceFeature, String targetApp, String targetFeature) {
        try (RepositoryConnection connection = repository.getConnection()) {
            String queryString = String.format("PREFIX schema: <https://schema.org/>\n" +
            "SELECT ?sourceParam ?targetParam\n" +
            "WHERE {\n" +
              // Define the source app and feature
              "?sourceApp a schema:MobileApplication;"+
              "           schema:identifier '%s';"+
              "           schema:name ?sourceAppName;"+
              "           schema:keywords ?sourceFeature."+
              "  ?sourceFeature a schema:DefinedTerm;" +
              "                 schema:name '%s'." +
                
              // Define the target app and feature
              "   ?targetApp a schema:MobileApplication;"+
              "           schema:identifier '%s';"+
              "           schema:name ?targetAppName;"+
              "           schema:keywords ?targetFeature."+
              "  ?targetFeature a schema:DefinedTerm;"+
              "                 schema:name '%s'."+
                
              // Find the parameter integrations between the source and target features
              "?sourceFeature schema:hasPart ?sourceParam."+
              "?targetFeature schema:hasPart ?targetParam."+
               
              "?parameterIntegration a schema:PropertyValue;"+
              "                        schema:name ?sourceParam;"+
              "                        schema:value ?targetParam."+    
            "}", sourceApp, sourceFeature, targetApp, targetFeature);

            TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
            TupleQueryResult result = tupleQuery.evaluate();

            List<List<Object>> parameterIntegrations = new ArrayList<>();
            while (result.hasNext()) {
                BindingSet bindingSet = result.next();
                String sourceParam = bindingSet.getValue("sourceParam").stringValue();
                String targetParam = bindingSet.getValue("targetParam").stringValue();
                List<Object> parameterIntegration = Arrays.asList(sourceParam, targetParam);
                parameterIntegrations.add(parameterIntegration);     
            }

            return parameterIntegrations;
        } finally {
            repository.shutDown();
        }
    }

    // Story #4 - custom parameters
    // Request custom parameter:
    // description: request custom parameters for selected app
    // input: source app, source feature, selected target app, selected target feature
    // expected output: List of custom target parameters (optional field)
    public List<String> requestCustomParameters(String sourceApp, String sourceFeature, String targetApp, String targetFeature) {
        List<String> paramsWithIntegration = new ArrayList<>();
        List<String> paramsWithoutIntegration = new ArrayList<>();
        try (RepositoryConnection connection = repository.getConnection()) {
            String queryString = String.format("PREFIX schema: <https://schema.org/>\n" +
            "SELECT ?targetParamId\n" +
            "WHERE {"+
                "?sourceApp a schema:MobileApplication;"+
                "           schema:identifier '%s';"+
                "           schema:keywords ?sourceFeature ."+
                "?sourceFeature a schema:DefinedTerm;"+
                "         schema:name '%s'."+
                "?sourceFeature schema:hasPart ?sourceParam."+
                "?sourceParam schema:identifier ?sourceParamId."+
                "?targetApp a schema:MobileApplication;"+
                "           schema:identifier '%s';"+
                "           schema:keywords ?targetFeature ."+
                "?targetFeature a schema:DefinedTerm;"+
                "         schema:name '%s'."+
                "?targetFeature schema:hasPart ?targetParam."+
                "?targetParam schema:identifier ?targetParamId."+
                "?paramInt a schema:PropertyValue;"+
                "        schema:name ?sourceInt;"+
                "        schema:value ?targetInt."+
                "?sourceInt schema:identifier ?sourceParamId."+
                "?targetInt schema:identifier ?targetParamId"+
            "}", sourceApp, sourceFeature, targetApp, targetFeature);

            TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
            TupleQueryResult result = tupleQuery.evaluate();

            while(result.hasNext()) {
                BindingSet bindingSet = result.next();
                String targetParamId = bindingSet.getValue("targetParamId").stringValue();
                if (!paramsWithIntegration.contains(targetParamId))
                    paramsWithIntegration.add(targetParamId);
            }

            paramsWithoutIntegration = featureRepository.getFeature(targetFeature).getParameters();
            paramsWithoutIntegration.removeAll(paramsWithIntegration);
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            repository.shutDown();
        }
        return paramsWithoutIntegration;
    }

    //USER PREFERENCES
    public void addPreferredParameterIntegration(String user, ParameterIntegration parameterIntegration) {
        if (getParameterIntegration(parameterIntegration.getIdentifier()) == null) {
            createParameterIntegration(parameterIntegration);
        }

        ModelBuilder builder = new ModelBuilder();
        builder.setNamespace("schema", IRIS.root);
        builder.subject("schema:Person/"+user)
                .add(IRIS.parameterIntegration, IRIS.createCustomIRI(IRIS.parameterIntegration+"/"+parameterIntegration.getIdentifier()));
        
        Model model = builder.build();
        try (RepositoryConnection connection = repository.getConnection()) {
            connection.add(model);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        } finally {
            repository.shutDown();
        }
    }

    public void removePreferredParameterIntegration(String user, ParameterIntegration parameterIntegration) {
        try (RepositoryConnection connection = repository.getConnection()) {
            String queryString = String.format("PREFIX schema: <https://schema.org/>\n" +
            "DELETE { \n"+
                "?user schema:PropertyValue ?parameterIntegration ."+
            "}\n"+
            "WHERE { \n"+
                "?user a schema:Person ."+ 
                "?user schema:identifier '%s' ."+ 
                "?user schema:PropertyValue ?parameterIntegration ."+
                "?parameterIntegration schema:identifier '%s' ."+
            "}", user, parameterIntegration.getIdentifier());

            Update update = connection.prepareUpdate(QueryLanguage.SPARQL, queryString);
            update.execute();


        } catch(Exception e) {
            System.out.println(e.getMessage());
        } finally {
            repository.shutDown();
        }
    }
}