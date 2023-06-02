package upc.edu.gessi.tfg.models;

import java.io.Serializable;

public class FeatureIntegration implements Serializable {
    private String identifier;
    private String name;
    private String source;
    private String target;

    public FeatureIntegration(String source, String target) {
        this.source = source;
        this.target = target;
    }

    public String getId() {
        return identifier;
    }

    public void setId(String identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSourceFeature() {
        return source;
    }

    public void setSourceFeature(String sourceFeature) {
        this.source = sourceFeature;
    }

    public String getTargetFeature() {
        return target;
    }

    public void setTargetFeature(String targetFeature) {
        this.target = targetFeature;
    }
}