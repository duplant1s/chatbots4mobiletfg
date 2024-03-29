package upc.edu.gessi.tfg.models;

import java.io.Serializable;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public class Feature implements Serializable {
    @NotBlank
    @Schema(description = "Feature identifier", example = "PlanMeeting", required = true)
    private String identifier;
    @NotBlank
    @Schema(description = "Feature name", example = "Plan a meeting", required = true)
    private String name;
    @Schema(description = "Feature parameters", example = "[\"meeting-name\", \"meeting-date\"]", required = false)
    private List<String> parameters;

    public Feature(String identifier, String name, List<String> parameters) {
        this.identifier = identifier;
        this.name = name;
        this.parameters = parameters;
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

    public List<String> getParameters() {
        return parameters;
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }
}
