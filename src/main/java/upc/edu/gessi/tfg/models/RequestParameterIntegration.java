package upc.edu.gessi.tfg.models;

public class RequestParameterIntegration {

    private String sourceApp;
    private String sourceFeature;
    private String targetApp;
    private String targetFeature;

    public RequestParameterIntegration(String sourceApp, String sourceFeature, String targetApp, String targetFeature) {
        this.sourceApp = sourceApp;
        this.sourceFeature = sourceFeature;
        this.targetApp = targetApp;
        this.targetFeature = targetFeature;
    }

    public String getSourceApp() {
        return sourceApp;
    }

    public void setSourceApp(String sourceApp) {
        this.sourceApp = sourceApp;
    }

    public String getSourceFeature() {
        return sourceFeature;
    }

    public void setSourceFeature(String sourceFeature) {
        this.sourceFeature = sourceFeature;
    }

    public String getTargetApp() {
        return targetApp;
    }

    public void setTargetApp(String targetApp) {
        this.targetApp = targetApp;
    }

    public String getTargetFeature() {
        return targetFeature;
    }

    public void setTargetFeature(String targetFeature) {
        this.targetFeature = targetFeature;
    }
    
}
