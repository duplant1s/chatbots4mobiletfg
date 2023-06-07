package upc.edu.gessi.tfg.utils;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;

public class IRIS {
    public static final String root = "https://schema.org/";

    private static final SimpleValueFactory valueFactory = SimpleValueFactory.getInstance();
    //APPS IRIs
    public static final IRI mobileApplication = valueFactory.createIRI(root+"MobileApplication");
    public static final IRI description = valueFactory.createIRI(root+"description");
    public static final IRI summary = valueFactory.createIRI(root+"abstract");
    public static final IRI appCategory = valueFactory.createIRI(root+"applicationCategory");
    public static final IRI datePublished = valueFactory.createIRI(root+"datePublished");
    public static final IRI dateModified = valueFactory.createIRI(root+"dateModified");
    public static final IRI softwareVersion = valueFactory.createIRI(root+"softwareVersion");
    public static final IRI releaseNotes = valueFactory.createIRI(root+"releaseNotes");
    public static final IRI keywords = valueFactory.createIRI(root+"keywords");

    //required for the description node
    public static final IRI digitalDocument = valueFactory.createIRI(root+"DigitalDocument");
    public static final IRI textProperty= valueFactory.createIRI(root+"text");
    public static final IRI disambiguatingDescription= valueFactory.createIRI(root+"disambiguatingDescription");

    //USER IRIs
    public static final IRI person = valueFactory.createIRI(root+"Person");
    public static final IRI email = valueFactory.createIRI(root+"email");
    public static final IRI givenName = valueFactory.createIRI(root+"givenName");
    public static final IRI familyName = valueFactory.createIRI(root+"familyName");
    public static final IRI application = valueFactory.createIRI(root+"application");

    //FEATURES IRIs
    public static final IRI feature = valueFactory.createIRI(root+"DefinedTerm");
    public static final IRI hasPart = valueFactory.createIRI(root+"hasPart");

    //PARAMETERS IRIs
    public static final IRI bool = valueFactory.createIRI(root+"Boolean");
    public static final IRI number = valueFactory.createIRI(root+"Number");
    public static final IRI text = valueFactory.createIRI(root+"Text");
    public static final IRI geoCoordinates = valueFactory.createIRI(root+"GeoCoordinates");
    public static final IRI contactPoint = valueFactory.createIRI(root+"ContactPoint");

    //FEATURE INTEGRATION IRIs
    public static final IRI featureIntegration = valueFactory.createIRI(root+"Action");

    //PARAMETER INTEGRATION IRIs
    public static final IRI parameterIntegration = valueFactory.createIRI(root+"PropertyValue");
    public static final IRI alternateName = valueFactory.createIRI(root+"alternateName");

    //APP INTEGRATION IRIs
    public static final IRI appIntegration = valueFactory.createIRI(root+"AppIntegration");

    //General properties IRIs
    public static final IRI identifier = valueFactory.createIRI(root+"identifier");
    public static final IRI name = valueFactory.createIRI(root+"name");
    public static final IRI value = valueFactory.createIRI(root+"value");
    public static final IRI source = valueFactory.createIRI(root+"source");
    public static final IRI target = valueFactory.createIRI(root+"target");

    public static IRI createCustomIRI(String iriString) {
        return valueFactory.createIRI(iriString);
    }

    public static Literal createLiteral(String iriString) {
        return valueFactory.createLiteral(iriString);
    }
    
    
}
