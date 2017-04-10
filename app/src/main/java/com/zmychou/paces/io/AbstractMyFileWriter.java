package com.zmychou.paces.io;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.CallSuper;
import android.widget.Toast;

import com.zmychou.paces.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Inherited;

/**
 * <pre>
 * Package    :com.zmychou.paces.io
 * Author     : zmychou
 * Create time:17-4-3
 * Last Modify:
 * Email      :zmychou@gmail.com
 * Version    :V1.0
 * description:
 * </pre>
 */
public abstract class AbstractMyFileWriter {

    protected Context mContext;

    public AbstractMyFileWriter(Context context) {
        mContext = context;
    }
    public final String save(){
        File file = openFile();
        write(file);
        return file.getPath();
    }
    protected File openFile() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File root = Environment.getExternalStorageDirectory();
            File path = new File(root,"Paces/"+getSubdirectory());
            if (!path.exists()){
                path.mkdirs();
            }
            return new File(path,getFileName());
        }
        Toast.makeText(mContext, R.string.external_device_invalid, Toast.LENGTH_SHORT).show();
        return null;
    }
    protected void write(File file) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(file);
            fw.write(getContent());
            fw.flush();
        } catch (IOException e) {
            Toast.makeText(mContext, R.string.failed_to_save, Toast.LENGTH_SHORT).show();
            return ;
        }
        finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                    //May be we should log the details ,but not now
                }
            }
        }
    }

    protected abstract String getSubdirectory();
    protected abstract String getFileName();
    protected abstract String getContent();
}
