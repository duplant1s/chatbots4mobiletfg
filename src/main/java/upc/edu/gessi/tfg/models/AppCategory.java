package upc.edu.gessi.tfg.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum AppCategory {

    @JsonProperty("Trail Tracking")
    TRAIL_TRACKING,
    @JsonProperty("Sports Activity")
    SPORTS_ACTIVITY,
    @JsonProperty("POI Report")
    POI,
    @JsonProperty("Calendar")
    CALENDAR,
    @JsonProperty("GPS/Maps")
    GPS,
    @JsonProperty("Weather")
    WEATHER,
    @JsonProperty("Air Quality")
    AIR_QUALITY,
    @JsonProperty("Instant Messaging")
    INSTANT_MESSAGING,
    @JsonProperty("Task Manager")
    TASKS,
    @JsonProperty("Notes")
    NOTES;

}