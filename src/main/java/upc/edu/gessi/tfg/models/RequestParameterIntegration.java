package upc.edu.gessi.tfg.models;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public class RequestParameterIntegration implements Serializable {

    @NotBlank
    @Schema(description = "Source app identifier", example = "com.app.source", required = true)
    private String sourceApp;
    @NotBlank
    @Schema(description = "Source feature identifier", example = "addEvent", required = true)
    private String sourceFeature;
    @NotBlank
    @Schema(description = "Target app identifier", example = "com.app.target", required = true)
    private String targetApp;
    @NotBlank
    @Schema(description = "Target feature identifier", example = "sharePlan", required = true)
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
