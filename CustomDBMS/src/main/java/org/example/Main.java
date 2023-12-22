package org.example;


import org.example.Database.databaseClasses.SqlDatabase;

import org.example.Screens.screenClasses.HomeScreen;
import org.example.UserAuthentication.authClasses.UserDb;

import java.security.NoSuchAlgorithmException;

import java.util.Scanner;

import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;  // Import the IOException class to handle errors
public class Main {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

        /****   For	any	hashing	you	can	use	standard Java library, such as md5  ****/

        System.out.println("Welcome to Uchenna's Light-Weight DBMS Program. \n");


        //Start of program

        UserDb userDatabase = new UserDb();

        SqlDatabase sqlDatabase = new SqlDatabase();

        FileWriter fileOutput = new FileWriter("CustomDBMSOutput.txt");

        HomeScreen homeScreen = new HomeScreen(userDatabase, sqlDatabase, fileOutput);

        Boolean terminateProgram = false;

        do{

            //myWriter.write("Files in Java might be tricky, but it is fun enough!\nnewline haha");
            //myWriter.close();


            System.out.println("Welcome to the landing Page\n");

            System.out.println("\nPlease enter one of the following options:\nHome\nTerminate\n");
            Scanner sc = new Scanner(System.in);
            String userInput = sc.nextLine();

            switch(userInput){
                case "Home":
                    homeScreen.invokeHomeScreen(homeScreen);
                    break;
                case "Terminate":
                    System.out.println("Terminating Program");
                    fileOutput.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Choose a correct option\n");
                    break;
                }


        }while(terminateProgram == false);





    }
}