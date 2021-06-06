package com.example.krobachat.socket;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class SocketListener extends WebSocketListener {
    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        try {
            System.out.println("Started initing websocket");
            super.onOpen(webSocket, response);

            System.out.println("Socket connection successful!!: " + response.message());
        } catch (Exception e) {

            System.out.println("Error: " + e.getMessage());
        }

    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        super.onMessage(webSocket, text);
    }
}
