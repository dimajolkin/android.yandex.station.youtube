package com.example.yandexstationshare.storage;

import android.content.Context;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;

public class Storage<Type> {

    private Context context;

    public Storage(Context context) {
        this.context = context;
    }

    public void set(String key, Type object) {
        String fileContents = (new Gson()).toJson(object);
        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(key, Context.MODE_PRIVATE);
            outputStream.write(fileContents.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Type get(String key) {
//        String fileContents = (new Gson()).toJson(object)//        FileOutputStream outputStream;

        StringBuilder text = new StringBuilder();
        try {
            File file = new File(context.getFilesDir(), key);

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }

            if (text.length() == 0) {
                return null;
            }

        } catch (IOException e) {
            e.printStackTrace();

        }
        return (new Gson()).fromJson(text.toString(), this.getClass().getGenericSuperclass());
    }
}
