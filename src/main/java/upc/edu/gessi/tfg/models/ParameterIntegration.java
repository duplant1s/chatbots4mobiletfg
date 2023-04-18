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

    public String getParameterIntegration_id() {
        return parameterIntegration_id;
    }

    public void setParameterIntegration_id(String parameterIntegration_id) {
        this.parameterIntegration_id = parameterIntegration_id;
    }

    public String getParameterIntegration_name() {
        return parameterIntegration_name;
    }

    public void setParameterIntegration_name(String parameterIntegration_name) {
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

    public Object getParameterIntegration_value() {
        return parameterIntegration_value;
    }

    public void setParameterIntegration_value(Object parameterIntegration_value) {
        this.parameterIntegration_value = parameterIntegration_value;
    }

}
