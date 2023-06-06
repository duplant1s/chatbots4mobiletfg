package upc.edu.gessi.tfg.models;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public class FeatureIntegration implements Serializable {
    @Schema(description = "Feature identifier", example = "com.app.source.test-com.app.target.test", required = false)
    private String identifier;
    @Schema(description = "Feature name", required = false)
    private String name;
    @NotBlank
    @Schema(description = "Source feature identifier", example = "com.app.source.test", required = true)
    private String sourceFeature;
    @NotBlank
    @Schema(description = "Target feature identifier", example = "com.app.target.test", required = true)
    private String targetFeature;

    public FeatureIntegration(String source, String target) {
        this.sourceFeature = source;
        this.targetFeature = target;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSourceFeature() {
        return sourceFeature;
    }

    public void setSourceFeature(String sourceFeature) {
        this.sourceFeature = sourceFeature;
    }

    public String getTargetFeature() {
        return targetFeature;
    }

    public void setTargetFeature(String targetFeature) {
        this.targetFeature = targetFeature;
    }
}