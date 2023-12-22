package org.example.UserAuthentication.authClasses;

import org.example.UserAuthentication.authInterfaces.UserInterface;

import java.util.ArrayList;

public class User implements UserInterface {

    private String userId;
    private String password;

    private String questionAnswer;

    private String encryptedPassword;


    @Override
    public boolean login(String userId, String password, String questionAnswer, UserDb database) {
        ArrayList userData = database.retrieveUserData(userId);

        String userPassword = (String) userData.get(0);
        String twoFactor = (String) userData.get(1);

        if(password.equals(userPassword)){
            database.twoFactor(userId, questionAnswer);
            System.out.println("User: '" + userId + "' Successfully logged in");
            return true;
        }else{
            System.out.println("Login Failed.\n");
            return false;
        }

    }

    @Override
    public boolean register(String userId, String password, String questionAnswer, UserDb database,  String encryptedPassword) {

        ArrayList userData = database.retrieveUserData(userId);

        if(userData == null) {
            database.storeUser(userId, password, questionAnswer, encryptedPassword);
            System.out.println("Successfully Registered User.");
            return true;
        }else{
            System.out.println("User Already Exists in database. Registration Failed.\n");
            return false;
        }

    }
    @Override
    public boolean logOut() {
        System.out.println("Successfully Logged out of System.");
        return true;
    }
}
