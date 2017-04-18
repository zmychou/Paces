package com.zmychou.paces.network;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
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
    protected WeatherResult doInBackground(String... citys) {
        mInputStream = requestWeather(citys[0]);
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
