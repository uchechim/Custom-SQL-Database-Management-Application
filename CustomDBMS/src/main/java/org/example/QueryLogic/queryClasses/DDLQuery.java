package org.example.QueryLogic.queryClasses;

import org.example.Database.databaseClasses.SqlDatabase;
import org.example.Database.databaseClasses.Table;
import org.example.QueryLogic.queryInterfaces.DDLQueryInterface;
import org.example.Screens.screenClasses.HomeScreen;

import java.util.ArrayList;
import java.util.Scanner;

public class DDLQuery implements DDLQueryInterface {

    public Table createTable(String userQuery) {

        //String firstRow = "CREATE TABLE Account (account_id int, account_number int, account_balance double, account_name varchar(255));";

        //remove all punctuation marks that include all brackets, braces and sq. brackets, commas, colon, semicolons from user input
        userQuery = userQuery.replaceAll("[\\p{Punct}&&[^_-]]+", "");


        //Split input by 'space' delimiter.
        String[] parsedUserInput = userQuery.split(" ");

        //Extract table name from input
        String tableName = parsedUserInput[2];


        //Create new array list to hold column names within CREATE sql statement.
        ArrayList<String> userInputData = new ArrayList<>();

        //Iterate starting from first column name onwards.
        for(int i = 3; i < parsedUserInput.length; i++){
            //Huge if-statement of many common SQL data types
            if(parsedUserInput[i].contains("int") || parsedUserInput[i].contains("varchar")
            || parsedUserInput[i].contains("double") || parsedUserInput[i].contains("float")
            || parsedUserInput[i].contains("char") || parsedUserInput[i].contains("binary")
            || parsedUserInput[i].contains("bool") || parsedUserInput[i].contains("boolean")
            || parsedUserInput[i].contains("integer") || parsedUserInput[i].contains("date")
            || parsedUserInput[i].contains("datetime") || parsedUserInput[i].contains("time")
            || parsedUserInput[i].contains("timestamp") || parsedUserInput[i].contains("year")){
                continue;
            }else{
                userInputData.add(parsedUserInput[i]);
            }
        }

        //Create a new table and add new row that contains all column names.
        Table resultTable = new Table(tableName);
        resultTable.table.add(userInputData);

        System.out.println("Successfully created table: '" + tableName + "'");
        return resultTable;

    }
}
