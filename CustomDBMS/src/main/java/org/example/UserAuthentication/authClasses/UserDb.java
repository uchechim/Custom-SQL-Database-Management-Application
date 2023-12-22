package org.example.UserAuthentication.authClasses;

import org.example.UserAuthentication.authInterfaces.UserDbInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class UserDb implements UserDbInterface {

    private HashMap<String, ArrayList<String>> userDatabase = new HashMap<String, ArrayList<String>>();

    /*
    public UserDb(HashMap userDatabase){

        this.userDatabase = userDatabase;
    }
    */

    @Override
    public boolean storeUser(String userId, String userPassword, String questionAnswer, String encryptedPassword) {

        ArrayList userData = this.retrieveUserData(userId);

        if(userData == null){
            this.userDatabase.put(userId, new ArrayList<String >(Arrays.asList(userPassword, encryptedPassword, questionAnswer)));
            System.out.println("User '" + userId + "' does not exist in the database. Successfully stored new user '" + userId +  "' to the user database");
            return true;
        }else{
            System.out.println("User '" + userId + "' already exists in the database. Please login using the correct user name and password.");
            return false;
        }
    }

    @Override
    public ArrayList retrieveUserData(String userId) {
        return this.userDatabase.get(userId);
    }

    @Override
    public boolean twoFactor(String userId, String questionAnswer) {
        ArrayList userData = this.retrieveUserData(userId);

        String questAns = (String) userData.get(1);

        if(questionAnswer.equals(questAns)){
            System.out.println("Two-Factor Auth Successful.");
            return true;
        }else{
            System.out.println("Two-Factor Auth NOT Successful. Please try logging in again.");
            return false;
        }
    }

    @Override
    public HashMap<String, ArrayList<String>> getUserDatabase() {
        return this.userDatabase;
    }

}
