package upc.edu.gessi.tfg.models;

import java.io.Serializable;

public class Parameter implements Serializable {
    private String parameter_id;
    private String parameter_name;
    private String parameter_type;
    private Object parameter_value;

    public Parameter(String parameter_id, String parameter_name, String parameter_type, Object parameter_value) {
        this.parameter_id = parameter_id;
        this.parameter_name = parameter_name;
        this.parameter_type = parameter_type;
        this.parameter_value = parameter_value;
    }

    public String getIdentifier() {
        return parameter_id;
    }

    public void setIdentifier(String parameter_id) {
        this.parameter_id = parameter_id;
    }

    public String getName() {
        return parameter_name;
    }

    public void setName(String parameter_name) {
        this.parameter_name = parameter_name;
    }

    public String getType() {
        return parameter_type;
    }

    public void setType(String parameter_type) {
        this.parameter_type = parameter_type;
    }

    public Object getValue() {
        return parameter_value;
    }

    public void setValue(Object parameter_value) {
        this.parameter_value = parameter_value;
    }
    
}
