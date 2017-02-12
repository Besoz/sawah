package com.accorpa.sawah.Handlers;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * Created by Accorpa on 11/29/2015.
 */
public class ComplexSharedPreferences {
    private String namePreferences = "XparkSharedPreference";
    private static ComplexSharedPreferences       complexPreferences;
    private final Context context;
    private final SharedPreferences preferences;
    private final SharedPreferences.Editor  editor;
    private static Gson                     GSON            = new Gson();
    Type typeOfObject    = new TypeToken<Object>(){}
            .getType();

    private ComplexSharedPreferences(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(namePreferences, 0);//context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public static ComplexSharedPreferences getComplexPreferences(Context context) {
        if (complexPreferences == null) {
            complexPreferences = new ComplexSharedPreferences(context);
        }
        return complexPreferences;
    }

    public void putObject(String key, Object object) {
        if (object == null) {
            throw new IllegalArgumentException("Object is null");
        }
        if (key.equals("") || key == null) {
            throw new IllegalArgumentException("Key is empty or null");
        }
        String dd = GSON.toJson(object);
        editor.putString(key, GSON.toJson(object));
    }

    public void commit() {
        editor.commit();
    }

    public <T> T getObject(String key, Class<T> a) {
        String gson = preferences.getString(key, null);
        if (gson == null) {
            return null;
        }
        else {
            try {
                return GSON.fromJson(gson, a);
            }
            catch (Exception e) {
                throw new IllegalArgumentException("Object stored with key "
                        + key + " is instance of other class");
            }
        }
    }

    public SharedPreferences.Editor getEditor() {
        return editor;
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }

    public void deleteObject(String key)
    {
        preferences.edit().remove(key).commit();
    }

    public boolean hasKey(String key){
        return preferences.contains(key);
    }

}
