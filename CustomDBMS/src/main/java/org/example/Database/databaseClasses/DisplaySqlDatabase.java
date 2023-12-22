package org.example.Database.databaseClasses;

import org.example.Database.databaseInterfaces.DisplaySqlDatabaseInterface;
import org.example.Screens.screenClasses.HomeScreen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DisplaySqlDatabase implements DisplaySqlDatabaseInterface {

    @Override
    public void displaySqlDatabaseTables(SqlDatabase sqlDatabase, HomeScreen homeScreen) throws IOException {

        System.out.println("\nWriting complete database information to file...");
        homeScreen.fileOutput.write("\n\t\t\t\t\tSQL Database Information");
        //System.out.println("\n\n\t\t\t\t\tComplete Database Information");

        HashMap<String, ArrayList<Table>> databaseCurrentState = sqlDatabase.getSqlDatabase();

        for(Map.Entry<String, ArrayList<Table>> dbEntry: databaseCurrentState.entrySet()){

            String db_name = dbEntry.getKey();
            homeScreen.fileOutput.write("\nDatabase Name: " + db_name);
            //System.out.println("\nDatabase Name: " + db_name);

            ArrayList<Table> tables = dbEntry.getValue();

            for(Table table : tables){
                String tabName = table.tableName;
                ArrayList<ArrayList<String>> tableData = table.table;

                homeScreen.fileOutput.write("\n\t\t\t\t\t'" + tabName+ "' Table Data\n");
                //System.out.println("\n\t\t\t\t\t'" + tabName+ "' Table Data\n");

                for(ArrayList row: tableData){

                    for(int i = 0; i < row.size(); i++){
                        homeScreen.fileOutput.write(row.get(i) + " ");
                        //System.out.print(row.get(i) + " ");
                    }
                    homeScreen.fileOutput.write("\n\n");
                    //System.out.println("\n");
                }
            }
        }
    }
}
