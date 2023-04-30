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
import upc.edu.gessi.tfg.models.ParameterIntegration;

@org.springframework.stereotype.Repository
public class ParameterIntegrationRepository {
    private String repoURL = "http://localhost:7200/repositories/Chatbots4MobileTFG";
    private Repository repository;
    private ParameterRepository parameterRepository = new ParameterRepository();

    private final SimpleValueFactory vf = SimpleValueFactory.getInstance();
    
    //PARAMETER INTEGRATION IRIs
    private final IRI schemaParameterIntegrationClassIRI = vf.createIRI("https://schema.org/PropertyValue");
    private final IRI identifierPropertyIRI = vf.createIRI("https://schema.org/identifier");
    private final IRI sourcePropertyIRI = vf.createIRI("https://schema.org/name");
    private final IRI targetPropertyIRI = vf.createIRI("https://schema.org/value");

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
        builder.setNamespace("schema", "https://schema.org/");
        builder.subject("schema:PropertyValue/"+parameterIntegration.getId())
                .add(RDF.TYPE, schemaParameterIntegrationClassIRI)
                .add(identifierPropertyIRI, vf.createLiteral(parameterIntegration.getId()));
                String parameterType = parameterRepository.getParameter(parameterIntegration.getSourceParameter()).getType().toString();
                builder.add(sourcePropertyIRI, vf.createIRI("https://schema.org/"+parameterType+"/"+parameterIntegration.getSourceParameter()));
                parameterType = parameterRepository.getParameter(parameterIntegration.getTargetParameter()).getType().toString();
                builder.add(targetPropertyIRI, vf.createIRI("https://schema.org/"+parameterType+"/"+parameterIntegration.getTargetParameter()));

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


}