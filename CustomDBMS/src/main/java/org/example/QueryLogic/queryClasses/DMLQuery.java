package org.example.QueryLogic.queryClasses;

import org.example.Database.databaseClasses.DisplayTableData;
import org.example.Database.databaseClasses.SqlDatabase;
import org.example.Database.databaseClasses.Table;
import org.example.QueryLogic.queryInterfaces.DMLQueryInterface;
import org.example.Screens.screenClasses.HomeScreen;

import java.util.*;


import java.io.IOException;  // Import the IOException class to handle errors
public class DMLQuery implements DMLQueryInterface {
    @Override
    public void select(SqlDatabase sqlDatabase, String databaseName, HomeScreen homeScreen) throws IOException {
        /*Syntax:
            SELECT column1, column2, column_n FROM table_name;
            SELECT * FROM table_name;
            SELECT column1, column2, column_n FROM table_name WHERE condition = value;
        */

        //Get SQL Statement
        System.out.println("Enter your 'SELECT' SQL statement: \n");
        Scanner input = new Scanner(System.in);
        String userQuery = input.nextLine();

        //Defining result array list -> ArrayList<ArrayList<Strings>>
        ArrayList<ArrayList<String>> result = new ArrayList<>();

        //remove all punctuation marks that include all brackets, braces and sq. brackets, commas, colon, semicolons from user input
        userQuery = userQuery.replaceAll("[\\p{Punct}&&[^*><=_-]]+", "");


        //Split input by 'space' delimiter.
        String[] parsedUserInput = userQuery.split(" ");

        //Convert parsed data to arraylist
        ArrayList<String> parsedUserInputArrList = new ArrayList<String>(Arrays.asList(parsedUserInput));


        int fromIdx = parsedUserInputArrList.indexOf("FROM");
        int whereIdx = parsedUserInputArrList.indexOf("WHERE");
        String tableName = parsedUserInputArrList.get(fromIdx+1);

        String leftOperand = "";
        String operator = "";
        String rightOperand = "";

        if(parsedUserInputArrList.contains("WHERE")){
            leftOperand = parsedUserInputArrList.get(whereIdx+1);
            operator = parsedUserInputArrList.get(whereIdx+2);
            rightOperand = parsedUserInputArrList.get(whereIdx+3);
        }

        //Extract Column Values
        ArrayList<String> columnValues = new ArrayList<>();
        for(int i = 1; i < fromIdx; i++){
            columnValues.add(parsedUserInputArrList.get(i));
        }


        //SELECT * FROM table_name;
        if( (columnValues.size() == 1) && (columnValues.get(0)).equals("*") && (!parsedUserInputArrList.contains("WHERE"))){
            for(Map.Entry<String, ArrayList<Table>> entries : sqlDatabase.sqlDatabase.entrySet()){
                if(entries.getKey().equals(databaseName)){
                    for(Table t : sqlDatabase.sqlDatabase.get(entries.getKey())){
                        if(t.tableName.equals(tableName)){
                            for(int i = 0; i < t.table.size(); i++){
                                ArrayList<String> row_data = t.table.get(i);
                                result.add(row_data);
                            }
                        }

                    }
                }
            }

            //System.out.println("SELECT * FROM TABLE NAME QUERY RESULT: \n" );
            //System.out.println("RESULT LIST: " + result);
            System.out.println("Writing SELECT query result to output file...\n" );
            homeScreen.fileOutput.write("-------------------------------------------------------------------------------------------------");
            homeScreen.fileOutput.write("\nQuery Author: " + homeScreen.username + "\nQuery Statement: " + userQuery + "\n\n\t\t\t\t\tQuery result \n");

            for(int i = 0; i < result.size(); i++) {
                ArrayList<String> row_data = result.get(i);
                for(int k = 0; k < row_data.size(); k++){

                    //System.out.print(row_data.get(k) + " ");
                    homeScreen.fileOutput.write(row_data.get(k) + " ");
                }
                //System.out.println("\n");
                homeScreen.fileOutput.write("\n");
            }
        }


        //Iterate through the first row of the database and perform mapping of column_name -> idx (col_1=0, col_2=1, col_3=2)
        ArrayList<Table> table_data = sqlDatabase.sqlDatabase.get(databaseName);
        HashMap<String, Integer> firstRowColumnToIdxMap = new HashMap<>();
        int first_row_size = 0;
        for(Table t: table_data){
            if(t.tableName.equals(tableName)){
                ArrayList<String> first_row = t.table.get(0);
                first_row_size = first_row.size();
                for(int i = 0; i < first_row.size(); i++){
                    firstRowColumnToIdxMap.put(first_row.get(i), i);
                }
            }
        }

        //This mapping is used to know what particular column to select within a table -> idx (queryCol_1=0, queryCol_2=1, queryCol_3=2)
        HashMap<String, Integer> columnValuesToColIdxMapping = new HashMap<>();
        for(Map.Entry<String, Integer> entries : firstRowColumnToIdxMap.entrySet()){
            if(columnValues.contains(entries.getKey())){
                columnValuesToColIdxMapping.put(entries.getKey(), firstRowColumnToIdxMap.get(entries.getKey()));
            }
        }

        //this hash map represents the inverse of columnValueToColIdxMapping indices -> (0=queryCol_1, 1=queryCol_2, 3=queryCol_3)
        Map<Integer, String> inverseColToRowMap = new HashMap<>();
        for (Map.Entry<String, Integer> entry : columnValuesToColIdxMapping.entrySet()){
            inverseColToRowMap.put(Integer.valueOf(entry.getValue()), entry.getKey());
        }

        //SELECT column1, column2, column_n FROM table_name;
        if( (columnValues.size() >= 1) && !(columnValues.get(0)).equals("*") && (!parsedUserInputArrList.contains("WHERE"))) {
            for (Map.Entry<String, ArrayList<Table>> entries : sqlDatabase.sqlDatabase.entrySet()) {
                if (entries.getKey().equals(databaseName)) {
                    for (Table t : sqlDatabase.sqlDatabase.get(entries.getKey())) {
                        if (t.tableName.equals(tableName)) {
                            //go through every row of that table
                            for (int i = 0; i < t.table.size(); i++) {

                                //represents the data_point of every row within the table.
                                ArrayList<String> row_data = sqlDatabase.sqlDatabase.get(entries.getKey()).get(0).table.get(i);
                                ArrayList<String> input_row = new ArrayList<>();
                                for(int k = 0; k < row_data.size(); k++){
                                    //go through every column of that row
                                    if(inverseColToRowMap.keySet().contains(k)){
                                        input_row.add(row_data.get(k));

                                    }

                                }
                                result.add(input_row);

                            }
                        }

                    }
                }
            }

            //System.out.println("SELECT column1, column2, column_n FROM table_name QUERY RESULT: \n" );
            //System.out.println("RESULT LIST: " + result);
            System.out.println("Writing SELECT query result to output file...\n" );
            homeScreen.fileOutput.write("-------------------------------------------------------------------------------------------------");
            homeScreen.fileOutput.write("\nQuery Author: " + homeScreen.username + "\nQuery Statement: " + userQuery + "\n\n\t\t\t\t\tQuery result \n");
            for(int i = 0; i < result.size(); i++) {
                ArrayList<String> row_data = result.get(i);
                for(int k = 0; k < row_data.size(); k++){

                    //System.out.print(row_data.get(k) + " ");
                    homeScreen.fileOutput.write(row_data.get(k) + " ");
                }
                //System.out.println("\n");
                homeScreen.fileOutput.write("\n");
            }
        }


        //SELECT column1, column2, column_n FROM table_name WHERE condition = value;
        //
        if( (columnValues.size() >= 1) && (parsedUserInputArrList.contains("WHERE"))) {
            for (Map.Entry<String, ArrayList<Table>> entries : sqlDatabase.sqlDatabase.entrySet()) {

                if (entries.getKey().equals(databaseName)) {
                    for (Table t : sqlDatabase.sqlDatabase.get(entries.getKey())) {

                        if (t.tableName.equals(tableName)) {
                            //go through every row of that table
                            for (int i = 1; i < t.table.size(); i++) {

                                //represents the data_point of every row within the table.
                                ArrayList<String> row_data = sqlDatabase.sqlDatabase.get(entries.getKey()).get(0).table.get(i);

                                ArrayList<String> input_row = new ArrayList<>();


                                //go through every column of row_data and build output based on column values
                                for(int k = 0; k < row_data.size(); k++){

                                    if(!columnValues.get(0).equals("*")){

                                        //(0=queryCol_1, 1=queryCol_2, 3=queryCol_3)

                                        if(inverseColToRowMap.keySet().contains(k)){
                                            switch(operator){
                                                case "=":
                                                    if(row_data.get(firstRowColumnToIdxMap.get(leftOperand)).equals(rightOperand)){
                                                        for(int j = 0; j < row_data.size(); j++){
                                                            if(j == k){
                                                                input_row.add(row_data.get(j));
                                                            }

                                                        }

                                                    }
                                                    break;
                                            }


                                        }
                                    }

                                }

                                result.add(input_row);
                            }
                        }

                    }
                }
            }

            //System.out.println("SELECT column1, column2, column_n FROM table_name WHERE condition = value; AND SELECT * FROM table_name WHERE condition = value; QUERY RESULT: \n" );

            System.out.println("Writing SELECT query result to output file...\n" );
            homeScreen.fileOutput.write("-------------------------------------------------------------------------------------------------");
            homeScreen.fileOutput.write("\nQuery Author: " + homeScreen.username + "\nQuery Statement: " + userQuery + "\n\n\t\t\t\t\tQuery result \n");

            for(int i = 0; i < result.size(); i++) {
                ArrayList<String> row_data = result.get(i);
                for(int k = 0; k < row_data.size(); k++){

                    //System.out.print(row_data.get(k) + " ");
                    homeScreen.fileOutput.write(row_data.get(k) + " ");
                }
                //System.out.println("\n");
                homeScreen.fileOutput.write("\n");
            }
        }

    }


