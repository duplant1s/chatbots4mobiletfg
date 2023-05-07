package upc.edu.gessi.tfg.models;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
    private String identifier;
    private String email;
    private String givenName;
    private String familyName;
    private List<String> apps;
    private List<String> preferredFeatureIntegrations;
    private List<String> preferredParameterIntegrations;

    public User(String identifier, String email, String givenName, String familyName, List<String> apps, List<String> preferredFeatureIntegrations, List<String> preferredParameterIntegrations) {
        this.identifier = identifier;
        this.email = email;
        this.givenName = givenName;
        this.familyName = familyName;
        this.apps = apps;
        this.preferredFeatureIntegrations = preferredFeatureIntegrations;
        this.preferredParameterIntegrations = preferredParameterIntegrations;
    }
    
    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public List<String> getApps() {
        return apps;
    }

    public void setApps(List<String> apps) {
        this.apps = apps;
    }

    public void addApp(String app) {
        this.apps.add(app);
    }

    public void removeApp(String app) {
        this.apps.remove(app);
    }

    public List<String> getPreferredFeatureIntegrations() {
        return preferredFeatureIntegrations;
    }

    public void setPreferredFeatureIntegrations(List<String> preferredFeatureIntegrations) {
        this.preferredFeatureIntegrations = preferredFeatureIntegrations;
    }

    public void addPreferredFeatureIntegration(String feature) {
        this.preferredFeatureIntegrations.add(feature);
    }

    public void removePreferredFeatureIntegration(String feature) {
        this.preferredFeatureIntegrations.remove(feature);
    }

    public List<String> getPreferredParameterIntegrations() {
        return preferredParameterIntegrations;
    }

    public void setPreferredParameterIntegrations(List<String> preferredParameterIntegrations) {
        this.preferredParameterIntegrations = preferredParameterIntegrations;
    }

    public void addPreferredParameterIntegration(String parameter) {
        this.preferredParameterIntegrations.add(parameter);
    }

    public void removePreferredParameterIntegration(String parameter) {
        this.preferredParameterIntegrations.remove(parameter);
    }
}
