package upc.edu.gessi.tfg.models;

import java.io.Serializable;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public class App implements Serializable {
    @NotBlank
    @Schema(description = "App name", example = "App name", required = true)
    private String name;
    @NotBlank
    @Schema(description = "App identifier", example = "com.app", required = true)
    private String identifier;
    @NotBlank
    @Schema(description = "App description", required = true)
    private String description;
    @NotBlank
    @Schema(description = "App abstract", required = true)
    private String summary;
    @NotBlank
    @Schema(description = "App release notes", required = true)
    private String releaseNotes;
    @NotBlank
    @Schema(description = "App category", required = true)
    private AppCategory applicationCategory;
    @NotBlank
    @Schema(description = "App date published", required = true)
    private String datePublished;
    @NotBlank
    @Schema(description = "App date modified", required = true)
    private String dateModified;
    @NotBlank
    @Schema(description = "App software version", required = true)
    private String softwareVersion;
    @NotBlank
    @Schema(description = "Features the app exposes", required = true)
    private List<String> features;

    public App(String name, String identifier, String description, String summary, String releaseNotes, AppCategory applicationCategory, String datePublished, String dateModified, String softwareVersion, List<String> features) {
        this.name = name;
        this.identifier = identifier;
        this.description = description;
        this.summary = summary;
        this.releaseNotes = releaseNotes;
        this.applicationCategory = applicationCategory;
        this.datePublished = datePublished;
        this.dateModified = dateModified;
        this.softwareVersion = softwareVersion;
        this.features = features;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(String datePublished) {
        this.datePublished = datePublished;
    }

    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    public String getReleaseNotes() {
        return releaseNotes;
    }

    public void setReleaseNotes(String releaseNotes) {
        this.releaseNotes = releaseNotes;
    }

    public AppCategory getApplicationCategory() {
        return applicationCategory;
    }

    public void setApplicationCategory(AppCategory applicationCategory) {
        this.applicationCategory = applicationCategory;
    }

    public String getSoftwareVersion() {
        return softwareVersion;
    }

    public void setSoftwareVersion(String softwareVersion) {
        this.softwareVersion = softwareVersion;
    }

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features.clear();
        this.features.addAll(features);
    }

    
}