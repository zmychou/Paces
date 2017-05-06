package com.zmychou.paces.weather;

import android.app.Application;
import android.content.Context;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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

    public WeatherResult getWeatherResult(InputStream in) {
        return parser(in);
    }

    private WeatherResult parser(InputStream in) {
        WeatherResult result = null;
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
    private WeatherResult parserArray(JsonReader reader) {
        WeatherResult result = new WeatherResult();
        try {
            reader.beginArray();
            while (reader.hasNext()) {
                reader.beginObject();
                while (reader.hasNext()) {
                    switch (reader.nextName()) {
                        case "aqi":
                            parserAqi(reader, result);
                            break;
                        case "basic":
                            parserBasic(reader, result);
                            break;
                        case "now":
                            parserNow(reader, result);
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
            result.setState(WeatherResult.STATE_FAIL);
        }
        return result;
    }

    private void parserAqi(JsonReader reader, WeatherResult result) {
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
                                    result.setPm2_5(reader.nextString());
                                    Log.e("PM2.5:",result.getPm2_5());
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
            result.setState(WeatherResult.STATE_FAIL);
        }
    }


    private void parserNow(JsonReader reader, WeatherResult result) {
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
                                    result.setWeather(reader.nextString());
                                    Log.e("天气:",result.getWeather());
                                    break;
                                default:
                                    reader.skipValue();
                                    break;
                            }
                        }
                        reader.endObject();
                        break;
                    case "tmp":
                        result.setTemperature(reader.nextString());
                        Log.e("气温:",result.getTemperature());
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }
            reader.endObject();
        } catch (IOException e) {
            result.setState(WeatherResult.STATE_FAIL);
        }
    }

    private void parserBasic(JsonReader reader, WeatherResult result) {
        try {
            reader.beginObject();
            String name;
            while (reader.hasNext()) {
                name = reader.nextName();
                switch (name) {
                    case "city":
                        result.setCity(reader.nextString());
                        Log.e("城市:",result.getCity());
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }
            reader.endObject();
        } catch (IOException e) {
            result.setState(WeatherResult.STATE_FAIL);
        }
    }
}
