package com.example.krobachat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.krobachat.services.UserService;

public class SignUp extends AppCompatActivity {
    Button signUpButton;
    EditText emailInput;
    EditText passwordInput;
    EditText usernameInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        signUpButton = findViewById(R.id.signupButton);
        passwordInput = findViewById(R.id.editTextTextPassword);
        emailInput = findViewById(R.id.editTextTextEmailAddress);
        usernameInput = findViewById(R.id.editTextTextUsername);
        signUpButton.setOnClickListener(v -> {
            signUp();
        });

    }

    void signUp() {
        System.out.println("Got email: " + emailInput.getText() + " and password: " + passwordInput.getText());
//        String username = usernameInput.getText();
//        String email = emailInput.getText();
//        String password = passwordInput.getText();
        UserService.registerUser(usernameInput.getText().toString(), passwordInput.getText().toString(), emailInput.getText().toString());
    }

}