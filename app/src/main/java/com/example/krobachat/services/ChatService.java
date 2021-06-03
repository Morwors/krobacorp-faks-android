package com.example.krobachat.services;

import android.util.Log;

import com.example.krobachat.model.User;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ChatService {
    public static List<User> getUsers() {
        try {
            ParseUser currentUser = ParseUser.getCurrentUser();
            List<User> users = new ArrayList<>();
            ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
            query.whereNotEqualTo("username", currentUser.getUsername());
            List<ParseObject> userList = query.find();
            for (int i = 0; i < userList.size(); i++) {
                User user = new User(userList.get(i).get("username").toString());
                users.add(user);
            }
            Log.d("chat", "Retrieved " + users.size() + " users");
            return users;
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
            return null;
        }
    }

    public static String getRoom(User user) {
        try {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Room");
            ParseUser parseUser = ParseUser.getCurrentUser();
            query.whereContainsAll("users", Arrays.asList(parseUser.getUsername(), user.getUsername()));
            List<ParseObject> roomsObjects = query.find();
            Log.d("chat", "Retrieved " + roomsObjects.size() + " rooms");
            if(roomsObjects.size()==0){
                return createRoom(user);
            }
            return roomsObjects.get(0).getObjectId();
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
            return null;
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
