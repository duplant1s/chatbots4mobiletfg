package upc.edu.gessi.tfg.repositories;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import upc.edu.gessi.tfg.models.App;
import upc.edu.gessi.tfg.models.AppCategory;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@org.springframework.stereotype.Repository
public class AppRepository {
    private String repoURL = "http://localhost:7200/repositories/Chatbots4MobileTFG";
    private Repository repository;

    private final SimpleValueFactory vf = SimpleValueFactory.getInstance();


    private final IRI schemaAppClassIRI = vf.createIRI("https://schema.org/MobileApplication");
    private final IRI identifierIRI = vf.createIRI("https://schema.org/identifier");
    private final IRI nameIRI = vf.createIRI("https://schema.org/name");
    private final IRI descriptionIRI = vf.createIRI("https://schema.org/description");
    private final IRI summaryIRI = vf.createIRI("https://schema.org/abstract");
    private final IRI appCategoryIRI = vf.createIRI("https://schema.org/applicationCategory");
    private final IRI datePublishedIRI = vf.createIRI("https://schema.org/datePublished");
    private final IRI dateModifiedIRI = vf.createIRI("https://schema.org/dateModified");
    private final IRI softwareVersionIRI = vf.createIRI("https://schema.org/softwareVersion");
    private final IRI releaseNotesIRI = vf.createIRI("https://schema.org/releaseNotes");
    private final IRI keywordsIRI = vf.createIRI("https://schema.org/keywords");
    private final IRI featureIRI = vf.createIRI("https://schema.org/DefinedTerm");

    //required for the description node
    private final IRI schemaDescriptionClassIRI = vf.createIRI("https://schema.org/DigitalDocument");
    private final IRI textIRI = vf.createIRI("https://schema.org/text");
    private final IRI disambiguatingDescriptionIRI = vf.createIRI("https://schema.org/disambiguatingDescription");


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
        appDescription.setNamespace("schema", "https://schema.org/");
        appDescription.subject("schema:DigitalDocument/"+app.getIdentifier()+"-DESCRIPTION")
                .add(RDF.TYPE, schemaDescriptionClassIRI)
                .add(disambiguatingDescriptionIRI, vf.createLiteral("description"))
                .add(textIRI, vf.createLiteral(app.getDescription()));
        Model descriptionModel = appDescription.build();

        ModelBuilder appSummary = new ModelBuilder();
        appDescription.setNamespace("schema", "https://schema.org/");
        appDescription.subject("schema:DigitalDocument/"+app.getIdentifier()+"-SUMMARY")
                .add(RDF.TYPE, schemaDescriptionClassIRI)
                .add(disambiguatingDescriptionIRI, vf.createLiteral("summary"))
                .add(textIRI, vf.createLiteral(app.getSummary()));
        Model summaryModel = appSummary.build();

        ModelBuilder modelBuilderApp = new ModelBuilder();
        modelBuilderApp.setNamespace("schema", "https://schema.org/");
        modelBuilderApp.subject("schema:MobileApplication/"+app.getIdentifier())
                .add(RDF.TYPE, schemaAppClassIRI)
                .add(identifierIRI, vf.createLiteral(app.getIdentifier()))
                .add(nameIRI, vf.createLiteral(app.getName()))
                //we connect the app to its description and it summary
                .add(descriptionIRI, vf.createIRI(schemaDescriptionClassIRI+"/"+app.getIdentifier()+"-DESCRIPTION"))
                .add(summaryIRI, vf.createIRI(schemaDescriptionClassIRI+"/"+app.getIdentifier()+"-SUMMARY"))
                .add(releaseNotesIRI, vf.createLiteral(app.getReleaseNotes()))
                .add(appCategoryIRI, vf.createLiteral(app.getApplicationCategory().toString()))
                .add(datePublishedIRI, vf.createLiteral(app.getDatePublished()))
                .add(dateModifiedIRI, vf.createLiteral(app.getDateModified()))
                .add(softwareVersionIRI, vf.createLiteral(app.getSoftwareVersion()));

        Iterator<String> features = app.getFeatures().iterator();
        while (features.hasNext()) {
            String nextFeature = features.next();
            modelBuilderApp.add(keywordsIRI, vf.createIRI(featureIRI+"/"+nextFeature));
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

    //TODOOOOOOOOOOOOOOOOOOOOO
    public void updateApp(String id, App updatedApp) {
        try (RepositoryConnection connection = repository.getConnection()) {
            //String queryString = String.format("DELETE { ?user schema:email ?email ; schema:givenName ?givenName ; schema:familyName ?familyName } INSERT { ?user schema:email '%s' ; schema:givenName '%s' ; schema:familyName '%s' } WHERE { ?user a schema:Person ; schema:identifier '%s' ; schema:email ?email ; schema:givenName ?givenName ; schema:familyName ?familyName }", updatedUser.getEmail(), updatedUser.getGivenName(), updatedUser.getFamilyName(), id);
            //Update update = connection.prepareUpdate(QueryLanguage.SPARQL, queryString);
            //update.execute();
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


}
