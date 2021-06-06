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

public class SignUp extends AppCompatActivity {
    Button signUpButton;
    Button gotoLogin;

    Context context = this;

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
        gotoLogin = findViewById(R.id.gotoLogin);
        signUpButton.setOnClickListener(v -> {
            signUp();
        });
        gotoLogin.setOnClickListener(v -> {
            gotoBack();
        });

    }

    void signUp() {
        System.out.println("Got email: " + emailInput.getText() + " and password: " + passwordInput.getText());
        UserService.registerUser(usernameInput.getText().toString(), passwordInput.getText().toString(), emailInput.getText().toString(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("Error: "+e.getMessage());
                Toast.makeText(SignUp.this, "Account already exists on that email/username", Toast.LENGTH_SHORT).show();

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
                }else{
                    Toast.makeText(SignUp.this, "Account already exists on that email/username", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    void gotoBack(){
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
    }

}