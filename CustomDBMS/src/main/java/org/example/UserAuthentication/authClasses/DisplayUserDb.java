package org.example.UserAuthentication.authClasses;

import org.example.Screens.screenClasses.HomeScreen;
import org.example.UserAuthentication.authInterfaces.DisplayUserDbInterface;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DisplayUserDb implements DisplayUserDbInterface {
    @Override
    public void displayAllUsers(UserDb userDb, HomeScreen homeScreen) throws IOException {
        System.out.println("Writing user database info to output file...");

        HashMap<String, ArrayList<String>> allUsers = userDb.getUserDatabase();
        homeScreen.fileOutput.write("-------------------------------------------------------------------------------------------------");
        homeScreen.fileOutput.write("\n\t\t\t\t\tUser Database Information\n");
        for(Map.Entry<String, ArrayList<String>> user : allUsers.entrySet()){
            String userId = user.getKey();
            ArrayList<String> userData = user.getValue();
            //System.out.println("\nUser Id: " + userId + ", User Data(Password, Question/Answer): " + userData + "\n");
            homeScreen.fileOutput.write("User Id: " + userId + ", User Data[Password, Encrypted Password, Question/Answer]: " + userData + "\n");
        }

        homeScreen.fileOutput.write("\n\n");

    }
}
