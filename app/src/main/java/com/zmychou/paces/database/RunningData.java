package com.zmychou.paces.database;

import com.zmychou.paces.running.Running;

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


//    public RunningData(int sequenceNumber, long timestamp, long startTime, long finishTime,
//                       int steps, int calories,
//                       String latLngFile, String stepsFile){
//        this(sequenceNumber,timestamp,startTime,1,finishTime,steps,calories,);
//    }
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
    private void setAll(long timestamp, long startTime, long finishTime,
                       float distance, float duration, int steps, int calories,
                       String latLngFile, String stepsFile) {
        this.sequenceNumber++;
        this.timestamp = timestamp;
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.distance = distance;
        this.duration = duration;
        this.steps = steps;
        this.calories = calories;
        this.latLngFile = latLngFile;
        this.stepsFile = stepsFile;
    }

    /**
     * A convenient for {@see setAll()} to set data
     * @param finishTime The finish time of this mile
     * @param steps This steps of this mile
     * @param calories This calories of this mile
     * @param latLngFile This file path of the record  of this mile,which is  stored in external
     *                   storage and contain record data such as latitude,longitude e.g .
     * @param stepsFile  This file path of the steps record  of this mile,which is  stored in
     *                   external storage and  contain the three axis accelerator data .
     */
    public void setAfterPrev(long finishTime, int steps, int calories,
                             String latLngFile, String stepsFile){
        this.setAll(this.timestamp,this.finishTime,finishTime,1,finishTime - this.finishTime,
                steps,calories,latLngFile,stepsFile);

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

}
