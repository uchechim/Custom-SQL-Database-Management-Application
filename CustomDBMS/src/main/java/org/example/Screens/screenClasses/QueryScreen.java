package org.example.Screens.screenClasses;

import org.example.Database.databaseClasses.DisplaySqlDatabase;
import org.example.Database.databaseClasses.Table;
import org.example.QueryLogic.queryClasses.DDLQuery;
import org.example.QueryLogic.queryClasses.DMLQuery;
import org.example.Screens.screenInterfaces.QueryScreenInterface;
import org.example.UserAuthentication.authClasses.DisplayUserDb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class QueryScreen implements QueryScreenInterface {

    HomeScreen homeScreen;

    public QueryScreen(HomeScreen homeScreen){
        this.homeScreen = homeScreen;

    }

    @Override
    public boolean invokeQueryScreen() throws IOException {
        //End of this method returns back to landing page

        System.out.println("\nWelcome to the Query Screen.");
        System.out.println("A default database named: '" + homeScreen.username + "' has been created for you.");

        Boolean terminateProgram = false;
        do{
            System.out.println("\nPlease enter one of the following actions:\nPerform Query\nLogout\nTerminate");
            Scanner sc = new Scanner(System.in);
            String userInput = sc.nextLine();

            switch(userInput){
                case "Perform Query":
                    this.performQuery();
                    break;

                case "Logout":
                    System.out.println("Logging out of account: '" + homeScreen.username +"' \n");

                    return true;

                case "Terminate":
                    System.out.println("Terminating Program...");

                    homeScreen.fileOutput.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Choose a correct option\n");
                    break;
            }
        }while(terminateProgram == false);

        return false;
    }

    @Override
    public boolean performQuery() throws IOException {


        Boolean queryExecuted = false;
        do{
            System.out.println("\nNOTE: ** For parsing purposes, make sure each clause is space-seperated. i.e - WHERE name = 'x'; or CREATE TABLE tableName; ** " +
                                "\n\nPlease enter one of the following types of queries:" +
                                "\nDDL Queries:\n\tCREATE" +
                                "\n\nDML Queries:\n\tSELECT\n\tINSERT\n\tUPDATE\n\tDELETE\n\tDisplay User DB, Display Sql DB, Display DB Tables");

            Scanner sc = new Scanner(System.in);
            String userInput = sc.nextLine();

            switch(userInput){
                case "CREATE":
                    //Get SQL Statement
                    System.out.println("Enter your 'CREATE' SQL statement: \n");
                    Scanner input = new Scanner(System.in);
                    String userQuery = input.nextLine();

                    //remove all punctuation marks that include all brackets, braces and sq. brackets, commas, colon, semicolons except underscore & hyphen from user input
                    userQuery = userQuery.replaceAll("[\\p{Punct}&&[^_-]]+", "");


                    //Split input by 'space' delimiter.
                    String[] parsedUserInput = userQuery.split(" ");

                    //Extract action name from input
                    String action = parsedUserInput[1];

                    //Sanity checks
                    if(action.equals("DATABASE")){
                        System.out.println("The 'CREATE DATABASE' command has been disabled as a default database named '"+ homeScreen.username + "' has been already been created for you." +
                                           "\nUsers may only have ONE database.\nYou can go ahead and start by creating a table.");
                        queryExecuted = true;
                        break;
                    }

                    if(!action.equals("TABLE")){
                        System.out.println("Make sure you have typed your SQL Query in the format: \nCREATE TABLE table_name (column1 datatype, column2 datatype, column3 datatype);");
                        queryExecuted = true;
                        break;
                    }

                    //Create new SQL Table
                    DDLQuery createQuery = new DDLQuery();
                    Table newTable = createQuery.createTable(userQuery);

                    //add table to sqlDb under correct user
                    homeScreen.sqlDatabase.addTableToDb(homeScreen.username, newTable);

                    homeScreen.fileOutput.write("-------------------------------------------------------------------------------------------------");
                    homeScreen.fileOutput.write("\nQuery Author: " + homeScreen.username + "\nQuery Statement: " + userQuery + "\n\n\t\t\t\t\tQuery result \n\n");
                    DisplaySqlDatabase displaySqlDatabase = new DisplaySqlDatabase();
                    displaySqlDatabase.displaySqlDatabaseTables(homeScreen.sqlDatabase, homeScreen);

                    queryExecuted = true;
                    break;

                case "SELECT":
                    DMLQuery selectQuery = new DMLQuery();
                    selectQuery.select(homeScreen.sqlDatabase, homeScreen.username, homeScreen);
                    queryExecuted = true;
                    break;

                case "INSERT":
                    DMLQuery insertQuery = new DMLQuery();
                    insertQuery.insert(homeScreen.sqlDatabase, homeScreen.username, homeScreen);
                    queryExecuted = true;
                    break;

                case "UPDATE":
                    DMLQuery updateQuery = new DMLQuery();
                    updateQuery.update(homeScreen.sqlDatabase, homeScreen.username, homeScreen);
                    queryExecuted = true;
                    break;

                case "DELETE":
                    DMLQuery deleteQuery = new DMLQuery();
                    deleteQuery.delete(homeScreen.sqlDatabase, homeScreen.username, homeScreen);
                    queryExecuted = true;
                    break;


                default:
                    System.out.println("Choose a correct option or enter 'Terminate' to terminate the program.\n");
                    break;
            }
        }while(queryExecuted == false);

        return false;
    }

}
