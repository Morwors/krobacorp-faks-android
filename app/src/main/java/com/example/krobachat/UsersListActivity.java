package com.example.krobachat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.krobachat.adapters.UsersAdapter;
import com.example.krobachat.interfaces.ILoadmore;
import com.example.krobachat.model.User;
import com.example.krobachat.services.ChatService;
import com.example.krobachat.services.MessageService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UsersListActivity extends AppCompatActivity implements UsersAdapter.OnUserClickListener {
    List<User> users = new ArrayList<>();
    UsersAdapter adapter;
    boolean shouldLoadMore = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);
//        getUserData();
        //Init View
        RecyclerView recyclerView = findViewById(R.id.userRecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UsersAdapter(recyclerView, this, users, this);
        recyclerView.setAdapter(adapter);
        loadData();
        //Set load more event
        adapter.setLoadmore(new ILoadmore() {
            @Override
            public void onLoadMore() {
                loadData();

            }
        });

    }

    private void loadData() {
        if (shouldLoadMore == true) {
            users.add(null);
            adapter.notifyItemInserted(users.size() - 1);
            List<User> tmpUsers = ChatService.getUsers();
            if (tmpUsers.size() < 10) {
                shouldLoadMore = false;
            }
            users.remove(users.size() - 1);
            adapter.notifyItemRemoved(users.size());
            addUsers(tmpUsers);
            adapter.notifyDataSetChanged();
            adapter.setLoaded();
        } else {
            Toast.makeText(UsersListActivity.this, "No more data to load!", Toast.LENGTH_SHORT).show();

        }
    }

    private void addUsers(List<User> tmpUsers) {
        users.addAll(tmpUsers);
    }

    private void getUserData() {
        for (int i = 0; i < 10; i++) {
            String name = UUID.randomUUID().toString();
            User user = new User(name);
            users.add(user);
        }
    }

    @Override
    public void onUserClick(int position) {
        Log.d("chat","Got user: "+users.get(position).getUsername());
        ChatService.getRoom(users.get(position));
//        System.out.println("Clicked on element: " + users.get(position));
    }
}