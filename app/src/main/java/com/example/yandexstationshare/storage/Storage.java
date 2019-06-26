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

public class Storage<T extends Object> {

    protected Class<T> entityClass;

    private Context context;

    public Storage(Context context) {
        this.context = context;
        //Get "T" and assign it to this.entityClass
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        Type type = genericSuperclass.getActualTypeArguments()[0];
        if (type instanceof Class) {
            this.entityClass = (Class<T>) type;
        } else if (type instanceof ParameterizedType) {
            this.entityClass = (Class<T>) ((ParameterizedType)type).getRawType();
        }
    }

    public void set(String key, T object) {
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

    public T get(String key) {
        return (new Gson()).fromJson(readFile(key), entityClass);
    }
}
