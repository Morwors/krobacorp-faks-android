package com.example.krobachat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.krobachat.adapters.UsersAdapter;
import com.example.krobachat.interfaces.ILoadmore;
import com.example.krobachat.model.User;
import com.example.krobachat.services.ChatService;
import com.example.krobachat.services.MessageService;
import com.example.krobachat.services.UserService;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class UsersListActivity extends AppCompatActivity implements UsersAdapter.OnUserClickListener, NavigationView.OnNavigationItemSelectedListener {
    List<User> users = new ArrayList<>();
    UsersAdapter adapter;
    Gson gson = new Gson();
    boolean shouldLoadMore = true;
    Handler refresh = new Handler(Looper.getMainLooper());


    Context context = this;

    int page = 0;

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

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


        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setNavigationViewListener();


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {

            case R.id.nav_logout: {
                logout();
                //do somthing
                break;
            }
        }
        //close navigation drawer
//        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setNavigationViewListener() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void logout() {
        System.out.println("Logging out");
        UserService.logout();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void notifiAdapter() {
        adapter.notifyDataSetChanged();
    }


    private void loadData() {
        try {
            if (shouldLoadMore == true) {
                shouldLoadMore = false;
                users.add(null);
                adapter.notifyItemInserted(users.size() - 1);
                List<User> tmpUsers = new ArrayList<>();
                ChatService.getUsers(page, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        System.out.println("Error: " + e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) {
                        String jsonOutput = null;
                        try {
                            jsonOutput = response.body().string();
                            Type listType = new TypeToken<List<User>>() {
                            }.getType();
                            tmpUsers.addAll(gson.fromJson(jsonOutput, listType));
                            if (tmpUsers.size() >= 10) {
                                shouldLoadMore = true;
                            }
                            refresh.post(new Runnable() {
                                public void run() {
                                    users.remove(users.size() - 1);
                                    adapter.notifyItemRemoved(users.size());
                                    addUsers(tmpUsers);
                                    notifiAdapter();
                                    page++;
                                    adapter.setLoaded();
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }

                });

            } else {
                Toast.makeText(UsersListActivity.this, "No more data to load!", Toast.LENGTH_SHORT).show();

            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

    private void addUsers(List<User> tmpUsers) {
        users.addAll(tmpUsers);
    }


    @Override
    public void onUserClick(int position) {
        Log.d("chat", "Got user: " + users.get(position).getUsername());
        System.out.println("Finding room");
        ChatService.getRoom(users.get(position), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("Error: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                System.out.println("Got data: " + response.body().string());
                String roomID = response.body().string();
//                String roomID = null;
                if (roomID != null) {
                    System.out.println("Got room: "+ roomID);
                    Intent intent = new Intent(context, MessageListActivity.class);
                    intent.putExtra("roomID", roomID);
                    startActivity(intent);
                }
            }
        });

//        System.out.println("Clicked on element: " + users.get(position));
    }
}