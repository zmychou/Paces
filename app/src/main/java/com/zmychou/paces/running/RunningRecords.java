package com.zmychou.paces.running;

/**
 * Created by ming on 17-6-9.
 */
public class RunningRecords {

    private String timestamp;
    private String latLngFile;
    private String duration;
    private String distance;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getLatLngFile() {
        return latLngFile;
    }

    public void setLatLngFile(String latLngFile) {
        this.latLngFile = latLngFile;
    }

}
