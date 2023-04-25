package upc.edu.gessi.tfg.repositories;

import upc.edu.gessi.tfg.models.User;


import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.query.*;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;
import org.eclipse.rdf4j.repository.util.RDFInserter;
import org.eclipse.rdf4j.rio.RDFFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@org.springframework.stereotype.Repository
public class UserRepository  {

    private String repoURL = "http://localhost:7200/repositories/ChatbotsReduced";
    private Repository repository;

    private final SimpleValueFactory vf = SimpleValueFactory.getInstance();


    private final IRI schemaPersonClass = vf.createIRI("http://schema.org/Person");
    private final IRI identifierProperty = vf.createIRI("http://schema.org/identifier");
    private final IRI emailProperty = vf.createIRI("http://schema.org/email");
    private final IRI givenNameProperty = vf.createIRI("http://schema.org/givenName");
    private final IRI familyNameProperty = vf.createIRI("http://schema.org/familyName");

    public UserRepository() {
        this.repository = new HTTPRepository(repoURL);
        this.repository.init();
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<User>();

        try (RepositoryConnection connection = repository.getConnection()) {
            String queryString = "PREFIX schema: <https://schema.org/>\n" +
            "\n" +"SELECT ?id ?email ?givenName ?familyName WHERE { ?user a schema:Person . ?user schema:identifier ?id . ?user schema:email ?email . ?user schema:givenName ?givenName . ?user schema:familyName ?familyName }";
            TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
            TupleQueryResult result = tupleQuery.evaluate();

            while (result.hasNext()) {
                BindingSet bindingSet = result.next();
                Long id = Long.parseLong(bindingSet.getValue("id").stringValue());
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

    public User getUser(Long id) {
        User user = null;
        try (RepositoryConnection connection = repository.getConnection()) {
            String queryString = String.format("SELECT ?email ?givenName ?familyName WHERE { ?user a schema:Person ; schema:identifier '%s' ; schema:email ?email ; schema:givenName ?givenName ; schema:familyName ?familyName }", id);
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
        String id = UUID.randomUUID().toString();

        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.setNamespace("schema", "http://schema.org/");
        modelBuilder.subject("http://localhost:8080/users/" + id)
                .add(RDF.TYPE, schemaPersonClass)
                .add(identifierProperty, vf.createLiteral(id))
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

    public void updateUser(Long id, User updatedUser) {
        try (RepositoryConnection connection = repository.getConnection()) {
            String queryString = String.format("DELETE { ?user schema:email ?email ; schema:givenName ?givenName ; schema:familyName ?familyName } INSERT { ?user schema:email '%s' ; schema:givenName '%s' ; schema:familyName '%s' } WHERE { ?user a schema:Person ; schema:identifier '%s' ; schema:email ?email ; schema:givenName ?givenName ; schema:familyName ?familyName }", updatedUser.getEmail(), updatedUser.getGivenName(), updatedUser.getFamilyName(), id);
            Update update = connection.prepareUpdate(QueryLanguage.SPARQL, queryString);
            update.execute();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            repository.shutDown();
        }
    }

    public void deleteUser(Long id) {
        try (RepositoryConnection connection = repository.getConnection()) {
            String queryString = String.format("DELETE WHERE { ?user a schema:Person ; schema:identifier '%s' ; ?p ?o }", id);
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