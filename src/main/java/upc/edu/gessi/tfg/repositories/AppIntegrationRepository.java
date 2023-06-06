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
import org.eclipse.rdf4j.repository.RepositoryConnection;

import upc.edu.gessi.tfg.models.AppIntegration;
import upc.edu.gessi.tfg.utils.IRIS;

import org.eclipse.rdf4j.repository.Repository;

@org.springframework.stereotype.Repository
public class AppIntegrationRepository {

    private String repoURL = "http://localhost:7200/repositories/Chatbots4MobileTFG";
    private Repository repository;

    public AppIntegrationRepository() {
        this.repository = new org.eclipse.rdf4j.repository.http.HTTPRepository(repoURL);
        this.repository.init();
    }
    
    public List<AppIntegration> getAllAppIntegrations() {
        List<AppIntegration> appIntegrations = new ArrayList<>();

        try (RepositoryConnection connection = repository.getConnection()) {
            String queryString = "PREFIX schema: <https://schema.org/>\n" + "SELECT ?id ?source ?target\n" + "WHERE { ?user a schema:AppIntegration ; schema:identifier ?id ; schema:source ?source ; schema:target ?target }";
            TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
            TupleQueryResult result = tupleQuery.evaluate();

            while (result.hasNext()) {
                BindingSet bindingSet = result.next();
                String id = bindingSet.getValue("id").stringValue();
                String source = bindingSet.getValue("source").stringValue();
                String target = bindingSet.getValue("target").stringValue();
                AppIntegration appIntegration = new AppIntegration(source, target);
                appIntegration.setIdentifier(id);
                appIntegrations.add(appIntegration);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            repository.shutDown();
        }

        return appIntegrations;
    }

    public AppIntegration getAppIntegration(String id) {
        AppIntegration appIntegration = null;

        try (RepositoryConnection connection = repository.getConnection()) {
            String queryString = String.format("PREFIX schema: <https://schema.org/>\n" + "SELECT ?id ?source ?target\n" + "WHERE { ?user a schema:AppIntegration ; schema:identifier '%s' ; schema:source ?source ; schema:target ?target }", id);
            TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
            TupleQueryResult result = tupleQuery.evaluate();

            while (result.hasNext()) {
                BindingSet bindingSet = result.next();
                String source = bindingSet.getValue("source").stringValue();
                String target = bindingSet.getValue("target").stringValue();
                appIntegration = new AppIntegration(source, target);
                appIntegration.setIdentifier(id);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            repository.shutDown();
        }
        return appIntegration;
        
    }
    
    public void createAppIntegration(AppIntegration appIntegration) {
        ModelBuilder model = new ModelBuilder();
        model.setNamespace("schema", IRIS.root);
        model.subject("schema:AppIntegration/" + appIntegration.getIdentifier())
            .add(RDF.TYPE, IRIS.appIntegration)
            .add(IRIS.identifier, appIntegration.getIdentifier())
            .add(IRIS.source, IRIS.createCustomIRI(IRIS.mobileApplication+"/"+appIntegration.getSourceApp()))
            .add(IRIS.target, IRIS.createCustomIRI(IRIS.mobileApplication+"/"+appIntegration.getTargetApp()));
        
        Model finalModel = model.build();

        try(RepositoryConnection connection = repository.getConnection()) {
            connection.add(finalModel);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        } finally {
            repository.shutDown();
        }
    }

    public void updateAppIntegration(String id, AppIntegration appIntegration) {
        try (RepositoryConnection connection = repository.getConnection()) {
            StringBuilder queryString = new StringBuilder();
            queryString.append("PREFIX schema: <https://schema.org/>\n");
            queryString.append("DELETE { \n");
            queryString.append("?appIntegration a schema:AppIntegration ;\n");
            queryString.append("schema:source ?source ;\n");
            queryString.append("schema:target ?target .\n");
            queryString.append("}\n");
            queryString.append("INSERT { \n");
            queryString.append("?appIntegration a schema:AppIntegration ;\n");
            queryString.append("schema:source <"+ IRIS.mobileApplication + appIntegration.getSourceApp()+"> ;\n");
            queryString.append("schema:target <"+ IRIS.mobileApplication + appIntegration.getTargetApp()+"> .\n");
            queryString.append("}\n");
            queryString.append("WHERE { \n");
            queryString.append("?appIntegration a schema:AppIntegration ;\n");
            queryString.append("schema:identifier '"+id+"' ;\n");
            queryString.append("schema:source ?source ;\n");
            queryString.append("schema:target ?target .\n");
            queryString.append("}");

            Update update = connection.prepareUpdate(QueryLanguage.SPARQL, queryString.toString());
            update.execute();
        }
    }

    public void removeAppIntegration(String id) {
        try (RepositoryConnection connection = repository.getConnection()) {
            String queryString = String.format("PREFIX schema: <https://schema.org/>\n" + "DELETE WHERE { ?user a schema:AppIntegration ; schema:identifier '%s' ; ?p ?o }",
            id);
            Update update = connection.prepareUpdate(QueryLanguage.SPARQL, queryString);
            update.execute();
        }
        catch (Exception e) {
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

    public List<String> getAppsFromFeature(String user, String feature) {
        List<String> apps = new ArrayList<String>();
        Map<String, Integer> appsMap = new HashMap<String, Integer>();
        try (RepositoryConnection connection = repository.getConnection()) {

            //LOWEST PRIORITY: ALL APPS WITH THE FEATURE
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
                appsMap.put(appName, 1);
            }

            //MEDIUM PRIORITY: APPS THE USER USES WITH THE FEATURE
            queryString = String.format("PREFIX schema: <https://schema.org/>\n"+
            "SELECT ?appName WHERE {"+
                "?user a schema:Person ."+
                "?user schema:identifier '%s' ."+
                "?user schema:application ?app ."+
                "?app a schema:MobileApplication ."+
                "?app schema:name ?appName ."+
                "?app schema:keywords ?feature ."+
                "    ?feature a schema:DefinedTerm;"+
                "       schema:name '%s'"+
            "}", user, feature);

            tupleQuery = connection.prepareTupleQuery(queryString);
            result = tupleQuery.evaluate();

            while (result.hasNext()) {
                BindingSet bindingSet = result.next();
                String appName = bindingSet.getValue("appName").stringValue();
                appsMap.put(appName, appsMap.get(appName) + 2);
            }

            //HIGHEST PRIORITY: USER PREFERRED-APPS THAT HAVE THE FEATURE
            queryString = String.format("PREFIX schema: <https://schema.org/>\n"+
            "SELECT ?appName WHERE {"+
                "{ "+
                "?user a schema:Person ."+
                "?user schema:identifier '%s' ."+
                "?user schema:AppIntegration ?appIntegration ."+
                "?appIntegration a schema:AppIntegration ."+
                "?appIntegration schema:source ?app ."+
                "?app a schema:MobileApplication ."+
                "?app schema:name ?appName ."+
                "?app schema:keywords ?feature ."+
                "?feature a schema:DefinedTerm;"+
                "schema:name '%s'"+
                "} "+
                "UNION "+
                "{ "+
                "?user a schema:Person ."+
                "?user schema:identifier '%s' ."+
                "?user schema:AppIntegration ?appIntegration ."+
                "?appIntegration a schema:AppIntegration ."+
                "?appIntegration schema:target ?app ."+
                "?app a schema:MobileApplication ."+
                "?app schema:name ?appName ."+
                "?app schema:keywords ?feature ."+
                "?feature a schema:DefinedTerm;"+
                "schema:name '%s'"+
                "}"+
            "}", user, feature, user, feature);


            tupleQuery = connection.prepareTupleQuery(queryString);
            result = tupleQuery.evaluate();

            while (result.hasNext()) {
                BindingSet bindingSet = result.next();
                String appName = bindingSet.getValue("appName").stringValue();
                appsMap.put(appName, appsMap.get(appName) + 3);
            }

            //SORT APPS BY PRIORITY
            List<Map.Entry<String, Integer>> list = new ArrayList<>(appsMap.entrySet());

            list.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

            for (Map.Entry<String, Integer> entry : list) {
                apps.add(entry.getKey());
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            repository.shutDown();
        }
        
        return apps;
    }


    //USER PREFERENCES
    public void addPreferredAppIntegration(String user, AppIntegration appIntegration) {
        if (getAppIntegration(appIntegration.getIdentifier()) == null) {
            createAppIntegration(appIntegration);
        }
        ModelBuilder model = new ModelBuilder();
        model.setNamespace("schema", IRIS.root);
        model.subject("schema:Person/"+user)
            .add(IRIS.appIntegration, IRIS.createCustomIRI(IRIS.appIntegration+"/"+appIntegration.getSourceApp()+"-"+appIntegration.getTargetApp()));
        
        Model finalModel = model.build();
        
        try(RepositoryConnection connection = repository.getConnection()) {
            connection.add(finalModel);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        } finally {
            repository.shutDown();
        }
    }

    public void removePreferredAppIntegration(String user, AppIntegration appIntegration) {
        try (RepositoryConnection connection = repository.getConnection()) {
            String queryString = String.format("PREFIX schema: <https://schema.org/>\n" +
            "DELETE { \n"+
                "?user schema:AppIntegration ?appIntegration ."+
            "}\n"+
            "WHERE { \n"+
                "?user a schema:Person ."+ 
                "?user schema:identifier '%s' ."+ 
                "?user schema:AppIntegration ?appIntegration ."+
                "?appIntegration schema:identifier '%s' ."+
            "}", user, appIntegration.getIdentifier());
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
