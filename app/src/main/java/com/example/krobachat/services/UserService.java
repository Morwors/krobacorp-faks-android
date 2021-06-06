package com.example.krobachat.services;

import android.widget.Toast;

import com.example.krobachat.SignUp;
import com.example.krobachat.UsersListActivity;
import com.example.krobachat.model.User;
import com.example.krobachat.store.UserStore;
import com.google.gson.Gson;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


public class UserService {
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
//    public static void registerUser(String username, String password, String email) {
//        try {
//            ParseUser user = new ParseUser();
//            user.setUsername(username);
//            user.setEmail(email);
//            user.setPassword(password);
//            user.signUpInBackground(new SignUpCallback() {
//                @Override
//                public void done(ParseException e) {
//                    if (e == null) {
//                        System.out.println("Successful sign in");
//                    } else {
//                        System.out.println("Error:" + e.toString());
//                    }
//                }
//            });
//        } catch (Exception e) {
//            System.out.println("Error: " + e.toString());
//        }
//
//    }
    public static void registerUser(String username, String password, String email, Callback callback){
        try {
            Gson gson = new Gson();
            User u = new User();
            u.setUsername(username);
            u.setPassword(password);
            u.setEmail(email);

            RequestBody body = RequestBody.create(JSON, gson.toJson(u)); // new


            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("http://192.168.0.27:8080/user/register")
                    .post(body)
                    .build();
            Call call = client.newCall(request);
            System.out.println("Adding call to enque");
            call.enqueue(callback);
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
    }
//    public static void loginUser(String username, String password){
//        try {
//            ParseUser.logIn(username, password);
//            System.out.println("Successful login");
//        }catch (Exception e){
//            System.out.println("Error: " + e.toString());
//        }
//    }
    public static void loginUser(String username, String password, Callback callback){
        try {
            Gson gson = new Gson();
            User u = new User(username, password);

            RequestBody body = RequestBody.create(JSON, gson.toJson(u)); // new
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://192.168.0.27:8080/user/login")
                    .post(body)
                    .build();
            Call call = client.newCall(request);
            System.out.println("Adding call to enque");
            call.enqueue(callback);
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
    }
    public static void logout(){
        try {
            UserStore.logout();
            System.out.println("Successfully logged out");
        }catch (Exception e){
            System.out.println("Error:" + e.toString());
        }
    }
}
