package upc.edu.gessi.tfg.models;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public class AppIntegration implements Serializable {

    @Schema(description = "App integration identifier", example = "com.app.source-com.app.target" , required = false)
    private String identifier;
    @NotBlank
    @Schema(description = "Source app identifier", example = "com.app.source", required = true)
    private String sourceApp;
    @NotBlank
    @Schema(description = "Target app identifier", example = "com.app.target", required = true)
    private String targetApp;

    public AppIntegration(String source, String target) {
        this.sourceApp = source;
        this.targetApp = target;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public void setIdentifier(String identifier) {
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
