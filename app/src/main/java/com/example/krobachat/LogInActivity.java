package com.example.krobachat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.krobachat.services.UserService;

public class LogInActivity extends AppCompatActivity {
    Button loginButton;
    EditText usernameInput;
    EditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(v -> {
            login();
        });
    }

    void login(){
        UserService.loginUser(usernameInput.getText().toString(), passwordInput.getText().toString());
    }
}