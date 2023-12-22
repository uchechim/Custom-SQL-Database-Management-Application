package org.example.UserAuthentication.authInterfaces;

import java.util.ArrayList;
import java.util.HashMap;

public interface UserDbInterface {

    //this function stores user to the database
    public boolean storeUser(String userId, String userPassword, String questionAnswer, String encryptedPassword);

    //This function should return an array of [userId, userPassword, questionAnswer]
    public ArrayList retrieveUserData(String userId);

    //This function performs two-factor auth when user tries to log in
    public boolean twoFactor(String userId, String questionAnswer);

    //This function returns current state of DB for printing
    public HashMap<String, ArrayList<String>> getUserDatabase();
}
