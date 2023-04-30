package upc.edu.gessi.tfg.models;

import java.io.Serializable;

public class ParameterIntegration implements Serializable {
    private String identifier;
    private String source;
    private String target;

    public ParameterIntegration(String identifier, String source, String target) {
        this.identifier = identifier;
        this.source = source;
        this.target = target;
    }

    public String getId() {
        return identifier;
    }

    public void setId(String parameterIntegration_id) {
        this.identifier = parameterIntegration_id;
    }

    public String getSourceParameter() {
        return source;
    }

    public void setSourceParameter(String sourceParameter) {
        this.source = sourceParameter;
    }

    public String getTargetParameter() {
        return target;
    }

    public void setTargetParameter(String targetParameter) {
        this.target = targetParameter;
    }
}