    @Override
    public void insert(SqlDatabase sqlDatabase, String databaseName, HomeScreen homeScreen) throws IOException {

        //Get SQL Statement
        System.out.println("Enter your 'INSERT' SQL statement: \n");
        Scanner input = new Scanner(System.in);
        String userQuery = input.nextLine();

        //remove all punctuation marks that include all brackets, braces and sq. brackets, commas, colon, semicolons from user input
        userQuery = userQuery.replaceAll("[\\p{Punct}&&[^_-]]+", "");


        //Split input by 'space' delimiter.
        String[] parsedUserInput = userQuery.split(" ");

        //Extract action name from input
        String tableName = parsedUserInput[2];

        String decider = parsedUserInput[3];

        ArrayList<String> data = new ArrayList<>();

        if(decider.equals("VALUES")){
            for(int i = 4; i < parsedUserInput.length; i++){
                data.add(parsedUserInput[i]);
            }

            for(Map.Entry<String, ArrayList<Table>> entry: sqlDatabase.sqlDatabase.entrySet()){
                if(entry.getKey().equals(databaseName)){
                    int i = 0;
                    for(Table t : sqlDatabase.sqlDatabase.get(databaseName)){
                        if(t.tableName.equals(tableName)){

                            DisplayTableData writeTableData = new DisplayTableData();

                            homeScreen.fileOutput.write("-------------------------------------------------------------------------------------------------");
                            homeScreen.fileOutput.write("\nQuery Author: " + homeScreen.username + "\nQuery Statement: " + userQuery + "\n\n\t\t\t\t\tQuery result \n");
                            homeScreen.fileOutput.write("'"+tableName + "' Table BEFORE 'INSERT' query\n");
                            writeTableData.writeTableToDb(sqlDatabase, databaseName, tableName, homeScreen);

                            sqlDatabase.sqlDatabase.get(databaseName).get(i).table.add(data);
                            System.out.println("Successfully added row to table: " + tableName);
                            System.out.println("Writing query result to output file... ");


                            homeScreen.fileOutput.write("'"+tableName + "' Table AFTER 'INSERT' query\n");
                            writeTableData.writeTableToDb(sqlDatabase, databaseName, tableName, homeScreen);


                        }
                        i++;
                    }
                }
            }

        }else{
            //do some more parsing.
            //Get the index of "VALUES" delimiter
            int values_idx = 0;

            for(int i = 0; i < parsedUserInput.length; i++){
                if(parsedUserInput[i].equals("VALUES")){
                    values_idx = i;
                }
            }

            //Extract Values data.
            ArrayList<String> valuesData = new ArrayList<>();
            for(int i = values_idx+1; i < parsedUserInput.length; i++){
                valuesData.add(parsedUserInput[i]);
            }

            //Extract Column data.
            ArrayList<String> columnData = new ArrayList<>();
            for(int i = 3; i < values_idx; i++){
                columnData.add(parsedUserInput[i]);
            }

            //This hash map represents a mapping for the first row col names to their respective indices. i.e (Key = Col1_name, Val = Col1_idx);
            HashMap<String, Integer> firstRowColumnToIdxMap = new HashMap<>();

            //Iterate through the first row of the database and perform mapping of column_name -> idx
            int first_row_size = 0;
            ArrayList<Table> table_data = sqlDatabase.sqlDatabase.get(databaseName);
            for(Table t: table_data){
                if(t.tableName.equals(tableName)){
                    ArrayList<String> first_row = t.table.get(0);
                    first_row_size = first_row.size();
                    for(int i = 0; i < first_row.size(); i++){
                        firstRowColumnToIdxMap.put(first_row.get(i), i);
                    }
                }
            }

            //System.out.println("Column to idx Mapping: " + firstRowColumnToIdxMap.toString());
            //System.out.println("Column Data: " + columnData.toString() + ", Value Data: " + valuesData.toString() + "\n");

            //Create a column to value mapping
            HashMap<String, String> columnToValueMap = new HashMap<>();

            for(int i = 0; i < columnData.size()-1; i++){
                columnToValueMap.put(columnData.get(i), valuesData.get(i));
            }
            //System.out.println("Column to data Mapping: " + columnToValueMap.toString());


            //Create new arraylist of length of first row size populated with null values.
            ArrayList<String> result = new ArrayList<>();
            for(int i = 0; i < first_row_size; i++){
                result.add("null");
            }

            //Iterate through column data (column names) and append to the correct column the corresponding data
            for(int i = 0; i < columnData.size()-1; i++){
                //if our column to idx mapping contains one of the columns inputted by the user, then add the data(col to data map) to the correct column of to-be-inserted row
                Set colToIdxKeys = firstRowColumnToIdxMap.keySet();
                if(colToIdxKeys.contains(columnData.get(i))){
                    result.set(firstRowColumnToIdxMap.get(columnData.get(i)), columnToValueMap.get(columnData.get(i)));
                }
            }

            //Display
            //System.out.println("Row Result: "+result.toString());

            //Add row result to correct table.
            for(Map.Entry<String, ArrayList<Table>> entry: sqlDatabase.sqlDatabase.entrySet()){
                if(entry.getKey().equals(databaseName)){
                    int i = 0;
                    for(Table t : sqlDatabase.sqlDatabase.get(databaseName)){
                        if(t.tableName.equals(tableName)){
                            DisplayTableData writeTableData = new DisplayTableData();

                            homeScreen.fileOutput.write("-------------------------------------------------------------------------------------------------");
                            homeScreen.fileOutput.write("\nQuery Author: " + homeScreen.username + "\nQuery Statement: " + userQuery + "\n\n\t\t\t\t\tQuery result \n");
                            homeScreen.fileOutput.write("'"+tableName + "' Table BEFORE 'INSERT' query\n");
                            writeTableData.writeTableToDb(sqlDatabase, databaseName, tableName, homeScreen);

                            sqlDatabase.sqlDatabase.get(databaseName).get(i).table.add(result);
                            System.out.println("Successfully added row to table: " + tableName);
                            System.out.println("Writing query result to output file... ");


                            homeScreen.fileOutput.write("'"+tableName + "' Table AFTER 'INSERT' query\n");
                            writeTableData.writeTableToDb(sqlDatabase, databaseName, tableName, homeScreen);
                        }
                        i++;
                    }
                }
            }


        }
    }

