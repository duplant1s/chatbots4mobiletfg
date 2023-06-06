package upc.edu.gessi.tfg.models;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public class Parameter implements Serializable {
    @NotBlank
    @Schema(description = "Parameter identifier", example = "event-name", required = true)
    private String identifier;
    @NotBlank
    @Schema(description = "Parameter name", example = "Event name", required = true)
    private String name;
    @NotBlank
    @Schema(description = "Parameter type", required = true)
    private ParamType type;

    public Parameter(String identifier, String name, ParamType type) {
        this.identifier = identifier;
        this.name = name;
        this.type = type;
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

    public ParamType getType() {
        return type;
    }

    public void setType(ParamType type) {
        this.type = type;
    }
}
