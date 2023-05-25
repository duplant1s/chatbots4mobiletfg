package upc.edu.gessi.tfg.repositories;

import java.util.ArrayList;
import java.util.Arrays;
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
import upc.edu.gessi.tfg.models.ParameterIntegration;
import upc.edu.gessi.tfg.utils.IRIS;

@org.springframework.stereotype.Repository
public class ParameterIntegrationRepository {
    private String repoURL = "http://localhost:7200/repositories/Chatbots4MobileTFG";
    private Repository repository;
    private ParameterRepository parameterRepository = new ParameterRepository();
    
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
        builder.subject("schema:PropertyValue/"+parameterIntegration.getId())
                .add(RDF.TYPE, IRIS.parameterIntegration)
                .add(IRIS.identifier, IRIS.createLiteral(parameterIntegration.getId()));
                String parameterType = parameterRepository.getParameter(parameterIntegration.getSourceParameter()).getType().toString();
                builder.add(IRIS.name, IRIS.createIRI(IRIS.root+parameterType+"/"+parameterIntegration.getSourceParameter()));
                parameterType = parameterRepository.getParameter(parameterIntegration.getTargetParameter()).getType().toString();
                builder.add(IRIS.value, IRIS.createIRI(IRIS.root+parameterType+"/"+parameterIntegration.getTargetParameter()));

        Model model = builder.build();
        try (RepositoryConnection connection = repository.getConnection()) {
            connection.add(model);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        } finally {
            repository.shutDown();
        }
    }


    //TODOOOOOOOOOOOOOOOOOOO
    public void updateParameterIntegration(String id, ParameterIntegration parameterIntegration) {
        try (RepositoryConnection connection = repository.getConnection()) {
            String queryString = String.format("PREFIX schema: <https://schema.org/>\n" +
            "DELETE { \n"+
                "?parameterIntegration schema:name ?source ."+
                "?parameterIntegration schema:value ?target"+
            "}\n"+
            "INSERT { \n"+
                "?parameterIntegration schema:name ?source ."+
                "?parameterIntegration schema:value ?target"+
            "}\n"+
            "WHERE { \n"+
                "?parameterIntegration a schema:PropertyValue ."+ 
                "?parameterIntegration schema:identifier ?id ."+ 
                "?parameterIntegration schema:name ?source ."+
                "?parameterIntegration schema:value ?target"+
            "}", parameterIntegration.getId(), parameterIntegration.getSourceParameter(), parameterIntegration.getTargetParameter());
            TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
            tupleQuery.evaluate();
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

    //USER PREFERENCES
    public void addPreferredParameterIntegration(String user, ParameterIntegration parameterIntegration) {
        if (getParameterIntegration(parameterIntegration.getId()) == null) {
            createParameterIntegration(parameterIntegration);
        }

        ModelBuilder builder = new ModelBuilder();
        builder.setNamespace("schema", "https://schema.org/");
        builder.subject("schema:Person/"+user)
                .add("https://schema.org/PropertyValue", IRIS.createIRI("https://schema.org/PropertyValue/"+parameterIntegration.getId()));
        
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
            "}", user, parameterIntegration.getId());

            Update update = connection.prepareUpdate(QueryLanguage.SPARQL, queryString);
            update.execute();


        } catch(Exception e) {
            System.out.println(e.getMessage());
        } finally {
            repository.shutDown();
        }
    }
}