    @Override
    public void delete(SqlDatabase sqlDatabase, String databaseName, HomeScreen homeScreen) throws IOException {

        /*
            Two types of DELETE queries
            1 - DELETE FROM table_name WHERE condition; -> delete specific record/row
            2 - DELETE FROM table_name; -> delete all rows from table.
        */

        //Get SQL Statement
        System.out.println("Enter your 'DELETE' SQL statement: \n");
        Scanner input = new Scanner(System.in);
        String userQuery = input.nextLine();

        //remove all punctuation marks that include all brackets, braces and sq. brackets, commas, colon, semicolons from user input
        userQuery = userQuery.replaceAll("[\\p{Punct}&&[^><=_-]]+", "");

        //System.out.println("User Query: " + userQuery);

        //Split input by 'space' delimiter.
        String[] parsedUserInput = userQuery.split(" ");

        //Extract action name from input
        String tableName = parsedUserInput[2];

        //if length of query is 3 then we can delete all records from the specific table
        if(parsedUserInput.length == 3){
            for(Map.Entry<String, ArrayList<Table>> entries: sqlDatabase.sqlDatabase.entrySet()){

                //found correct db to delete from
                if(entries.getKey().equals(databaseName)){

                    //find correct table and delete all records
                    int tableIdx = 0;
                    for(Table t : sqlDatabase.sqlDatabase.get(entries.getKey())){
                        int current_table_size = sqlDatabase.sqlDatabase.get(entries.getKey()).get(tableIdx).table.size();

                        if(t.tableName.equals(tableName)){
                            for(int i = 1; i < current_table_size; i++){

                                //gets specific table, gets specific row in that table and clears it.
                                sqlDatabase.sqlDatabase.get(entries.getKey()).get(tableIdx).table.get(i).clear();

                            }
                        }
                        tableIdx++;
                    }
                    System.out.println("Successfully deleted ALL rows from table: " + tableName);
                    System.out.println("Writing query result to output file... ");

                    DisplayTableData writeTableData = new DisplayTableData();

                    homeScreen.fileOutput.write("-------------------------------------------------------------------------------------------------");
                    homeScreen.fileOutput.write("\nQuery Author: " + homeScreen.username + "\nQuery Statement: " + userQuery + "\n\n\t\t\t\t\tQuery result \n");
                    homeScreen.fileOutput.write("'"+tableName + "' Table BEFORE 'DELETE' query\n");
                    writeTableData.writeTableToDb(sqlDatabase, databaseName, tableName, homeScreen);

                    homeScreen.fileOutput.write("'"+tableName + "' Table AFTER 'DELETE' query\n");
                    writeTableData.writeTableToDb(sqlDatabase, databaseName, tableName, homeScreen);
                }
            }
        }else{
            //do some more parsing

            String leftOperand = parsedUserInput[4];
            String operator = parsedUserInput[5];
            String rightOperand = parsedUserInput[6];

            //This hash map represents a mapping for the first row col names to their respective indices. i.e (Key = Col1_name, Val = Col1_idx);
            HashMap<String, Integer> firstRowColumnToIdxMap = new HashMap<>();

            //Iterate through the first row of the database and perform mapping of column_name -> idx
            ArrayList<Table> table_data = sqlDatabase.sqlDatabase.get(databaseName);
            for(Table t: table_data){
                if(t.tableName.equals(tableName)){
                    ArrayList<String> first_row = t.table.get(0);
                    for(int i = 0; i < first_row.size(); i++){
                        firstRowColumnToIdxMap.put(first_row.get(i), i);
                    }
                }
            }

            int leftOperandIdx = firstRowColumnToIdxMap.get(leftOperand);

            //iterate through table data structures
            for(Map.Entry<String, ArrayList<Table>> entries: sqlDatabase.sqlDatabase.entrySet()){

                //found correct db to delete from
                if(entries.getKey().equals(databaseName)){

                    //find correct table and delete all records
                    int tableIdx = 0;
                    for(Table t : sqlDatabase.sqlDatabase.get(entries.getKey())){
                        int current_table_size = sqlDatabase.sqlDatabase.get(entries.getKey()).get(tableIdx).table.size();

                        if(t.tableName.equals(tableName)){
                            //iterate through every row in the table and delete records that match leftOperand + operator + rightOperand

                            for(int i = 1; i < current_table_size; i++){
                                //System.out.println("I value: " + i);
                                switch(operator){
                                    case "=":
                                        if (sqlDatabase.sqlDatabase.get(entries.getKey()).get(tableIdx).table.get(i).size() == 0){
                                            continue;
                                        }else
                                        //if the value of the row at the leftOperand index of a specific row is equal to the right operand, then delete that row.
                                        if(sqlDatabase.sqlDatabase.get(entries.getKey()).get(tableIdx).table.get(i).get(leftOperandIdx).equals(rightOperand)){

                                            DisplayTableData writeTableData = new DisplayTableData();

                                            homeScreen.fileOutput.write("-------------------------------------------------------------------------------------------------");
                                            homeScreen.fileOutput.write("\nQuery Author: " + homeScreen.username + "\nQuery Statement: " + userQuery + "\n\n\t\t\t\t\tQuery result \n");
                                            homeScreen.fileOutput.write("'"+tableName + "' Table BEFORE 'DELETE' query\n");
                                            writeTableData.writeTableToDb(sqlDatabase, databaseName, tableName, homeScreen);

                                            //gets specific table, gets specific row in that table and clears it.
                                            sqlDatabase.sqlDatabase.get(entries.getKey()).get(tableIdx).table.get(i).clear();

                                            System.out.println("Successfully removed one record from table: '" + tableName + "' using the '=' operator");
                                            System.out.println("Writing query result to output file... ");

                                            homeScreen.fileOutput.write("'"+tableName + "' Table AFTER 'DELETE' query\n");
                                            writeTableData.writeTableToDb(sqlDatabase, databaseName, tableName, homeScreen);
                                        }
                                    break;

                                    case "<":
                                        if (sqlDatabase.sqlDatabase.get(entries.getKey()).get(tableIdx).table.get(i).size() == 0){
                                            continue;
                                        }else
                                        //if the value of the row at the leftOperand index of a specific row is less than the right operand, then delete that row.
                                        if((Integer.parseInt(sqlDatabase.sqlDatabase.get(entries.getKey()).get(tableIdx).table.get(i).get(leftOperandIdx)) < Integer.parseInt(rightOperand))){
                                            DisplayTableData writeTableData = new DisplayTableData();

                                            homeScreen.fileOutput.write("-------------------------------------------------------------------------------------------------");
                                            homeScreen.fileOutput.write("\nQuery Author: " + homeScreen.username + "\nQuery Statement: " + userQuery + "\n\n\t\t\t\t\tQuery result \n");
                                            homeScreen.fileOutput.write("'"+tableName + "' Table BEFORE 'DELETE' query\n");
                                            writeTableData.writeTableToDb(sqlDatabase, databaseName, tableName, homeScreen);

                                            //gets specific table, gets specific row in that table and clears it.
                                            sqlDatabase.sqlDatabase.get(entries.getKey()).get(tableIdx).table.get(i).clear();

                                            System.out.println("Successfully removed one record from table: '" + tableName + "' using the '<' operator");
                                            System.out.println("Writing query result to output file... ");

                                            homeScreen.fileOutput.write("'"+tableName + "' Table AFTER 'DELETE' query\n");
                                            writeTableData.writeTableToDb(sqlDatabase, databaseName, tableName, homeScreen);
                                        }
                                        break;

                                    case ">":
                                        if (sqlDatabase.sqlDatabase.get(entries.getKey()).get(tableIdx).table.get(i).size() == 0){
                                            continue;
                                        }else
                                        //if the value of the row at the leftOperand index of a specific row is greater than the right operand, then delete that row.
                                        if((Integer.parseInt(sqlDatabase.sqlDatabase.get(entries.getKey()).get(tableIdx).table.get(i).get(leftOperandIdx)) > Integer.parseInt(rightOperand))){
                                            DisplayTableData writeTableData = new DisplayTableData();

                                            homeScreen.fileOutput.write("-------------------------------------------------------------------------------------------------");
                                            homeScreen.fileOutput.write("\nQuery Author: " + homeScreen.username + "\nQuery Statement: " + userQuery + "\n\n\t\t\t\t\tQuery result \n");
                                            homeScreen.fileOutput.write("'"+tableName + "' Table BEFORE 'DELETE' query\n");
                                            writeTableData.writeTableToDb(sqlDatabase, databaseName, tableName, homeScreen);

                                            //gets specific table, gets specific row in that table and clears it.
                                            sqlDatabase.sqlDatabase.get(entries.getKey()).get(tableIdx).table.get(i).clear();

                                            System.out.println("Successfully removed one record from table: '" + tableName + "' using the '>' operator");
                                            System.out.println("Writing query result to output file... ");

                                            homeScreen.fileOutput.write("'"+tableName + "' Table AFTER 'DELETE' query\n");
                                            writeTableData.writeTableToDb(sqlDatabase, databaseName, tableName, homeScreen);
                                        }
                                        break;

                                    case "<=":
                                        if (sqlDatabase.sqlDatabase.get(entries.getKey()).get(tableIdx).table.get(i).size() == 0){
                                            continue;
                                        }else
                                        //if the value of the row at the leftOperand index of a specific row is less than or equal to the right operand, then delete that row.
                                        if((Integer.parseInt(sqlDatabase.sqlDatabase.get(entries.getKey()).get(tableIdx).table.get(i).get(leftOperandIdx)) <= Integer.parseInt(rightOperand))){
                                            DisplayTableData writeTableData = new DisplayTableData();

                                            homeScreen.fileOutput.write("-------------------------------------------------------------------------------------------------");
                                            homeScreen.fileOutput.write("\nQuery Author: " + homeScreen.username + "\nQuery Statement: " + userQuery + "\n\n\t\t\t\t\tQuery result \n");
                                            homeScreen.fileOutput.write("'"+tableName + "' Table BEFORE 'DELETE' query\n");
                                            writeTableData.writeTableToDb(sqlDatabase, databaseName, tableName, homeScreen);

                                            //gets specific table, gets specific row in that table and clears it.
                                            sqlDatabase.sqlDatabase.get(entries.getKey()).get(tableIdx).table.get(i).clear();

                                            System.out.println("Successfully removed one record from table: '" + tableName + "' using the '<=' operator");
                                            System.out.println("Writing query result to output file... ");

                                            homeScreen.fileOutput.write("'"+tableName + "' Table AFTER 'DELETE' query\n");
                                            writeTableData.writeTableToDb(sqlDatabase, databaseName, tableName, homeScreen);
                                        }
                                        break;

                                    case ">=":
                                        if (sqlDatabase.sqlDatabase.get(entries.getKey()).get(tableIdx).table.get(i).size() == 0){
                                            continue;
                                        }else
                                        //if the value of the row at the leftOperand index of a specific row is greater than or equal to the right operand, then delete that row.
                                        if((Integer.parseInt(sqlDatabase.sqlDatabase.get(entries.getKey()).get(tableIdx).table.get(i).get(leftOperandIdx)) >= Integer.parseInt(rightOperand))){
                                            DisplayTableData writeTableData = new DisplayTableData();

                                            homeScreen.fileOutput.write("-------------------------------------------------------------------------------------------------");
                                            homeScreen.fileOutput.write("\nQuery Author: " + homeScreen.username + "\nQuery Statement: " + userQuery + "\n\n\t\t\t\t\tQuery result \n");
                                            homeScreen.fileOutput.write("'"+tableName + "' Table BEFORE 'DELETE' query\n");
                                            writeTableData.writeTableToDb(sqlDatabase, databaseName, tableName, homeScreen);

                                            //gets specific table, gets specific row in that table and clears it.
                                            sqlDatabase.sqlDatabase.get(entries.getKey()).get(tableIdx).table.get(i).clear();

                                            System.out.println("Successfully removed one record from table: '" + tableName + "' using the '>=' operator");
                                            System.out.println("Writing query result to output file... ");

                                            homeScreen.fileOutput.write("'"+tableName + "' Table AFTER 'DELETE' query\n");
                                            writeTableData.writeTableToDb(sqlDatabase, databaseName, tableName, homeScreen);
                                        }
                                        break;
                                }
                            }
                            tableIdx++;
                        }
                    }
                }
            }

        }

    }

