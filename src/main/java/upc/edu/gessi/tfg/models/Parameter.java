package upc.edu.gessi.tfg.models;

import java.io.Serializable;

public class Parameter implements Serializable {
    private String identifier;
    private String name;
    private ParamType type;
    private Object value;

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

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
    
}
