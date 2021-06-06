package com.example.krobachat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.krobachat.adapters.MessageListAdapter;
import com.example.krobachat.interfaces.Callback;
import com.example.krobachat.model.Message;
import com.example.krobachat.model.User;
import com.example.krobachat.services.MessageService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import okhttp3.Call;
import okhttp3.Response;

public class MessageListActivity extends AppCompatActivity {
    Handler refresh = new Handler(Looper.getMainLooper());




    private RecyclerView mMessageRecycler;
    Gson gson = new Gson();
    String roomID = "";
    List<Message> messages = new ArrayList<>();
    Button sendMessageButton;
    EditText messageEditText;
    private MessageListAdapter mMessageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
        Bundle extras = getIntent().getExtras();
        sendMessageButton = findViewById(R.id.button_gchat_send);
        messageEditText = findViewById(R.id.edit_chat_message);
        if (extras != null) {
            roomID = extras.getString("roomID");
//            getAllMessagesFromRoom();
        }

        mMessageRecycler = (RecyclerView) findViewById(R.id.recycler_gchat);
        Log.d("message", "Adding messages to adapter:" + messages);
        mMessageAdapter = new MessageListAdapter(this, messages);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
        mMessageRecycler.setAdapter(mMessageAdapter);
        sendMessageButton.setOnClickListener(v -> {
            sendMessage();
        });
//        MessageService.startReadingMessages(roomID, new Callback() {
//            @Override
//            public void methodToCallback(Message msg) {
//                messages.add(msg);
//                mMessageAdapter.notifyItemInserted(messages.size() - 1);
////                notifyItemRangeChanged(int start, int end);`
////                mMessageAdapter.notifyDataSetChanged();
//                Log.d("message", "Returned message: " + msg.getMessage());
//            }
//        });
        MessageService.startReadingMessages("60bd4c63e8bd651148bf5881");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void sendMessage() {
        String message = messageEditText.getText().toString();
        messageEditText.getText().clear();
        MessageService.sendMessage(message, roomID, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("Error: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("Sent messages: " + response.body().string());
            }
        });
    }

    private void getAllMessagesFromRoom() {
        List<Message> msgs = new ArrayList<>();
        MessageService.getAllMessages(roomID, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("Error: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("Got response");
                String jsonOutput = response.body().string();
                System.out.println("Got messages: " + jsonOutput);
                Type listType = new TypeToken<List<Message>>() {
                }.getType();
                messages.addAll(gson.fromJson(jsonOutput, listType));
                refresh.post(new Runnable() {
                    @Override
                    public void run() {
                        mMessageAdapter.notifyItemInserted(messages.size() - 1);
                        System.out.println("Loaded messages: " + messages.toString());
                        messages.addAll(msgs);
                    }
                });
            }
        });
    }
}