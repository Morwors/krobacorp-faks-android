package com.example.krobachat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.krobachat.adapters.MessageListAdapter;
import com.example.krobachat.model.Message;
import com.example.krobachat.model.User;
import com.example.krobachat.services.MessageService;

import java.util.ArrayList;
import java.util.List;

public class MessageListActivity extends AppCompatActivity {
    private RecyclerView mMessageRecycler;
    String roomID = "";
    List<Message> messages = new ArrayList<>();
    private MessageListAdapter mMessageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            roomID = extras.getString("roomID");
            getAllMessagesFromRoom();
        }

        mMessageRecycler = (RecyclerView) findViewById(R.id.recycler_gchat);
        Log.d("message", "Adding messages to adapter:" + messages);
        mMessageAdapter = new MessageListAdapter(this, messages);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
    }


    private void getAllMessagesFromRoom(){
        List<Message> msgs = MessageService.getAllMessages(roomID);
        messages.addAll(msgs);
    }
}