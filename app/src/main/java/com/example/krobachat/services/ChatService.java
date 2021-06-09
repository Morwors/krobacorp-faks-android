package com.example.krobachat.services;

import android.util.Log;

import com.example.krobachat.model.User;

import com.example.krobachat.store.UserStore;
import com.google.gson.Gson;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ChatService {
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    //    public static List<User> getUsers() {
//        try {
//            ParseUser currentUser = ParseUser.getCurrentUser();
//            List<User> users = new ArrayList<>();
//            ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
//            query.whereNotEqualTo("username", currentUser.getUsername());
//            List<ParseObject> userList = query.find();
//            for (int i = 0; i < userList.size(); i++) {
//                User user = new User(userList.get(i).get("username").toString());
//                users.add(user);
//            }
//            Log.d("chat", "Retrieved " + users.size() + " users");
//            return users;
//        } catch (Exception e) {
//            System.out.println("Error: " + e.toString());
//            return null;
//        }
//    }
    public static void getUsers(int page, Callback callback) {
        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("http://192.168.0.27:8080/user/loadUsers?page=" + page)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(callback);
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
    }

//    public static String getRoom(User user) {
//        try {
//            ParseQuery<ParseObject> query = ParseQuery.getQuery("Room");
//            ParseUser parseUser = ParseUser.getCurrentUser();
//            query.whereContainsAll("users", Arrays.asList(parseUser.getUsername(), user.getUsername()));
//            List<ParseObject> roomsObjects = query.find();
//            Log.d("chat", "Retrieved " + roomsObjects.size() + " rooms");
//            if(roomsObjects.size()==0){
//                return createRoom(user);
//            }
//            return roomsObjects.get(0).getObjectId();
//        } catch (Exception e) {
//            System.out.println("Error: " + e.toString());
//            return null;
//        }
//
//
//    }

    public static void getRoom(User user, Callback callback) {
        try {
            Gson gson = new Gson();
            List<User> users = Arrays.asList(user, UserStore.getUser());

            RequestBody body = RequestBody.create(JSON, gson.toJson(users)); // new

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("http://192.168.0.27:8080/room/findRoom")
                    .post(body)
                    .build();
            Call call = client.newCall(request);
            System.out.println("Adding call to enque");
            call.enqueue(callback);
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }


    }

    public static String createRoom(User user) {
        try {
            ParseObject room = new ParseObject("Room");
            ParseUser parseUser = ParseUser.getCurrentUser();
            List<String> usernames = Arrays.asList(parseUser.getUsername(), user.getUsername());
            room.put("users", usernames);
            room.save();
            return room.getObjectId();
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
            return null;
        }

    }
}
