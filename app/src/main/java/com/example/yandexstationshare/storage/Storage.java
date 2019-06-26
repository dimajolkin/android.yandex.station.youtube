package com.example.yandexstationshare.storage;

import android.content.Context;

import com.example.yandexstationshare.api.models.Station;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class Storage<ValueObject> {

    private Context context;

    public Storage(Context context) {
        this.context = context;
    }

    public void set(String key, ValueObject object) {
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

    protected String readFile(String key) {
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

        return text.toString();
    }

    public ValueObject get(String key) {

        Type sooper = getClass().getGenericSuperclass();
        Type t = ((ParameterizedType)sooper).getActualTypeArguments()[ 0 ];

        return (ValueObject) (new Gson()).fromJson(readFile(key), t);
    }
}
