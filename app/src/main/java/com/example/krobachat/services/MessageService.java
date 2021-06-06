package com.example.krobachat.services;

import android.annotation.SuppressLint;
import android.util.Log;

import com.example.krobachat.interfaces.Callback;
import com.example.krobachat.model.Message;
import com.example.krobachat.model.User;
import com.example.krobachat.socket.SocketListener;
import com.example.krobachat.store.UserStore;
import com.google.gson.Gson;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;


import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import io.reactivex.*;



public class MessageService {
    public static Socket socket;
    public static WebSocket webSocket;
    public static StompClient mStompClient;



    @SuppressLint("CheckResult")
    public static void startReadingMessages(String roomID) {
        try {
            mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "http://192.168.0.27:8080/gs-guide-websocket");
            mStompClient.connect();
            mStompClient.topic("/room/"+roomID).subscribe(stompMessage -> {
                System.out.println("New message: "+ stompMessage.getPayload());
            }).dispose();



            Log.d("message", "Live messages inited");
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }

    }




    public static void sendMessage(String message, String roomID, okhttp3.Callback callback) {
        Gson gson = new Gson();
        try {
            Message msg = new Message(message, UserStore.getUser(), roomID);
            RequestBody formBody = new FormBody.Builder()
                    .add("message", message)
                    .add("sentBy", gson.toJson(UserStore.getUser()))
                    .add("roomID", roomID)
                    .build();
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("http://192.168.0.27:8080/message/send")
                    .post(formBody)
                    .build();
            Call call = client.newCall(request);
            System.out.println("Adding call to enque");
            call.enqueue(callback);
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
    }

//    public static void sendMessage(String message, String roomID) {
//        try {
//            ParseObject messageObj = new ParseObject("Message");
//            ParseObject roomObj = ParseObject.createWithoutData("Room", roomID);
//            ParseUser parseUser = ParseUser.getCurrentUser();
//            messageObj.put("sentBy", parseUser);
//            messageObj.put("room", roomObj);
//            messageObj.put("message", message);
//            messageObj.save();
//            Log.d("message", "Sent " + message + " message");
//        } catch (Exception e) {
//            System.out.println("Error: " + e.toString());
//        }
//    }

    public static void getAllMessages(String roomID, okhttp3.Callback callback) {
        try {
            RequestBody formBody = new FormBody.Builder()
                    .add("roomID", roomID)
                    .build();
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("http://192.168.0.27:8080/message/getAll")
                    .post(formBody)
                    .build();
            Call call = client.newCall(request);
            System.out.println("Adding call to enque");
            call.enqueue(callback);
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
    }

//    public static List<Message> getAllMessages(String roomID) {
//        try {
//            ParseQuery<ParseObject> query = ParseQuery.getQuery("Message");
//            List<Message> messages = new ArrayList<>();
//            ParseObject roomObject = ParseObject.createWithoutData("Room", roomID);
//            query.whereEqualTo("room", roomObject);
////            query.whereContainsAll("users", Arrays.asList(user1.getUsername(), user2.getUsername()));
//            List<ParseObject> messageObjects = query.include("sentBy").find();
//            Log.d("message", "Retrieved " + messageObjects.size() + " messages with room id:"+roomID);
//            for(int i = 0; i<messageObjects.size(); i++){
//                ParseUser parseUser = (ParseUser) messageObjects.get(i).get("sentBy");
//                User u = new User(parseUser.getUsername());
//                Log.d("message", "Looping message:" +u.toString());
//                Message message = new Message(messageObjects.get(i).get("message").toString(), u, messageObjects.get(i).getCreatedAt().toGMTString());
//                Log.d("message", "Got new message:" + message.toString());
//                messages.add(message);
//            }
//            Log.d("message", "Retrieved " + messages.toString() + " messages");
//            return messages;
//        } catch (Exception e) {
//            System.out.println("Error: " + e.toString());
//            return null;
//        }
//
//
//    }

//    public static void startReadingMessages(String roomID, Callback callback) {
//        try {
//            ParseObject roomObj = ParseObject.createWithoutData("Room", roomID);
//            ParseLiveQueryClient parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient(new URI("wss://krbulachat.b4a.io/"));
//            ParseQuery parseQuery = ParseQuery.getQuery("Message");
//            parseQuery.whereEqualTo("room", roomObj);
//            SubscriptionHandling<ParseObject> subscriptionHandling = parseLiveQueryClient.subscribe(parseQuery);
//            subscriptionHandling.handleEvent(SubscriptionHandling.Event.CREATE, new SubscriptionHandling.HandleEventCallback<ParseObject>() {
//                @Override
//                public void onEvent(ParseQuery<ParseObject> query, ParseObject object) {
//                    try {
//                        ParseUser testUser = (ParseUser) object.get("sentBy");
//                        System.out.println("Got user: " + testUser.getObjectId());
//
//                        ParseQuery<ParseUser> userQuery = ParseQuery.getQuery("_User");
//                        ParseUser parseUser = userQuery.get(testUser.getObjectId());
//                        User u = new User(parseUser.getUsername());
//                        Message message = new Message(object.get("message").toString(), u, object.getCreatedAt().toString());
//                        Log.d("message","Event Fired");
//                        Log.d("message", "Event fired and got" + object.toString() + " messages");
//                        callback.methodToCallback(message);
//                    }catch (Exception e){
//                        System.out.println("Error: "+e.toString());
//                    }
//
//                }
//            });
//            Log.d("message", "Live messages inited");
//        } catch (Exception e) {
//            System.out.println("Error: " + e.toString());
//        }
//
//    }


}
