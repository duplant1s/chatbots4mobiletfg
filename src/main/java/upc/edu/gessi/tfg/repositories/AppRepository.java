package upc.edu.gessi.tfg.repositories;

import upc.edu.gessi.tfg.models.App;
import upc.edu.gessi.tfg.models.AppCategory;
import upc.edu.gessi.tfg.models.AppIntegration;
import upc.edu.gessi.tfg.utils.IRIS;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Value;
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
public class AppRepository {
    private String repoURL = "http://localhost:7200/repositories/Chatbots4MobileTFG";
    private Repository repository;

    public AppRepository() {
        this.repository = new HTTPRepository(repoURL);
        this.repository.init();
    }

    public List<App> getAllApps() {
        List<App> apps = new ArrayList<App>();

        try (RepositoryConnection connection = repository.getConnection()) {
            String queryString = "PREFIX schema: <https://schema.org/>\n"+
            "SELECT ?id ?name ?description "+
            "?summary ?releaseNotes "+
            "?applicationCategory ?datePublished ?dateModified ?softwareVersion ?feature \n"+
            "WHERE {\n"+
              "?app a schema:MobileApplication ."+
              "?app schema:identifier ?id ."+
              "?app schema:name ?name ."+
              "?app schema:description ?desc."+
              "?desc schema:text ?description ."+
              "?app schema:abstract ?summ ."+
              "?summ schema:text ?summary ."+
              "?app schema:releaseNotes ?releaseNotes ."+
              "?app schema:applicationCategory ?applicationCategory ."+
              "?app schema:datePublished ?datePublished ."+
              "?app schema:dateModified ?dateModified ."+
              "?app schema:softwareVersion ?softwareVersion ."+
              "OPTIONAL { ?app schema:keywords ?feature } ."+
            "}";
            
            TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
            TupleQueryResult result = tupleQuery.evaluate();
            Map<String, App> appMap = new HashMap<String, App>();

            while (result.hasNext()) {
                BindingSet bindingSet = result.next();
                String identifier = bindingSet.getValue("id").stringValue();
                String name = bindingSet.getValue("name").stringValue();
                String description = bindingSet.getValue("description").stringValue();
                String summary = bindingSet.getValue("summary").stringValue();
                String releaseNotes = bindingSet.getValue("releaseNotes").stringValue();
                String category = bindingSet.getValue("applicationCategory").stringValue();
                AppCategory appCategory = AppCategory.valueOf(category);
                String datePublished = bindingSet.getValue("datePublished").stringValue();
                String dateModified = bindingSet.getValue("dateModified").stringValue();
                String version = bindingSet.getValue("softwareVersion").stringValue();
                Value featureValue = bindingSet.getValue("feature");
                String feature = featureValue != null ? featureValue.stringValue() : null;

                App app;
                if (appMap.containsKey(identifier)) {
                    app = appMap.get(identifier);
                    app.getFeatures().add(feature);
                } else {
                    app = new App(name, identifier, description, summary, releaseNotes, appCategory, datePublished, dateModified, version, new ArrayList<String>(Arrays.asList(feature)));
                    appMap.put(identifier, app);
                }
                
            }
            apps.addAll(appMap.values());
        } finally {
            repository.shutDown();
        }

        return apps;
    }

