package org.example.UserAuthentication.authInterfaces;

import org.example.UserAuthentication.authClasses.UserDb;

public interface UserInterface {

    //This method enables a user to log-in to the database
    public boolean login(String userId, String password, String questionAnswer, UserDb userDatabase);

    //This method enables a user to register to the database
    public boolean register(String userId, String password, String questionAnswer, UserDb database, String encryptedPassword);

    //This method enables a user to log out -> transition to home screen.
    public boolean logOut();

}
