package com.example.krobachat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;


import com.parse.Parse;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build());
        setContentView(R.layout.activity_main);
        this.changeActivity();
    }

    void changeActivity(){
         if(ParseUser.getCurrentUser()!= null){
             Intent intent = new Intent(this, UsersListActivity.class);
             startActivity(intent);
         }else{
             Intent intent = new Intent(this, LogInActivity.class);
             startActivity(intent);
         }

    }
}