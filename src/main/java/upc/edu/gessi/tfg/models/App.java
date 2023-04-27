package upc.edu.gessi.tfg.models;

import java.io.Serializable;
import java.util.List;

public class App implements Serializable {

    private String name;
    private String identifier;
    private String description;
    private String summary;
    private String releaseNotes;
    private AppCategory applicationCategory;
    private String datePublished;
    private String dateModified;
    private String softwareVersion;

    public App(String name, String identifier, String description, String summary, String releaseNotes, AppCategory applicationCategory, String datePublished, String dateModified, String softwareVersion) {
        this.name = name;
        this.identifier = identifier;
        this.description = description;
        this.summary = summary;
        this.releaseNotes = releaseNotes;
        this.applicationCategory = applicationCategory;
        this.datePublished = datePublished;
        this.dateModified = dateModified;
        this.softwareVersion = softwareVersion;
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




}