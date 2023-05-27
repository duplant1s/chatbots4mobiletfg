package upc.edu.gessi.tfg.repositories;

import upc.edu.gessi.tfg.models.ParamType;
import upc.edu.gessi.tfg.models.Parameter;
import upc.edu.gessi.tfg.utils.IRIS;

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

@org.springframework.stereotype.Repository
public class ParameterRepository {

    private String repoURL = "http://localhost:7200/repositories/Chatbots4MobileTFG";
    private Repository repository;
    
    public ParameterRepository() {
        this.repository = new HTTPRepository(repoURL);
        this.repository.init();
    }

    public List<Parameter> getAllParameters(){
        List<Parameter> parameters = new ArrayList<Parameter>();

        try (RepositoryConnection connection = repository.getConnection()) {
            String queryString = "PREFIX schema: <https://schema.org/>\n" +
            "SELECT ?id ?name ?type\n"+
            "WHERE {"+
              "{ ?parameter a schema:Number ; schema:identifier ?id; schema:name ?name . BIND(\"Number\" AS ?type) }"+
              "UNION { ?parameter a schema:Boolean ; schema:identifier ?id; schema:name ?name . BIND(\"Boolean\" AS ?type) }"+
              "UNION { ?parameter a schema:Text ; schema:identifier ?id; schema:name ?name  . BIND(\"Text\" AS ?type) }"+
                "UNION { ?parameter a schema:GeoCoordinates ; schema:identifier ?id; schema:name ?name  . BIND(\"GeoCoordinates\" AS ?type) }"+
                "UNION { ?parameter a schema:ContactPoint ; schema:identifier ?id; schema:name ?name  . BIND(\"ContactPoint\" AS ?type) }"+
            "}";

            TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
            TupleQueryResult result = tupleQuery.evaluate();

            while (result.hasNext()) {
                BindingSet bindingSet = result.next();
                String id = bindingSet.getValue("id").stringValue();
                String name = bindingSet.getValue("name").stringValue();
                String type = bindingSet.getValue("type").stringValue();
                Parameter parameter = new Parameter(id, name, ParamType.valueOf(type));
                parameters.add(parameter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            repository.shutDown();
        }

        return parameters;
    }

    public Parameter getParameter(String id) {
        Parameter parameter = null;
        try (RepositoryConnection connection = repository.getConnection()) {
            String queryString = String.format("PREFIX schema: <https://schema.org/>\n" +
            "SELECT ?name ?type WHERE { ?parameter a ?type ; schema:name ?name ; schema:identifier \"%s\" . FILTER (?type IN (schema:Number, schema:Boolean, schema:Text, schema:GeoCoordinates, schema:ContactPoint))}", id);
            
            TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
            TupleQueryResult result = tupleQuery.evaluate();

            if (result.hasNext()) {
                BindingSet bindingSet = result.next();
                String name = bindingSet.getValue("name").stringValue();
                String type = bindingSet.getValue("type").stringValue();
                parameter = new Parameter(id, name, ParamType.valueOf(type.substring("https://schema.org/".length())));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            repository.shutDown();
        }

        return parameter;
    }

    public void createParameter(Parameter parameter) {
        ModelBuilder builder = new ModelBuilder();
        builder.setNamespace("schema", IRIS.root);
        builder.subject("schema:"+parameter.getType()+"/"+parameter.getIdentifier())
            .add("schema:name", parameter.getName())
            .add("schema:identifier", parameter.getIdentifier());
        switch (parameter.getType()) {
            case Number:
                builder.add(RDF.TYPE, IRIS.number);
                break;
            case Boolean:
                builder.add(RDF.TYPE, IRIS.bool);
                break;
            case Text:
                builder.add(RDF.TYPE, IRIS.text);
                break;
            case GeoCoordinates:
                builder.add(RDF.TYPE, IRIS.geoCoordinates);
                break;
            case ContactPoint:
                builder.add(RDF.TYPE, IRIS.contactPoint);
                break;
            default:
                break;
        }
        Model model = builder.build();
        try (RepositoryConnection connection = repository.getConnection()) {
            connection.add(model);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            repository.shutDown();
        }
    }

    public void updateParameter(String id, Parameter updatedParameter) {
        try (RepositoryConnection connection = repository.getConnection()) {
            String queryString = String.format("PREFIX schema: <https://schema.org/>\n" +
            "DELETE { \n"+
            " ?parameter schema:name ?name ; \n"+
            "} \n"+
            "INSERT { \n"+
            " ?parameter schema:name \'%s\' ; \n"+
            "} \n"+
            "WHERE { \n"+
            " ?parameter schema:identifier \'%s\' ; \n"+
            " ?parameter a schema:Number . \n"+
            " UNION { ?parameter a schema:Boolean . } \n"+
            " UNION { ?parameter a schema:Text . } \n"+
            " UNION { ?parameter a schema:GeoCoordinates . } \n"+
            " UNION { ?parameter a schema:ContactPoint . } \n"+
            "}", updatedParameter.getName(), id);

            Update update = connection.prepareUpdate(QueryLanguage.SPARQL, queryString);
            update.execute();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            repository.shutDown();
        }
    }

    public void deleteParameter(String id) {
        try (RepositoryConnection connection = repository.getConnection()) {
            String queryString = String.format("PREFIX schema: <https://schema.org/>\n"+ "DELETE WHERE { ?parameter schema:identifier '%s' ; ?p ?o }", id);
            Update update = connection.prepareUpdate(QueryLanguage.SPARQL, queryString);
            update.execute();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            repository.shutDown();
        }
    }
    
}
