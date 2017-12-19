package com.kpfu.khlopunov.sportgid.providers;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kpfu.khlopunov.sportgid.models.User;

import java.lang.reflect.Type;

/**
 * Created by hlopu on 30.10.2017.
 */

public class SharedPreferencesProvider {
    public static final String PREFERENCES_NAME = "PREFERENCES_NAME";
    public static final String VK_ID_PREFERENCES = "VK_ID";
    public static final String VK_NAME_PREFERENCES = "NAME_PREFEERENCES";
    public static final String USER_TOKEN_PREFERENCES ="USER_TOKEN_PREFRENCES";
    public static final String CITY_PREFERENCES ="CITY_PREFERENCES";
    public static final String USER_PREFERENCES ="USER_PREFERENCES";

    private static SharedPreferencesProvider sInstance;
    private Context context;

    public SharedPreferencesProvider(Context context) {
        this.context = context.getApplicationContext();
    }

    public static SharedPreferencesProvider getInstance(@NonNull Context context) {
        if (sInstance == null) {
            sInstance = new SharedPreferencesProvider(context.getApplicationContext());
        }
        return sInstance;
    }

    public String getVkId() {
        SharedPreferences preferences = context.getSharedPreferences(VK_ID_PREFERENCES, Context.MODE_PRIVATE);
        if (preferences.contains(PREFERENCES_NAME)) {
            Gson gson = new Gson();
            String jsonText = preferences.getString(PREFERENCES_NAME, "");
            Type listType = new TypeToken<String>() {
            }.getType();
            String vkId = gson.fromJson(jsonText, listType);
            return vkId;
        } else {
            String vkId = null;
            saveVkId(vkId);
            return vkId;
        }
    }

    public void saveVkId(String vkId) {
        SharedPreferences preferences = context.getSharedPreferences(VK_ID_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        Type listType = new TypeToken<String>() {
        }.getType();
        String jsonText = gson.toJson(vkId, listType);
        editor.putString(PREFERENCES_NAME, jsonText);
        editor.commit();
    }

    public String getVkName() {
        SharedPreferences preferences = context.getSharedPreferences(VK_NAME_PREFERENCES, Context.MODE_PRIVATE);
        if (preferences.contains(PREFERENCES_NAME)) {
            Gson gson = new Gson();
            String jsonText = preferences.getString(PREFERENCES_NAME, "");
            Type listType = new TypeToken<String>() {
            }.getType();
            String vkId = gson.fromJson(jsonText, listType);
            return vkId;
        } else {
            String vkId = null;
            saveVkId(vkId);
            return vkId;
        }
    }

    public void saveVkName(String name) {
        SharedPreferences preferences = context.getSharedPreferences(VK_NAME_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        Type listType = new TypeToken<String>() {
        }.getType();
        String jsonText = gson.toJson(name, listType);
        editor.putString(PREFERENCES_NAME, jsonText);
        editor.commit();
    }

    public String getCity() {
        SharedPreferences preferences = context.getSharedPreferences(CITY_PREFERENCES, Context.MODE_PRIVATE);
        if (preferences.contains(PREFERENCES_NAME)) {
            Gson gson = new Gson();
            String jsonText = preferences.getString(PREFERENCES_NAME, "");
            Type listType = new TypeToken<String>() {
            }.getType();
            String city = gson.fromJson(jsonText, listType);
            return city;
        } else {
            String city = null;
            saveCity(city);
            return city;
        }
    }

    public void saveCity(String city) {
        SharedPreferences preferences = context.getSharedPreferences(CITY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        Type listType = new TypeToken<String>() {
        }.getType();
        String jsonText = gson.toJson(city, listType);
        editor.putString(PREFERENCES_NAME, jsonText);
        editor.commit();
    }
    public String getUserTokken() {
        SharedPreferences preferences = context.getSharedPreferences(USER_TOKEN_PREFERENCES, Context.MODE_PRIVATE);
        if (preferences.contains(PREFERENCES_NAME)) {
            Gson gson = new Gson();
            String jsonText = preferences.getString(PREFERENCES_NAME, "");
            Type listType = new TypeToken<String>() {
            }.getType();
            String token = gson.fromJson(jsonText, listType);
            return token;
        } else {
            String token = null;
            saveUserTokken(token);
            return token;
        }
    }

    public void saveUserTokken(String userToken) {
        SharedPreferences preferences = context.getSharedPreferences(USER_TOKEN_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        Type listType = new TypeToken<String>() {
        }.getType();
        String jsonText = gson.toJson(userToken, listType);
        editor.putString(PREFERENCES_NAME, jsonText);
        editor.commit();
    }
    public void deleteUserTokken() {
        SharedPreferences preferences = context.getSharedPreferences(USER_TOKEN_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

    public User getUser() {
        SharedPreferences preferences = context.getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);
        if (preferences.contains(PREFERENCES_NAME)) {
            Gson gson = new Gson();
            String jsonText = preferences.getString(PREFERENCES_NAME, "");
            Type listType = new TypeToken<User>() {}.getType();
            User user = gson.fromJson(jsonText, listType);
            return user;
        } else {
            User user = null;
            saveUser(user);
            return user;
        }
    }

    public void saveUser(User user) {
        SharedPreferences preferences = context.getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        Type listType = new TypeToken<User>() {
        }.getType();
        String jsonText = gson.toJson(user, listType);
        editor.putString(PREFERENCES_NAME, jsonText);
        editor.commit();
    }
    public void deleteUser() {
        SharedPreferences preferences = context.getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }
}
