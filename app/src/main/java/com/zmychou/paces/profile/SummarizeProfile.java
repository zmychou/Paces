package com.zmychou.paces.profile;

/**
 * Created by ming on 17-3-13.
 */
public class SummarizeProfile {

    private String value;
    private String key;
    private int id;
    public SummarizeProfile(String key, String value){
        this.key = key;
        this.value = value;
    }
    public String getValue(){
        return value;
    }
    public String getKey() {
        return key;
    }

    public SummarizeProfile setValue(String value) {
        this.value = value;
        return this;
    }
}
