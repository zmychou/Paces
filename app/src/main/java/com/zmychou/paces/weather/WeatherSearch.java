package com.zmychou.paces.weather;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * <pre>
 * Package    :com.zmychou.paces.network
 * Author     : zmychou
 * Create time:17-4-3
 * Last Modify:
 * Email      :zmychou@gmail.com
 * Version    :V1.0
 * description:
 * </pre>
 */
public class WeatherSearch extends AsyncTask<String,Void,WeatherResult>{

    private InputStream mInputStream;
    private WeatherListener mListener;
    private InputStream requestWeatherFromMyServer() {
        try {
            return new URL("http://10.42.0.1:8080/Paces/weather_info/LiveWeather.json")
                    .openStream();
        } catch (MalformedURLException e) {

        } catch (IOException e) {}
        return null;
    }
    public InputStream requestWeather(String city) {

        URL u ;
        try {
            u = new URL("https://free-api.heweather.com/v5/weather?city="+city
                    +"&key=fff122ff73b5403aa6352f75b3dea57c");
            return u.openStream();
//            byte[] buffer = new byte[1024];
//            for (int i = 0; (i = inputStream.read(buffer,0,buffer.length)) > -1; ) {
//                Log.e("content",new String(buffer,0,i));
//            }
//            Log.e("content","dd");
        } catch (MalformedURLException e) {

        } catch (IOException e) {

        }
        return null;
    }

    public void searchLiveWeather(final String city) {
        this.execute(city);
    }


    public void registerWeatherListener(WeatherListener l) {
        mListener = l;
    }

    @Override
    protected WeatherResult doInBackground(String... cities) {
        mInputStream = requestWeather(cities[0]);
        WeatherResultParser parser = new WeatherResultParser();
        WeatherResult result = parser.getWeatherResult(mInputStream);
        return result;
    }

    @Override
    protected void onPostExecute(WeatherResult result) {
        super.onPostExecute(result);
        if (result != null
                && mInputStream != null && mListener != null) {
            mListener.onWeatherSearchResult(result,
                    result.getState());
        }
    }
}
