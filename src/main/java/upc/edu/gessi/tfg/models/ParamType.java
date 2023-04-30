package upc.edu.gessi.tfg.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ParamType {

    @JsonProperty("Number")
    Number,
    @JsonProperty("Boolean")
    Boolean,
    @JsonProperty("Text")
    Text,
    @JsonProperty("GeoCoordinates")
    GeoCoordinates,
    @JsonProperty("ContactPoint")
    ContactPoint
}