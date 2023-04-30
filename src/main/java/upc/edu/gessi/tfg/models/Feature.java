package upc.edu.gessi.tfg.models;

import java.io.Serializable;
import java.util.List;

public class Feature implements Serializable {
    private String identifier;
    private String name;
    private List<String> parameters;

    public Feature(String identifier, String name, List<String> parameters) {
        this.identifier = identifier;
        this.name = name;
        this.parameters = parameters;
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
