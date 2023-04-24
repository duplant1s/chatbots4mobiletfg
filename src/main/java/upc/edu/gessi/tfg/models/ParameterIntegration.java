package upc.edu.gessi.tfg.models;

import java.io.Serializable;

public class ParameterIntegration implements Serializable {
    private String parameterIntegration_id;
    private String parameterIntegration_name;
    private String sourceParameter;
    private String targetParameter;
    private Object parameterIntegration_value;
    
    public ParameterIntegration() {
    }

    public ParameterIntegration(String parameterIntegration_id, String parameterIntegration_name, String sourceParameter, String targetParameter, Object parameterIntegration_value) {
        this.parameterIntegration_id = parameterIntegration_id;
        this.parameterIntegration_name = parameterIntegration_name;
        this.sourceParameter = sourceParameter;
        this.targetParameter = targetParameter;
        this.parameterIntegration_value = parameterIntegration_value;
    }

    public String getId() {
        return parameterIntegration_id;
    }

    public void setId(String parameterIntegration_id) {
        this.parameterIntegration_id = parameterIntegration_id;
    }

    public String getName() {
        return parameterIntegration_name;
    }

    public void setName(String parameterIntegration_name) {
        this.parameterIntegration_name = parameterIntegration_name;
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

    public Object getValue() {
        return parameterIntegration_value;
    }

    public void setValue(Object parameterIntegration_value) {
        this.parameterIntegration_value = parameterIntegration_value;
    }

}
