package com.zmychou.paces.network;

/**
 * <pre>
 * Package    :com.zmychou.paces.network
 * Author     : zmychou
 * Create time:17-4-18
 * Last Modify:
 * Email      :zmychou@gmail.com
 * Version    :V1.0
 * description:
 * </pre>
 */

public interface WeatherListener {
    void onWeatherSearchResult(WeatherResult result, String state);
}