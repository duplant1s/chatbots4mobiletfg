package chatbots4mobile_tfg.chatbots4mobiletfg.models;

import java.io.Serializable;
import java.util.List;

public class Feature implements Serializable {
    private String feature_id;
    private String feature_name;
    private List<String> parameters;

    public Feature(String feature_id, String feature_name, List<String> parameters) {
        this.feature_id = feature_id;
        this.feature_name = feature_name;
        this.parameters = parameters;
    }

    public String getFeature_id() {
        return feature_id;
    }

    public void setFeature_id(String feature_id) {
        this.feature_id = feature_id;
    }

    public String getFeature_name() {
        return feature_name;
    }

    public void setFeature_name(String feature_name) {
        this.feature_name = feature_name;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }

    public void deleteParameter(String parameter_id) {
        this.parameters.remove(parameter_id);
    }

    public void addParameter(String parameter_id) {
        this.parameters.add(parameter_id);
    }
}
