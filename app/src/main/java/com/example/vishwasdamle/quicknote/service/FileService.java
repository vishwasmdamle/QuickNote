package com.example.vishwasdamle.quicknote.service;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by vishwasdamle on 04/04/15.
 */
public class FileService {


    public static final String FILE_ERROR = "FILE_ERROR";
    private Context context;

    public FileService(Context context) {
        this.context = context;
    }

    public File getFile(String path, String filename) {
        File file;
        if(isExternalStorageReadWritable()) {
            File dir = new File(Environment.getExternalStorageDirectory() + path);
            dir.mkdirs();
            file = new File(dir, filename);
        } else {
            file = new File(context.getFilesDir(), filename);
        }
        return file;
    }

    private boolean isExternalStorageReadWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }


    public boolean writeToFile(File file, String data) {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(data.getBytes());
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                Log.e(FILE_ERROR, e.getMessage());
                return false;
            } catch (IOException e) {
                Log.e(FILE_ERROR, e.getMessage());
                return false;
            }
            return true;
    }

    public boolean exists(String directory, String filename) {
        return getFile(directory, filename).exists();
    }
}
