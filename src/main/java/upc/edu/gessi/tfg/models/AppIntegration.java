package upc.edu.gessi.tfg.models;

public class AppIntegration {

    private String identifier;
    private String sourceApp;
    private String targetApp;

    public AppIntegration(String source, String target) {
        this.sourceApp = source;
        this.targetApp = target;
    }

    public String getId() {
        return this.identifier;
    }

    public void setId(String identifier) {
        this.identifier = identifier;
    }

    public String getSourceApp() {
        return this.sourceApp;
    }

    public void SetSourceApp(String sourceApp){
        this.sourceApp = sourceApp;
    }

    public String getTargetApp(){
        return this.targetApp;
    }

    public void SetTargetApp(String targetApp){
        this.targetApp = targetApp;
    }
    
}
