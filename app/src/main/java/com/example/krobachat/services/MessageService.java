package com.example.krobachat.services;

import android.annotation.SuppressLint;
import android.util.Log;


import com.example.krobachat.interfaces.Callback;
import com.example.krobachat.model.Message;

import com.example.krobachat.model.User;
import com.example.krobachat.store.UserStore;
import com.google.gson.Gson;

import org.java_websocket.WebSocket;

import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;


import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
//import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;


public class MessageService {
    public static Socket socket;
    public static WebSocket webSocket;
    public static StompClient mStompClient;


    @SuppressLint("CheckResult")
    public static void startReadingMessages(String roomID, Callback callback) {
        try {
            mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://192.168.0.27:8080/gs-guide-websocket/websocket").withClientHeartbeat(10000);
            mStompClient.connect();
            mStompClient.topic("/room/" + roomID).subscribe(topicMessage -> {
//                System.out.println("New message: " + topicMessage.getPayload());
                String message = topicMessage.getPayload();
                System.out.println("New message: "+message);
                Message msg = new Message();
                msg.setMessage(message);
                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                msg.setCreatedAt(formatter.format(date));
                User user = new User("", "", "", "");
                msg.setSentBy(user);
                callback.methodToCallback(msg);
            });


            Log.d("message", "Live messages inited");
        } catch (Exception e) {
            System.out.println("Error: " + e.fillInStackTrace());
        }

    }

    public static void sendMessage(String message, String roomID) {
        try {
            mStompClient.send("/room/" + roomID, message).subscribe();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


}
