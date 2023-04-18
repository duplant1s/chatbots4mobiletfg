package upc.edu.gessi.tfg.models;

import java.io.Serializable;

public class FeatureIntegration implements Serializable {
    private String featureIntegration_id;
    private String featureIntegration_name;
    private String sourceFeature;
    private String targetFeature;

    public FeatureIntegration() {
    }
    
    public FeatureIntegration(String featureIntegration_id, String featureIntegration_name, String sourceFeature, String targetFeature) {
        this.featureIntegration_id = featureIntegration_id;
        this.featureIntegration_name = featureIntegration_name;
        this.sourceFeature = sourceFeature;
        this.targetFeature = targetFeature;
    }

    public String getFeatureIntegration_id() {
        return featureIntegration_id;
    }

    public void setFeatureIntegration_id(String featureIntegration_id) {
        this.featureIntegration_id = featureIntegration_id;
    }

    public String getFeatureIntegration_name() {
        return featureIntegration_name;
    }

    public void setFeatureIntegration_name(String featureIntegration_name) {
        this.featureIntegration_name = featureIntegration_name;
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