    public App getApp(String id) {
        App app = null;
        try (RepositoryConnection connection = repository.getConnection()) {
            String queryString = String.format(
                "PREFIX schema: <https://schema.org/>\n"+
                "SELECT ?id ?name ?description "+
                "?summary ?releaseNotes "+
                "?applicationCategory ?datePublished ?dateModified ?softwareVersion ?feature \n"+
                "WHERE {\n"+
                "?app a schema:MobileApplication ."+
                "?app schema:identifier '%s' ."+
                "?app schema:name ?name ."+
                "?app schema:description ?desc."+
                "?desc schema:text ?description ."+
                "?app schema:abstract ?summ ."+
                "?summ schema:text ?summary ."+
                "?app schema:releaseNotes ?releaseNotes ."+
                "?app schema:applicationCategory ?applicationCategory ."+
                "?app schema:datePublished ?datePublished ."+
                "?app schema:dateModified ?dateModified ."+
                "?app schema:softwareVersion ?softwareVersion ."+
                "OPTIONAL { ?app schema:keywords ?feature } ."+

              "}", id);
              
            TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
            TupleQueryResult result = tupleQuery.evaluate();
            List<String> features = new ArrayList<String>();
            if (result.hasNext()) {
                BindingSet bindingSet = result.next();
                String name = bindingSet.getValue("name").stringValue();
                String description = bindingSet.getValue("description").stringValue();
                String summary = bindingSet.getValue("summary").stringValue();
                String releaseNotes = bindingSet.getValue("releaseNotes").stringValue();
                String category = bindingSet.getValue("applicationCategory").stringValue();
                AppCategory appCategory = AppCategory.valueOf(category);
                String datePublished = bindingSet.getValue("datePublished").stringValue();
                String dateModified = bindingSet.getValue("dateModified").stringValue();
                String version = bindingSet.getValue("softwareVersion").stringValue();
                Value featureValue = bindingSet.getValue("feature");
                String feature = featureValue != null ? featureValue.stringValue() : null;
                features.add(feature);
                app = new App(name, id, description, summary, releaseNotes, appCategory, datePublished, dateModified, version, features);
            }
        } finally {
            repository.shutDown();
        }
        
        return app;
    }

    public void createApp(App app) {
        ModelBuilder appDescription = new ModelBuilder();
        appDescription.setNamespace("schema", IRIS.root);
        appDescription.subject("schema:DigitalDocument/"+app.getIdentifier()+"-DESCRIPTION")
                .add(RDF.TYPE, IRIS.description)
                .add(IRIS.disambiguatingDescription, IRIS.createLiteral("description"))
                .add(IRIS.textProperty, IRIS.createLiteral(app.getDescription()));
        Model descriptionModel = appDescription.build();

        ModelBuilder appSummary = new ModelBuilder();
        appDescription.setNamespace("schema", IRIS.root);
        appDescription.subject("schema:DigitalDocument/"+app.getIdentifier()+"-SUMMARY")
                .add(RDF.TYPE, IRIS.digitalDocument)
                .add(IRIS.disambiguatingDescription, IRIS.createLiteral("summary"))
                .add(IRIS.textProperty, IRIS.createLiteral(app.getSummary()));
        Model summaryModel = appSummary.build();

        ModelBuilder modelBuilderApp = new ModelBuilder();
        modelBuilderApp.setNamespace("schema", IRIS.root);
        modelBuilderApp.subject("schema:MobileApplication/"+app.getIdentifier())
                .add(RDF.TYPE, IRIS.mobileApplication)
                .add(IRIS.identifier, IRIS.createLiteral(app.getIdentifier()))
                .add(IRIS.name, IRIS.createLiteral(app.getName()))
                //we connect the app to its description and it summary
                .add(IRIS.description, IRIS.createCustomIRI(IRIS.digitalDocument+"/"+app.getIdentifier()+"-DESCRIPTION"))
                .add(IRIS.summary, IRIS.createCustomIRI(IRIS.digitalDocument+"/"+app.getIdentifier()+"-SUMMARY"))
                .add(IRIS.releaseNotes, IRIS.createLiteral(app.getReleaseNotes()))
                .add(IRIS.appCategory, IRIS.createLiteral(app.getApplicationCategory().toString()))
                .add(IRIS.datePublished, IRIS.createLiteral(app.getDatePublished()))
                .add(IRIS.dateModified, IRIS.createLiteral(app.getDateModified()))
                .add(IRIS.softwareVersion, IRIS.createLiteral(app.getSoftwareVersion()));

        Iterator<String> features = app.getFeatures().iterator();
        while (features.hasNext()) {
            String nextFeature = features.next();
            modelBuilderApp.add(IRIS.keywords, IRIS.createCustomIRI(IRIS.feature+"/"+nextFeature));
        }
        Model model = modelBuilderApp.build();

        try (RepositoryConnection connection = repository.getConnection()) {
            connection.add(descriptionModel);
            connection.add(summaryModel);
            connection.add(model);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            repository.shutDown();
        }
    }

