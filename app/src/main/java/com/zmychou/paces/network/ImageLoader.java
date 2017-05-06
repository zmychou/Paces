package com.zmychou.paces.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * <pre>
 * Package    :com.zmychou.paces.network
 * Author     : zmychou
 * Create time:17-5-6
 * Last Modify:
 * Email      :zmychou@gmail.com
 * Version    :V1.0
 * description:
 * </pre>
 */

public class ImageLoader extends AsyncTask<String, Void, Bitmap> {

    String url;
    Bitmap source;
    ImageView dest;
    public ImageLoader from(String imgUrl) {
        url = "http://10.42.0.1:8080/" + imgUrl;
        return this;
    }

    public static ImageLoader getOne() {
        return new ImageLoader();
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            InputStream in = url.openStream();
            BitmapFactory.Options options = new BitmapFactory.Options();
            String path = saveImage(in);
            options.inSampleSize = getInSampleSize(path, 60, 60,options);
            options.inJustDecodeBounds = false;

            source = BitmapFactory.decodeFile(path, options);
        } catch (MalformedURLException e) {

        } catch (IOException e) {}
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        dest.setImageBitmap(source);
    }

    public void load() {
        execute(url);
    }

    public ImageLoader into(ImageView view) {
        dest = view;
        return this;
    }

    private int getInSampleSize(String filePath, int reqWidth, int reqHeight,
                                BitmapFactory.Options options) {
        int inSampleSize = 1;
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        while (options.outHeight > reqHeight && options.outWidth > reqWidth) {
            options.outWidth /= 2;
            options.outHeight /= 2;
            inSampleSize *= 2;
        }
        options.inSampleSize = inSampleSize;
        return inSampleSize;
    }

    private String saveImage(InputStream in) {
        File file = new File(Environment.getExternalStorageDirectory()+"/Paces/cache/avatar");
        if (!file.exists()) {
            file.mkdirs();
        }

        file = new File(file , url.split("/")[3]);
        Log.e("debug", url.split("/")[0]);
        try {
            OutputStream os = new FileOutputStream(file);
            byte[] buff = new byte[1024];
            for (int len; (len = in.read(buff)) != -1; ) {
                os.write(buff, 0, len);
            }
            os.flush();
            os.close();
            in.close();
            return file.toString();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {}
        return null;
    }
}
