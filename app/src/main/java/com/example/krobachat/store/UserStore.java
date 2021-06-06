package com.example.krobachat.store;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.krobachat.model.User;
import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;

public class UserStore {

    public static User user;
    public static SharedPreferences editor;

    public static void initStore(Context context) {
        Gson gson = new Gson();
        SharedPreferences preferences =
                context.getSharedPreferences("com.example.krobachat", Context.MODE_PRIVATE);
        UserStore.editor = preferences;
        String userStr = UserStore.editor.getString("user", "");
        if(!userStr.equals("")){
            UserStore.user = gson.fromJson(userStr, User.class);
        }else{
            UserStore.user = null;
        }

    }


    public static User getUser() {
        return user;
    }

    public static void logout(){
        UserStore.setUser(null);
        editor.edit().putString("user","").apply();
    }

    public static void setUser(User user) {
        UserStore.user = user;
    }

    public static void saveCurrentUser() {
        Gson gson = new Gson();
        editor.edit().putString("user", gson.toJson(user)).apply();
    }


}
