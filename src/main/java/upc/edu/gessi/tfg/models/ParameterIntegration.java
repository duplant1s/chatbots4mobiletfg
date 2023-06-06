package upc.edu.gessi.tfg.models;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public class ParameterIntegration implements Serializable {
    @NotBlank
    private String identifier;
    @NotBlank
    @Schema(description = "Source parameter identifier", example = "event-name", required = true)
    private String sourceParameter;
    @NotBlank
    @Schema(description = "Target parameter identifier", example = "route-name", required = true)
    private String targetParameter;

    public ParameterIntegration(String identifier, String sourceParameter, String targetParameter) {
        this.identifier = identifier;
        this.sourceParameter = sourceParameter;
        this.targetParameter = targetParameter;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String parameterIntegration_id) {
        this.identifier = parameterIntegration_id;
    }

    public String getSourceParameter() {
        return sourceParameter;
    }

    public void setSourceParameter(String sourceParameter) {
        this.sourceParameter = sourceParameter;
    }

    public String getTargetParameter() {
        return targetParameter;
    }

    public void setTargetParameter(String targetParameter) {
        this.targetParameter = targetParameter;
    }
}
