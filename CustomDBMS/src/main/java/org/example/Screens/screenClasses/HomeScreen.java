package org.example.Screens.screenClasses;

import org.example.Database.databaseClasses.SqlDatabase;
import org.example.Screens.screenInterfaces.HomeScreenInterface;
import org.example.UserAuthentication.authClasses.DisplayUserDb;
import org.example.UserAuthentication.authClasses.User;
import org.example.UserAuthentication.authClasses.UserDb;

import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Scanner;

public class HomeScreen implements HomeScreenInterface {

    UserDb userDatabase;

    SqlDatabase sqlDatabase;
    public String username;
    public String password;
    public ArrayList userData;
    public String questionAnswer;
    public boolean isLoggedIn = false;

    public FileWriter fileOutput;


    public HomeScreen(UserDb userDatabase, SqlDatabase sqlDatabase, FileWriter fileOutput){

        this.userDatabase = userDatabase;
        this.sqlDatabase = sqlDatabase;
        this.fileOutput = fileOutput;
    }
    public boolean invokeHomeScreen(HomeScreen homeScreen) throws IOException, NoSuchAlgorithmException {
        System.out.println("Welcome to the Home Screen\n");

        do{
            System.out.println("\nPlease enter one of the following options:\nLogin\nRegister\nBack\nTerminate\n");
            Scanner sc = new Scanner(System.in);
            String userInput = sc.nextLine();

            switch (userInput){
                case "Login":
                    System.out.println("Please enter your username");
                    username = sc.nextLine();

                    System.out.println("Please enter your password");
                    password = sc.nextLine();

                    System.out.println("Please enter your question & answer for two-factor authentication");
                    questionAnswer = sc.nextLine();

                    userData = userDatabase.retrieveUserData(username);

                    if(userData == null){
                        System.out.println("Incorrect username, password or question/answer. \nPlease make sure you are registered.");
                    }else{
                        User user = new User();
                        if(user.login(username,password, questionAnswer, userDatabase)){
                            isLoggedIn = true;
                        }else {
                            System.out.println("Incorrect username, password or question/answer. \nPlease make sure you are registered.");
                        }
                    }
                    break;

                case "Register":
                    System.out.println("Please enter your user id");
                    username = sc.nextLine();

                    System.out.println("Please enter your password");
                    password= sc.nextLine();

                    System.out.println("Please enter a question & answer for two-factor authentication");
                    questionAnswer = sc.nextLine();

                    userData = userDatabase.retrieveUserData(username);

                    if(userData == null){
                        User user = new User();

                        //Encrypting password upon registration and storing it to database.
                        byte[] encryptedPassword = password.getBytes("UTF-8");
                        MessageDigest md = MessageDigest.getInstance("MD5");
                        byte[] md5Digest = md.digest(encryptedPassword);

                        //encode, byte[] to Base64  string
                        String encryptedPasswordString = Base64.getEncoder().encodeToString(md5Digest);
                       //System.out.println("Encrypted pw string: " + encryptedPasswordString);



                        user.register(username, password, questionAnswer, userDatabase, encryptedPasswordString);

                    }else{
                        System.out.println("User '" + username + "' already exists. Please register with different credentials.\n" );
                    }
                    break;

                case "Back":
                    System.out.println("Leaving home screen");
                    return false;


                case "Terminate":
                    System.out.println("Terminating Program");

                    this.fileOutput.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Choose a correct option\n");
                    break;
            }

        }while (isLoggedIn == false);

        if(isLoggedIn){

            DisplayUserDb displayUserDb = new DisplayUserDb();
            displayUserDb.displayAllUsers(userDatabase, this);

            System.out.println("Navigating to Query Screen...\n");
            QueryScreen queryScreen = new QueryScreen(this );

            queryScreen.invokeQueryScreen();
        }
        System.out.println("\nNavigating to Landing Page...\n");
        return true;
    }

}
