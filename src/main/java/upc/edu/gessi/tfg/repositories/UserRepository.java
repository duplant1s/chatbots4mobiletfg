package upc.edu.gessi.tfg.repositories;

import upc.edu.gessi.tfg.models.User;
import upc.edu.gessi.tfg.utils.IRIS;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.query.*;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@org.springframework.stereotype.Repository
public class UserRepository  {

    private String repoURL = "http://localhost:7200/repositories/Chatbots4MobileTFG";
    private Repository repository;

    public UserRepository() {
        this.repository = new HTTPRepository(repoURL);
        this.repository.init();
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<User>();

        try (RepositoryConnection connection = repository.getConnection()) {
            String queryString = 
            "PREFIX schema: <https://schema.org/> "+
            "SELECT ?id ?email ?givenName ?familyName ?app ?prefFeature ?prefParam ?prefApp " +
			"WHERE { ?user a schema:Person . "+
            "?user schema:identifier ?id . ?user schema:email ?email . ?user schema:givenName ?givenName . "+
            "?user schema:familyName ?familyName . "+
    		"OPTIONAL { ?user schema:application ?app .} "+
    		"OPTIONAL {?user schema:Action ?prefFeature .} "+
    		"OPTIONAL {?user schema:PropertyValue ?prefParam .} "+
        	"OPTIONAL {?user schema:preferredapp ?prefApp }}";
            TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
            TupleQueryResult result = tupleQuery.evaluate();
            Map<String, User> userMap = new HashMap<String, User>();

            while (result.hasNext()) {
                BindingSet bindingSet = result.next();
                String id = bindingSet.getValue("id").stringValue();
                String email = bindingSet.getValue("email").stringValue();
                String givenName = bindingSet.getValue("givenName").stringValue();
                String familyName = bindingSet.getValue("familyName").stringValue();
                Value appValue = bindingSet.getValue("app");
                String app = appValue != null ? appValue.stringValue() : null;
                Value prefFeatureInt = bindingSet.getValue("prefFeature");
                String prefFeature = prefFeatureInt != null ? prefFeatureInt.stringValue() : null;
                Value prefParamInt = bindingSet.getValue("prefParam");
                String prefParam = prefParamInt != null ? prefParamInt.stringValue() : null;
                Value prefApplication = bindingSet.getValue("prefApp");
                String prefApp = prefApplication != null ? prefApplication.stringValue() : null;
                User user;
                if (userMap.containsKey(id)) {
                    user = userMap.get(id);
                    user.getApps().add(app);
                    user.getPreferredFeatureIntegrations().add(prefFeature);
                    user.getPreferredParameterIntegrations().add(prefParam);
                } else {
                    user = new User(id, email, givenName, familyName, new ArrayList<String>(Arrays.asList(app)), new ArrayList<String>(Arrays.asList(prefFeature)), new ArrayList<String>(Arrays.asList(prefParam)), new ArrayList<String>(Arrays.asList(prefApp)));
                    userMap.put(id, user);
                }
            }
            users.addAll(userMap.values());
        } finally {
            repository.shutDown();
        }

        return users;
    }

