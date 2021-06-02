package com.example.krobachat.services;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class UserService {
    public static void registerUser(String username, String password, String email) {
        try {
            ParseUser user = new ParseUser();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(password);
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        System.out.println("Successful sign in");
                    } else {
                        System.out.println("Error:" + e.toString());
                    }
                }
            });
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }

    }
    public static void loginUser(String username, String password){
        try {
            ParseUser.logIn(username, password);
            System.out.println("Successful login");
        }catch (Exception e){
            System.out.println("Error: " + e.toString());
        }
    }
    public static void logout(){
        try {
            ParseUser.logOut();
            System.out.println("Successfully logged out");
        }catch (Exception e){
            System.out.println("Error:" + e.toString());
        }
    }
}
