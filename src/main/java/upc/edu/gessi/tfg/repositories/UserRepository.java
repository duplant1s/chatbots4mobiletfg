package upc.edu.gessi.tfg.repositories;

import upc.edu.gessi.tfg.models.User;


import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.query.*;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Repository
public class UserRepository  {

    private String repoURL = "http://localhost:7200/repositories/Chatbots4MobileTFG";
    private Repository repository;

    private final SimpleValueFactory vf = SimpleValueFactory.getInstance();


    private final IRI schemaPersonClass = vf.createIRI("https://schema.org/Person");
    private final IRI identifierProperty = vf.createIRI("https://schema.org/identifier");
    private final IRI emailProperty = vf.createIRI("https://schema.org/email");
    private final IRI givenNameProperty = vf.createIRI("https://schema.org/givenName");
    private final IRI familyNameProperty = vf.createIRI("https://schema.org/familyName");

    public UserRepository() {
        this.repository = new HTTPRepository(repoURL);
        this.repository.init();
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<User>();

        try (RepositoryConnection connection = repository.getConnection()) {
            String queryString = "PREFIX schema: <https://schema.org/>\n" +
            "SELECT ?id ?email ?givenName ?familyName WHERE { ?user a schema:Person ."+ 
            "?user schema:identifier ?id . ?user schema:email ?email . ?user schema:givenName ?givenName ."+ 
            "?user schema:familyName ?familyName }";
            TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
            TupleQueryResult result = tupleQuery.evaluate();

            while (result.hasNext()) {
                BindingSet bindingSet = result.next();
                String id = bindingSet.getValue("id").stringValue();
                String email = bindingSet.getValue("email").stringValue();
                String givenName = bindingSet.getValue("givenName").stringValue();
                String familyName = bindingSet.getValue("familyName").stringValue();
                User user = new User(id, email, givenName, familyName);
                users.add(user);
            }
        } finally {
            repository.shutDown();
        }

        return users;
    }

    public User getUser(String id) {
        User user = null;
        try (RepositoryConnection connection = repository.getConnection()) {
            String queryString = String.format("PREFIX schema: <https://schema.org/>\n" +
            "\n" +"SELECT ?email ?givenName ?familyName WHERE { ?user a schema:Person ; schema:identifier '%s' ;"+
            "schema:email ?email ; schema:givenName ?givenName ; schema:familyName ?familyName }", id);
            TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
            TupleQueryResult result = tupleQuery.evaluate();

            if (result.hasNext()) {
                BindingSet bindingSet = result.next();
                String email = bindingSet.getValue("email").stringValue();
                String givenName = bindingSet.getValue("givenName").stringValue();
                String familyName = bindingSet.getValue("familyName").stringValue();
                user = new User(id, email, givenName, familyName);
            }
        } finally {
            repository.shutDown();
        }
        
        return user;
    }

    public void createUser(User user) {
        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.setNamespace("schema", "https://schema.org/");
        modelBuilder.subject("schema:Person/" + user.getIdentifier())
                .add(RDF.TYPE, schemaPersonClass)
                .add(identifierProperty, vf.createLiteral(user.getIdentifier()))
                .add(emailProperty, vf.createLiteral(user.getEmail()))
                .add(givenNameProperty, vf.createLiteral(user.getGivenName()))
                .add(familyNameProperty, vf.createLiteral(user.getFamilyName()));

        Model model = modelBuilder.build();
        try (RepositoryConnection connection = repository.getConnection()) {
            connection.add(model);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            repository.shutDown();
        }
    }

    public void updateUser(String id, User updatedUser) {
        try (RepositoryConnection connection = repository.getConnection()) {
            String queryString = String.format("PREFIX schema: <https://schema.org/>\n" 
            +"DELETE { ?user schema:email ?email ; schema:givenName ?givenName ; schema:familyName ?familyName }"
            +"INSERT { ?user schema:email '%s' ; schema:givenName '%s' ; schema:familyName '%s' }"
            +"WHERE { ?user a schema:Person ; schema:identifier '%s' ; schema:email ?email ; schema:givenName ?givenName ; schema:familyName ?familyName }", 
            updatedUser.getEmail(), updatedUser.getGivenName(), updatedUser.getFamilyName(), id);
            Update update = connection.prepareUpdate(QueryLanguage.SPARQL, queryString);
            update.execute();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            repository.shutDown();
        }
    }

    public void deleteUser(String id) {
        try (RepositoryConnection connection = repository.getConnection()) {
            String queryString = String.format("PREFIX schema: <https://schema.org/>\n" + "DELETE WHERE { ?user a schema:Person ; schema:identifier '%s' ; ?p ?o }", id);
            Update update = connection.prepareUpdate(QueryLanguage.SPARQL, queryString);
            update.execute();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            repository.shutDown();
        }
    }
}
