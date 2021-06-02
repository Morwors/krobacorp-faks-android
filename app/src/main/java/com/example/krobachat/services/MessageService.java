package com.example.krobachat.services;

import android.util.Log;

import com.example.krobachat.model.Message;
import com.example.krobachat.model.User;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MessageService {
    public static void sendMessage(String message, String roomID) {
        try {
            ParseObject messageObj = new ParseObject("Message");
            ParseUser parseUser = ParseUser.getCurrentUser();
            messageObj.put("sentBy", parseUser);
            messageObj.put("room", roomID);
            messageObj.put("message", message);
            messageObj.save();
            Log.d("message", "Sent " + message + " message");
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
    }

    private static void getAllMessages(String roomID) {
        try {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Message");
            List<Message> messages = new ArrayList<>();
            query.whereEqualTo("room", roomID);
//            query.whereContainsAll("users", Arrays.asList(user1.getUsername(), user2.getUsername()));
            List<ParseObject> messageObjects = query.include("sentBy").find();
            Log.d("message", "Retrieved " + messageObjects.size() + " messages");
            for(Message message: (Message[]) messageObjects.toArray()){
                messages.add(message);
            }
            Log.d("message", "Retrieved " + messages.toString() + " messages");
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }


    }

    private static void startReadingMessages(User user1, User user2) {
        try {
            ParseObject room = new ParseObject("Room");
            List<String> usernames = Arrays.asList(user1.getUsername(), user2.getUsername());
            room.put("users", usernames);
            room.save();
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }

    }
}
