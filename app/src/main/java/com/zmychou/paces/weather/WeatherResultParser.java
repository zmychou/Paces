package com.zmychou.paces.weather;

import android.app.Application;
import android.content.Context;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * <pre>
 * Package    :com.zmychou.paces.network
 * Author     : zmychou
 * Create time:17-4-18
 * Last Modify:
 * Email      :zmychou@gmail.com
 * Version    :V1.0
 * description:Parser the weather data ,which is fetch from
 * https://www.heweather.com/documents/api/v5/weather
 * </pre>
 */

public class WeatherResultParser  {

    public static final String TEMPERATURE = "temperature";
    public static final String CITY = "city";
    public static final String WEATHER = "weather";
    public static final String TIME = "time";
    public static final String WIND = "wind";
    public static final String WIND_SPEED = "windSpeed";
    public static final String HUMIDITY = "humidity";
    public static final String AQI = "aqi";
    public static final String PM2_5 = "pm2.5";
    public static final String PM10 = "pm10";
    public static final String NO2 = "no2";
    public static final String SO2 = "so2";
    public static final String CO = "co";
    public static final String O3 = "o3";
    public static final String SPORT = "sport";
    public static final String UV = "uv";
    public static final String QLTY = "qlty";
    public static final String STATE = "state";
    public static final String STATE_OK = "ok";
    public static final String STATE_ERROR = "error";

    public HashMap<String, String> getWeatherResult(InputStream in) {
        return parser(in);
    }

    private HashMap<String, String> parser(InputStream in) {
        HashMap<String, String> result = null;
        if (in == null) {
            return null;
        }
        JsonReader jr = new JsonReader(new InputStreamReader(in));
        try {
            jr.beginObject();
            while (jr.hasNext()) {
                switch (jr.nextName()) {
                    case "HeWeather5" :
                        result = parserArray(jr);
                        break;
                    default:jr.skipValue();
                        break;
                }
            }
            jr.endObject();
        } catch (IOException e) {
//            result.setState(WeatherResult.STATE_FAIL);
        }

        return result;
    }
    private HashMap<String, String> parserArray(JsonReader reader) {
        HashMap<String, String> resultMap = new HashMap<>();
        try {
            resultMap.put(STATE, STATE_OK);
            reader.beginArray();
            while (reader.hasNext()) {
                reader.beginObject();
                while (reader.hasNext()) {
                    switch (reader.nextName()) {
                        case "aqi":
                            parserAqi(reader, resultMap);
                            break;
                        case "basic":
                            parserBasic(reader, resultMap);
                            break;
                        case "now":
                            parserNow(reader, resultMap);
                            break;
                        case "suggestion":
                            parseSuggestion(reader, resultMap);
                            break;
                        default:
                            reader.skipValue();
                            break;
                    }
                }
                reader.endObject();
            }
            reader.endArray();
        } catch (IOException e) {
            resultMap.put(STATE, STATE_ERROR);
        }
        return resultMap;
    }

    private void parserAqi(JsonReader reader, HashMap<String, String> result) {
        try {
            reader.beginObject();
            String name;
            while (reader.hasNext()) {
                name = reader.nextName();
                switch (name) {
                    case "city":
                        reader.beginObject();
                        while (reader.hasNext()) {
                            switch (reader.nextName()) {
                                case "pm25":
                                    result.put(PM2_5, reader.nextString());
                                    break;
                                case "pm10":
                                    result.put(PM10, reader.nextString());
                                    break;
                                case "aqi":
                                    result.put(AQI, reader.nextString());
                                    break;
                                case "co":
                                    result.put(CO, reader.nextString());
                                    break;
                                case "no2":
                                    result.put(NO2, reader.nextString());
                                    break;
                                case "o3":
                                    result.put(O3, reader.nextString());
                                    break;
                                case "qlty":
                                    result.put(QLTY, reader.nextString());
                                    break;
                                case "so2":
                                    result.put(SO2, reader.nextString());
                                    break;
                                default:
                                    reader.skipValue();
                                    break;
                            }
                        }
                        reader.endObject();
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }
            reader.endObject();
        } catch (IOException e) {
            result.put(STATE, STATE_ERROR);
        }
    }


    private void parserNow(JsonReader reader, HashMap<String, String> result) {
        try {
            reader.beginObject();
            String name;
            while (reader.hasNext()) {
                name = reader.nextName();
                switch (name) {
                    case "cond":
                        reader.beginObject();
                        while (reader.hasNext()) {
                            switch (reader.nextName()) {
                                case "txt":
                                    result.put(WEATHER, reader.nextString());
                                    break;
                                default:
                                    reader.skipValue();
                                    break;
                            }
                        }
                        reader.endObject();
                        break;
                    case "tmp":
                        result.put(TEMPERATURE, reader.nextString());
                        break;
                    case "hum":
                        result.put(HUMIDITY, reader.nextString());
                        break;
                    case "wind":
                        reader.beginObject();
                        while (reader.hasNext()) {
                            switch (reader.nextName()) {
                                case "dir":
                                    result.put(WIND, reader.nextString());
                                    break;
                                case "sc":
                                    result.put(WIND_SPEED, reader.nextString());
                                    break;
                                default:reader.skipValue();
                                    break;
                            }
                        }
                        reader.endObject();
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }
            reader.endObject();
        } catch (IOException e) {
            result.put(STATE, STATE_ERROR);
        }
    }

    private void parserBasic(JsonReader reader, HashMap<String, String> result) {
        try {
            reader.beginObject();
            String name;
            while (reader.hasNext()) {
                name = reader.nextName();
                switch (name) {
                    case "city":
                        result.put(CITY, reader.nextString());
                        break;
                    case "update":
                        reader.beginObject();
                        while(reader.hasNext()) {
                            switch (reader.nextName()) {
                                case "loc":
                                    result.put(TIME, reader.nextString());
                                    break;
                                default:reader.skipValue();
                                    break;
                            }
                        }
                        reader.endObject();
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }
            reader.endObject();
        } catch (IOException e) {
            result.put(STATE, STATE_ERROR);
        }
    }

    private void parseSuggestion(JsonReader reader, HashMap<String, String> result) {
        try {
            reader.beginObject();
            while (reader.hasNext()) {
                switch (reader.nextName()) {
                    case "sport":
                        reader.beginObject();
                        while (reader.hasNext()) {
                            switch (reader.nextName()) {
                                case "brf":
                                    result.put(SPORT, reader.nextString());
                                    break;
                                case "txt":
                                    result.put(SPORT, result.get(SPORT) + "," + reader.nextString());
                                    break;
                                default:reader.skipValue();
                                    break;
                            }
                        }
                        reader.endObject();
                        break;
                    case "uv":
                        reader.beginObject();
                        while (reader.hasNext()) {
                            switch (reader.nextName()) {
                                case "brf":
                                    result.put(UV, reader.nextString());
                                    break;
                                case "txt":
                                    result.put(UV, result.get(UV) + "," + reader.nextString());
                                    break;
                                default:reader.skipValue();
                                    break;
                            }
                        }
                        reader.endObject();
                        break;
                    default:reader.skipValue();
                        break;
                }
            }
            reader.endObject();
        }catch (IOException e) {}

    }
}
