package org.example.UserAuthentication.authInterfaces;

import org.example.Screens.screenClasses.HomeScreen;
import org.example.UserAuthentication.authClasses.UserDb;

import java.io.FileWriter;
import java.io.IOException;

public interface DisplayUserDbInterface {

    public void displayAllUsers(UserDb userDb, HomeScreen homeScreen) throws IOException;
}