    public void updateApp(String id, App updatedApp) {
        try (RepositoryConnection connection = repository.getConnection()) {
            StringBuilder queryString = new StringBuilder();
            queryString.append("PREFIX schema: <https://schema.org/>\n");
            queryString.append("DELETE { \n");
            queryString.append("?app a schema:MobileApplication ; \n");
            queryString.append("schema:name ?name ; \n");
            queryString.append("schema:description ?desc ; \n");
            queryString.append("schema:abstract ?summ ; \n");
            queryString.append("schema:releaseNotes ?releaseNotes ; \n");
            queryString.append("schema:applicationCategory ?applicationCategory ; \n");
            queryString.append("schema:datePublished ?datePublished ; \n");
            queryString.append("schema:dateModified ?dateModified ; \n");
            queryString.append("schema:softwareVersion ?softwareVersion ; \n");
            queryString.append("schema:keywords ?feature . \n");
            queryString.append("} \n");
            queryString.append("INSERT { \n");
            queryString.append("?app a schema:MobileApplication ; \n");
            queryString.append("schema:name '"+updatedApp.getName()+"' ; \n");
            queryString.append("schema:description '"+updatedApp.getDescription()+"' ; \n");
            queryString.append("schema:abstract '"+updatedApp.getSummary()+"' ; \n");
            queryString.append("schema:releaseNotes '"+updatedApp.getReleaseNotes()+"' ; \n");
            queryString.append("schema:applicationCategory '"+updatedApp.getApplicationCategory().toString()+"' ; \n");
            queryString.append("schema:datePublished '"+updatedApp.getDatePublished()+"' ; \n");
            queryString.append("schema:dateModified '"+updatedApp.getDateModified()+"' ; \n");
            queryString.append("schema:softwareVersion '"+updatedApp.getSoftwareVersion()+"' ; \n");
            Iterator<String> features = updatedApp.getFeatures().iterator();
            while (features.hasNext()) {
                String nextFeature = features.next();
                queryString.append("schema:keywords '"+nextFeature+"' ; \n");
            }
            queryString.append("} \n");
            queryString.append("WHERE { \n");
            queryString.append("?app a schema:MobileApplication ; \n");
            queryString.append("schema:identifier '"+id+"' ; \n");
            queryString.append("schema:name ?name ; \n");
            queryString.append("schema:description ?desc ; \n");
            queryString.append("schema:abstract ?summ ; \n");
            queryString.append("schema:releaseNotes ?releaseNotes ; \n");
            queryString.append("schema:applicationCategory ?applicationCategory ; \n");
            queryString.append("schema:datePublished ?datePublished ; \n");
            queryString.append("schema:dateModified ?dateModified ; \n");
            queryString.append("schema:softwareVersion ?softwareVersion ; \n");
            queryString.append("schema:keywords ?feature . \n");
            queryString.append("} \n");

            Update update = connection.prepareUpdate(QueryLanguage.SPARQL, queryString.toString());
            update.execute();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            repository.shutDown();
        }
    }

    public void deleteApp(String id) {
        try (RepositoryConnection connection = repository.getConnection()) {
            String queryString = String.format("PREFIX schema: <https://schema.org/>\n" + "DELETE WHERE { ?user a schema:MobileApplication ; schema:identifier '%s' ; ?p ?o }", id);
            Update update = connection.prepareUpdate(QueryLanguage.SPARQL, queryString);
            update.execute();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            repository.shutDown();
        }
    }

    public void addPreferredAppIntegration(AppIntegration appIntegration) {

        ModelBuilder model = new ModelBuilder();
        model.setNamespace("schema", "https://schema.org");
        model.subject("schema:AppIntegration/"+appIntegration.getSourceApp()+"-"+appIntegration.getTargetApp())
            .add(RDF.TYPE, IRIS.appIntegration)
            .add(IRIS.identifier, appIntegration.getId())
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

    public void removeAppIntegration(AppIntegration appIntegration) {
        try (RepositoryConnection connection = repository.getConnection()) {
            String queryString = String.format("PREFIX schema: <https://schema.org/>\n" + "DELETE WHERE { ?user a schema:AppIntegration ; schema:identifier '%s' ; ?p ?o }",
            appIntegration.getSourceApp()+"-"+appIntegration.getTargetApp());
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