    public User getUser(String id) {
        User user = null;
        try (RepositoryConnection connection = repository.getConnection()) {
            String queryString = String.format("PREFIX schema: <https://schema.org/>"+
            "SELECT ?email ?givenName ?familyName ?app ?prefFeature ?prefParam ?prefApp " +
			"WHERE { ?user a schema:Person ."+
            "?user schema:identifier '%s' . ?user schema:email ?email . ?user schema:givenName ?givenName ."+
            "?user schema:familyName ?familyName . "+
    		"OPTIONAL { ?user schema:application ?app .}"+
    		"OPTIONAL {?user schema:Action ?prefFeature .}"+
    		"OPTIONAL {?user schema:PropertyValue ?prefParam .}"+
        	"OPTIONAL {?user schema:AppIntegration ?prefApp }"+
            "}", id);
            TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
            TupleQueryResult result = tupleQuery.evaluate();
            
            List<String> apps = new ArrayList<String>();
            List<String> prefFeature = new ArrayList<String>();
            List<String> prefParam = new ArrayList<String>();
            List<String> prefApps = new ArrayList<String>();
            if (result.hasNext()) {
                BindingSet bindingSet = result.next();
                String email = bindingSet.getValue("email").stringValue();
                String givenName = bindingSet.getValue("givenName").stringValue();
                String familyName = bindingSet.getValue("familyName").stringValue();
                Value appValue = bindingSet.getValue("app");
                if (appValue != null) {
                    String app = appValue.stringValue();
                    apps.add(app);
                }
                Value prefFeatureInt = bindingSet.getValue("prefFeature");
                if (prefFeatureInt != null) {
                    String featureInt = prefFeatureInt.stringValue();
                    prefFeature.add(featureInt);
                }
                Value prefParamInt = bindingSet.getValue("prefParam");
                if (prefParamInt != null) {
                    String paramInt = prefParamInt.stringValue();
                    prefParam.add(paramInt);
                }
                Value prefApplication = bindingSet.getValue("prefApp");
                if (prefApplication != null) {
                    String prefApp = prefApplication.stringValue();
                    prefApps.add(prefApp);
                }

                user = new User(id, email, givenName, familyName, apps, prefFeature, prefParam, prefApps);
            }
        } finally {
            repository.shutDown();
        }
        
        return user;
    }

    public void createUser(User user) {
        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.setNamespace("schema", IRIS.root);
        modelBuilder.subject("schema:Person/" + user.getIdentifier())
                .add(RDF.TYPE, IRIS.person)
                .add(IRIS.identifier, IRIS.createLiteral(user.getIdentifier()))
                .add(IRIS.email, IRIS.createLiteral(user.getEmail()))
                .add(IRIS.givenName, IRIS.createLiteral(user.getGivenName()))
                .add(IRIS.familyName, IRIS.createLiteral(user.getFamilyName()));

        Iterator<String> apps = user.getApps().iterator();
        while (apps.hasNext()) {
            String app = apps.next();
            modelBuilder.add(IRIS.application, IRIS.createIRI(IRIS.mobileApplication+"/" + app));
        }

        Iterator<String> featuresInt = user.getPreferredFeatureIntegrations().iterator();
        while (featuresInt.hasNext()) {
            String feature = featuresInt.next();
            modelBuilder.add(IRIS.featureIntegration, IRIS.createIRI(IRIS.featureIntegration + "/" + feature));
        }

        Iterator<String> parametersInt = user.getPreferredParameterIntegrations().iterator();
        while (parametersInt.hasNext()) {
            String parameter = parametersInt.next();
            modelBuilder.add(IRIS.parameterIntegration, IRIS.createIRI(IRIS.parameterIntegration + "/" + parameter));
        }

        Iterator<String> prefApps = user.getPreferredApps().iterator();
        while (prefApps.hasNext()) {
            String prefApp = prefApps.next();
            modelBuilder.add(IRIS.appIntegration, IRIS.createIRI(IRIS.mobileApplication +"/" + prefApp));
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

    public void updateUser(String id, User updatedUser) {
        try (RepositoryConnection connection = repository.getConnection()) {
            String queryString = String.format("PREFIX schema: <https://schema.org/>\n" 
            +"DELETE {"+
                "?user schema:email ?oldEmail ;"+
                      "schema:familyName ?oldFamilyName ."+
                      "schema:givenName ?oldGivenName . "+
              "}"+
              "INSERT {"+
                "?user schema:email %s ;"+
                      "schema:givenName %s ;"+
                      "schema:familyName %s ."+
              "}"+
              "WHERE {"+
                "?user a schema:Person ;"+
                      "schema:identifier '%s' ."+
                "}"+
              "}", updatedUser.getEmail() != null ?updatedUser.getEmail() : "?oldEmail", 
              updatedUser.getFamilyName() != null ?updatedUser.getFamilyName() : "?oldFamilyName",
              updatedUser.getGivenName() != null ?updatedUser.getGivenName() : "?oldGivenName");

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
