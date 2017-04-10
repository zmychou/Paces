package com.zmychou.paces.database;

import com.amap.api.maps.model.LatLng;

import java.util.ArrayList;

/**
 * <pre>
 * Package    :com.zmychou.paces.database
 * Author     : zmychou
 * Create time:17-4-7
 * Last Modify:
 * Email      :zmychou@gmail.com
 * Version    :V1.0
 * description:
 * </pre>
 */
public class RunningData {

    private ArrayList<LatLng> latLngs;
    private int sequenceNumber;
    private long timestamp;
    private long startTime;
    private long finishTime;
    private float distance;
    private float duration;
    private int steps;
    private int calories;
    private String latLngFile;
    private String stepsFile;
    private static RunningData self;

    public static RunningData getInstance(){
        if (self == null){
            synchronized (RunningData.class){
                if (self == null)
                    self = new RunningData();
            }
        }
        return self;
    }
    private RunningData(){}
//    private void setAll(long timestamp, long startTime, long finishTime,
//                       float distance, float duration, int steps, int calories,
//                       String latLngFile, String stepsFile) {
//        this.sequenceNumber++;
//        this.timestamp = timestamp;
//        this.startTime = startTime;
//        this.finishTime = finishTime;
//        this.distance = distance;
//        this.duration = duration;
//        this.steps = steps;
//        this.calories = calories;
//        this.latLngFile = latLngFile;
//        this.stepsFile = stepsFile;
//    }
    public void clear() {
        this.startTime = 0;
        this.finishTime = 0;
        this.distance = 0;
        this.duration = 0;
        this.steps = 0;
        this.calories = 0;
        this.latLngFile = null;
        this.stepsFile = null;
        this.latLngs = null;
    }

//    /**
//     * A convenient for {@see setAll()} to set data
//     * @param finishTime The finish time of this mile
//     * @param steps This steps of this mile
//     * @param calories This calories of this mile
//     * @param latLngFile This file path of the record  of this mile,which is  stored in external
//     *                   storage and contain record data such as latitude,longitude e.g .
//     * @param stepsFile  This file path of the steps record  of this mile,which is  stored in
//     *                   external storage and  contain the three axis accelerator data .
//     */
//    public void setAfterPrev(long finishTime, int steps, int calories,
//                             String latLngFile, String stepsFile){
//        this.setAll(this.timestamp,this.finishTime,finishTime,1,finishTime - this.finishTime,
//                steps,calories,latLngFile,stepsFile);
//
//    }
    public ArrayList<LatLng> getLatLngs() {
        return latLngs;
    }

    public String getStepsFile() {
        return stepsFile;
    }

    public String getLatLngFile() {
        return latLngFile;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getFinishTime() {
        return finishTime;
    }

    public float getDistance() {
        return distance;
    }

    public float getDuration() {
        return duration;
    }

    public int getSteps() {
        return steps;
    }

    public int getCalories() {
        return calories;
    }

    public void setLatLngs(ArrayList<LatLng> latLngs) {
        this.latLngs = latLngs;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setFinishTime(long finishTime) {
        this.finishTime = finishTime;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public void setLatLngFile(String latLngFile) {
        this.latLngFile = latLngFile;
    }

    public void setStepsFile(String stepsFile) {
        this.stepsFile = stepsFile;
    }
}
