package chatbots4mobile_tfg.chatbots4mobiletfg.models;

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

    public String getParameter_id() {
        return parameter_id;
    }

    public void setParameter_id(String parameter_id) {
        this.parameter_id = parameter_id;
    }

    public String getParameter_name() {
        return parameter_name;
    }

    public void setParameter_name(String parameter_name) {
        this.parameter_name = parameter_name;
    }

    public String getParameter_type() {
        return parameter_type;
    }

    public void setParameter_type(String parameter_type) {
        this.parameter_type = parameter_type;
    }

    public Object getParameter_value() {
        return parameter_value;
    }

    public void setParameter_value(Object parameter_value) {
        this.parameter_value = parameter_value;
    }
    
}
