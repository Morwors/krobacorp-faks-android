package com.example.krobachat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.krobachat.model.User;
import com.example.krobachat.services.UserService;
import com.example.krobachat.store.UserStore;
import com.google.gson.Gson;
import com.parse.ParseUser;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LogInActivity extends AppCompatActivity {
    Button loginButton;
    Button signUp;

    Context context = this;

    EditText usernameInput;
    EditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        signUp = findViewById(R.id.gotoRegisterButton);
        loginButton.setOnClickListener(v -> {
            login();
        });
        signUp.setOnClickListener(v -> {
            signUp();
        });
    }

    void login() {
        UserService.loginUser(usernameInput.getText().toString(), passwordInput.getText().toString(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("Login error");
                Toast.makeText(LogInActivity.this, "Account does not exist on that email/username", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                String jsonOutput = response.body().string();
                UserStore.setUser(gson.fromJson(jsonOutput, User.class));
                if(UserStore.getUser()!=null){
                    UserStore.saveCurrentUser();
                    Intent intent = new Intent(context, UsersListActivity.class);
                    startActivity(intent);
                }
            }
        });
//        if(ParseUser.getCurrentUser()!=null){
//            Intent intent = new Intent(this, UsersListActivity.class);
//            startActivity(intent);
//        }else{
//
//        }
    }

    void signUp() {
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }
}