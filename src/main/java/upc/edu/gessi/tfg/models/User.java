package upc.edu.gessi.tfg.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public class User implements Serializable {
    @NotBlank
    @Schema(description = "User identifier, typically an email", example = "example@gessi.upc.edu", required = true)
    private String identifier;
    @NotBlank
    @Schema(description = "User email", example = "example@gessi.upc.edu", required = true)
    private String email;
    @NotBlank
    @Schema(description = "User given name", example = "Quim", required = true)
    private String givenName;
    @NotBlank
    @Schema(description = "User family name", example = "Motger", required = true)
    private String familyName;
    @Schema(description = "Apps the user's uses", required = false)
    private List<String> apps;
    @Schema(description = "User' preferred integrations between two features", required = false)
    private List<String> preferredFeatureIntegrations;
    @Schema(description = "User' preferred integrations between two parameters", required = false)
    private List<String> preferredParameterIntegrations;
    @Schema(description = "User' preferred integrations between two apps", required = false)
    private List<String> preferredApps;

    public User(String identifier, String email, String givenName, String familyName) {
        this.identifier = identifier;
        this.email = email;
        this.givenName = givenName;
        this.familyName = familyName;
        this.apps = new ArrayList<>();
        this.preferredFeatureIntegrations = new ArrayList<>();
        this.preferredParameterIntegrations = new ArrayList<>();
        this.preferredApps = new ArrayList<>();
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

    public List<String> getPreferredApps() {
        return preferredApps;
    }

    public void setPreferredApps(List<String> preferredApps) {
        this.preferredApps = preferredApps;
    }

    public void addPreferredApp(String app) {
        this.preferredApps.add(app);
    }

    public void removePreferredApp(String app) {
        this.preferredApps.remove(app);
    }
}
