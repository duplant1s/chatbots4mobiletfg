package upc.edu.gessi.tfg.utils;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;

public class IRIS {
    public static final String root = "https://schema.org/";

    //APPS IRIs
    public static final String mobileApplication = root+"MobileApplication";
    public static final String description = root+"description";
    public static final String summary = root+"abstract";
    public static final String appCategory = root+"applicationCategory";
    public static final String datePublished = root+"datePublished";
    public static final String dateModified = root+"dateModified";
    public static final String softwareVersion = root+"softwareVersion";
    public static final String releaseNotes = root+"releaseNotes";
    public static final String keywords = root+"keywords";

    //required for the description node
    public static final String digitalDocument = root+"DigitalDocument";
    public static final String textProperty= root+"text";
    public static final String disambiguatingDescription= root+"disambiguatingDescription";

    //USER IRIs
    public static final String person = root+"Person";
    public static final String email = root+"email";
    public static final String givenName = root+"givenName";
    public static final String familyName = root+"familyName";
    public static final String application = root+"application";

    //FEATURES IRIs
    public static final String feature = root+"DefinedTerm";
    public static final String hasPart = root+"hasPart";

    //PARAMETERS IRIs
    public static final String bool = root+"Boolean";
    public static final String number = root+"Number";
    public static final String text = root+"Text";
    public static final String geoCoordinates = root+"GeoCoordinates";
    public static final String contactPoint = root+"ContactPoint";

    //FEATURE INTEGRATION IRIs
    public static final String featureIntegration = root+"Action";

    //PARAMETER INTEGRATION IRIs
    public static final String parameterIntegration = root+"PropertyValue";

    //APP INTEGRATION IRIs
    public static final String appIntegration = root+"AppIntegration";

    //General properties IRIs
    public static final String identifier = root+"identifier";
    public static final String name = root+"name";
    public static final String value = root+"value";
    public static final String source = root+"source";
    public static final String target = root+"target";

    private static final SimpleValueFactory valueFactory = SimpleValueFactory.getInstance();

    public static IRI createIRI(String iriString) {
        return valueFactory.createIRI(iriString);
    }

    public static Literal createLiteral(String iriString) {
        return valueFactory.createLiteral(iriString);
    }
    
    
}