    @Override
    public void update(SqlDatabase sqlDatabase, String databaseName, HomeScreen homeScreen) throws IOException {
        /*Syntax:
        UPDATE table_name SET column1 = value1, column2 = value2 WHERE condition;*/
        //Get SQL Statement
        System.out.println("Enter your 'UPDATE' SQL statement: \n");
        Scanner input = new Scanner(System.in);
        String userQuery = input.nextLine();

        //remove all punctuation marks that include all brackets, braces and sq. brackets, commas, colon, semicolons from user input
        userQuery = userQuery.replaceAll("[\\p{Punct}&&[^><=_-]]+", "");


        //Split input by 'space' delimiter.
        String[] parsedUserInput = userQuery.split(" ");
        //Convert parsed data to arraylist
        ArrayList<String> parsedUserInputArrList = new ArrayList<String>(Arrays.asList(parsedUserInput));

        if(!parsedUserInputArrList.contains("WHERE")){
            System.out.println("Your 'UPDATE' query MUST NOT omit the 'WHERE' clause." +
                               "\nPlease follow the Standard Syntax: UPDATE table_name SET column1 = value1, column2 = value2 WHERE condition");
            return;
        }

        //Extract table name & "SET" idx from input
        String tableName = parsedUserInputArrList.get(1);

        int setIdx = 2;
        int whereIdx = 0;

        //Find "WHERE" index.
        for(int i = 0; i < parsedUserInputArrList.size(); i++){
            if(parsedUserInputArrList.get(i).equals("WHERE")){
                whereIdx = i;
            }
        }

        //Extract operands and operators
        String leftOperand = parsedUserInput[whereIdx + 1];
        String operator = parsedUserInput[whereIdx + 2];
        String rightOperand = parsedUserInput[whereIdx + 3];

        //System.out.println("Table name: " + tableName+ ", Where_Index: " + whereIdx + ", LeftOperand: " + leftOperand + ", Operator: " + operator + ", RightOperand: " + rightOperand);

        //This hash map represents a mapping for the columns to values of the UPDATE query;
        HashMap<String, String> columnToValue = new HashMap<>();

        //Iterate through parsed input slice and store corresponding data to hashmap
        String[] slice = Arrays.copyOfRange(parsedUserInput, setIdx+1, whereIdx);
        int ptr = 0;

        while(ptr < slice.length){
            if(!slice[ptr].equals("=") && ptr+2 < slice.length){
                String columnData = slice[ptr];
                String valueData = slice[ptr+2];
                columnToValue.put(columnData,valueData);
            }
            ptr+=3;
        }



        //This hash map represents a mapping for the first row col names to their respective indices. i.e (Key = Col1_name, Val = Col1_idx);
        HashMap<String, Integer> firstRowColumnToIdxMap = new HashMap<>();

        //Iterate through the first row of the database and perform mapping of column_name -> idx
        ArrayList<Table> table_data = sqlDatabase.sqlDatabase.get(databaseName);
        int first_row_size = 0;
        for(Table t: table_data){
            if(t.tableName.equals(tableName)){
                ArrayList<String> first_row = t.table.get(0);
                first_row_size = first_row.size();
                for(int i = 0; i < first_row.size(); i++){
                    firstRowColumnToIdxMap.put(first_row.get(i), i);
                }
            }
        }

        //This mapping is used to know what particular column to update within a table
        HashMap<String, Integer> columnToRowMapping = new HashMap<>();
        for(Map.Entry<String, Integer> entries : firstRowColumnToIdxMap.entrySet()){
            if(columnToValue.containsKey(entries.getKey())){
                columnToRowMapping.put(entries.getKey(), firstRowColumnToIdxMap.get(entries.getKey()));
            }
        }

        //this hash map represents the inverse of column to row indices
        Map<Integer, String> inverseColToRowMap = new HashMap<>();
        for (Map.Entry<String, Integer> entry : columnToRowMapping.entrySet()){
            inverseColToRowMap.put(Integer.valueOf(entry.getValue()), entry.getKey());
        }
        //System.out.println("inverse col to row : " + inverseColToRowMap.toString());

        //System.out.println("Col to row mapping: " + columnToRowMapping.toString());

        /*
        Algorithm:
        (1) - Find the table to be updated, (2) - Find the row(s) to be updated, (3) - Update correct columns within the corresponding row(s)
        */

        int colToParse = firstRowColumnToIdxMap.get(leftOperand);

        for(Map.Entry<String, ArrayList<Table>> entries : sqlDatabase.sqlDatabase.entrySet()) {

            //found correct db to delete from
            if (entries.getKey().equals(databaseName)) {

                //find correct table and update corresponding  records
                int tableIdx = 0;
                for (Table t : sqlDatabase.sqlDatabase.get(entries.getKey())) {
                    int current_table_size = sqlDatabase.sqlDatabase.get(entries.getKey()).get(tableIdx).table.size();

                    //go through every row and update correct columns within them
                    if(t.tableName.equals(tableName)) {
                        for (int i = 1; i < current_table_size; i++) {
                            //Go through every column within the row
                            //System.out.println("Row Data: " + sqlDatabase.sqlDatabase.get(entries.getKey()).get(0).table.get(i).toString());
                            ArrayList<String> row_data = sqlDatabase.sqlDatabase.get(entries.getKey()).get(0).table.get(i);
                            for(int k = 0; k < row_data.size(); k++){
                                if(k == colToParse){
                                    switch (operator){
                                        case "=":
                                            if (sqlDatabase.sqlDatabase.get(entries.getKey()).get(tableIdx).table.get(i).size() == 0){
                                                continue;
                                            }else if(row_data.get(colToParse).equals(rightOperand)){

                                                DisplayTableData writeTableData = new DisplayTableData();
                                                homeScreen.fileOutput.write("-------------------------------------------------------------------------------------------------");
                                                homeScreen.fileOutput.write("\nQuery Author: " + homeScreen.username + "\nQuery Statement: " + userQuery + "\n\n\t\t\t\t\tQuery result \n");
                                                homeScreen.fileOutput.write("'"+tableName + "' Table BEFORE 'UPDATE' query\n");
                                                writeTableData.writeTableToDb(sqlDatabase, databaseName, tableName, homeScreen);

                                                for(int x = 0; x < row_data.size(); x++){

                                                    if(columnToRowMapping.values().contains(x)){
                                                        //updates specific row(s)
                                                        row_data.set(x, columnToValue.get(inverseColToRowMap.get(x)));

                                                    }
                                                }

                                                System.out.println("Successfully updated row(s) in table: '" + tableName + "' using the '=' operator");
                                                System.out.println("Writing query result to output file...");

                                                homeScreen.fileOutput.write("'"+tableName + "' Table AFTER 'UPDATE' query\n");
                                                writeTableData.writeTableToDb(sqlDatabase, databaseName, tableName, homeScreen);

                                            }
                                            break;

                                        case "<":
                                            if (sqlDatabase.sqlDatabase.get(entries.getKey()).get(tableIdx).table.get(i).size() == 0){
                                                continue;
                                            }else if(Integer.valueOf(row_data.get(colToParse)) < Integer.valueOf(rightOperand)){
                                                DisplayTableData writeTableData = new DisplayTableData();
                                                homeScreen.fileOutput.write("-------------------------------------------------------------------------------------------------");
                                                homeScreen.fileOutput.write("\nQuery Author: " + homeScreen.username + "\nQuery Statement: " + userQuery + "\n\n\t\t\t\t\tQuery result \n");
                                                homeScreen.fileOutput.write("'"+tableName + "' Table BEFORE 'UPDATE' query\n");
                                                writeTableData.writeTableToDb(sqlDatabase, databaseName, tableName, homeScreen);

                                                for(int x = 0; x < row_data.size(); x++){
                                                    if(columnToRowMapping.values().contains(x)){
                                                        row_data.set(x, columnToValue.get(inverseColToRowMap.get(x)));
                                                    }
                                                }

                                                System.out.println("Successfully updated row(s) in table: '" + tableName + "' using the '<' operator");
                                                System.out.println("Writing query result to output file...");

                                                homeScreen.fileOutput.write("'"+tableName + "' Table AFTER 'UPDATE' query\n");
                                                writeTableData.writeTableToDb(sqlDatabase, databaseName, tableName, homeScreen);

                                            }

                                            break;

                                        case ">":
                                            if (sqlDatabase.sqlDatabase.get(entries.getKey()).get(tableIdx).table.get(i).size() == 0){
                                                continue;
                                            }else if(Integer.valueOf(row_data.get(colToParse)) > Integer.valueOf(rightOperand)){
                                                DisplayTableData writeTableData = new DisplayTableData();
                                                homeScreen.fileOutput.write("-------------------------------------------------------------------------------------------------");
                                                homeScreen.fileOutput.write("\nQuery Author: " + homeScreen.username + "\nQuery Statement: " + userQuery + "\n\n\t\t\t\t\tQuery result \n");
                                                homeScreen.fileOutput.write("'"+tableName + "' Table BEFORE 'UPDATE' query\n");
                                                writeTableData.writeTableToDb(sqlDatabase, databaseName, tableName, homeScreen);
                                                for(int x = 0; x < row_data.size(); x++){
                                                    if(columnToRowMapping.values().contains(x)){
                                                        row_data.set(x, columnToValue.get(inverseColToRowMap.get(x)));
                                                    }
                                                }

                                                System.out.println("Successfully updated row(s) in table: '" + tableName + "' using the '>' operator");
                                                System.out.println("Writing query result to output file...");

                                                homeScreen.fileOutput.write("'"+tableName + "' Table AFTER 'UPDATE' query\n");
                                                writeTableData.writeTableToDb(sqlDatabase, databaseName, tableName, homeScreen);

                                            }
                                            break;

                                        case "<=":
                                            if (sqlDatabase.sqlDatabase.get(entries.getKey()).get(tableIdx).table.get(i).size() == 0){
                                                continue;
                                            }else if(Integer.valueOf(row_data.get(colToParse)) <= Integer.valueOf(rightOperand)){
                                                DisplayTableData writeTableData = new DisplayTableData();
                                                homeScreen.fileOutput.write("-------------------------------------------------------------------------------------------------");
                                                homeScreen.fileOutput.write("\nQuery Author: " + homeScreen.username + "\nQuery Statement: " + userQuery + "\n\n\t\t\t\t\tQuery result \n");
                                                homeScreen.fileOutput.write("'"+tableName + "' Table BEFORE 'UPDATE' query\n");
                                                writeTableData.writeTableToDb(sqlDatabase, databaseName, tableName, homeScreen);

                                                for(int x = 0; x < row_data.size(); x++){
                                                    if(columnToRowMapping.values().contains(x)){
                                                        row_data.set(x, columnToValue.get(inverseColToRowMap.get(x)));
                                                    }
                                                }

                                                System.out.println("Successfully updated row(s) in table: '" + tableName + "' using the '<=' operator");
                                                System.out.println("Writing query result to output file...");

                                                homeScreen.fileOutput.write("'"+tableName + "' Table AFTER 'UPDATE' query\n");
                                                writeTableData.writeTableToDb(sqlDatabase, databaseName, tableName, homeScreen);

                                            }
                                            break;

                                        case ">=":
                                            if (sqlDatabase.sqlDatabase.get(entries.getKey()).get(tableIdx).table.get(i).size() == 0){
                                                continue;
                                            }else if(Integer.valueOf(row_data.get(colToParse)) >= Integer.valueOf(rightOperand)){
                                                DisplayTableData writeTableData = new DisplayTableData();
                                                homeScreen.fileOutput.write("-------------------------------------------------------------------------------------------------");
                                                homeScreen.fileOutput.write("\nQuery Author: " + homeScreen.username + "\nQuery Statement: " + userQuery + "\n\n\t\t\t\t\tQuery result \n");
                                                homeScreen.fileOutput.write("'"+tableName + "' Table BEFORE 'UPDATE' query\n");
                                                writeTableData.writeTableToDb(sqlDatabase, databaseName, tableName, homeScreen);

                                                for(int x = 0; x < row_data.size(); x++){
                                                    if(columnToRowMapping.values().contains(x)){
                                                        row_data.set(x, columnToValue.get(inverseColToRowMap.get(x)));
                                                    }
                                                }

                                                System.out.println("Successfully updated row(s) in table: '" + tableName + "' using the '>=' operator");
                                                System.out.println("Writing query result to output file...");

                                                homeScreen.fileOutput.write("'"+tableName + "' Table AFTER 'UPDATE' query\n");
                                                writeTableData.writeTableToDb(sqlDatabase, databaseName, tableName, homeScreen);

                                            }
                                            break;
                                    }

                                }
                            }

                        }
                    }
                    tableIdx++;
                }

            }
        }

    }
}
