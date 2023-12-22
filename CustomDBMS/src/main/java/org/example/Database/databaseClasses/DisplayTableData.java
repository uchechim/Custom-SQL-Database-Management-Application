package org.example.Database.databaseClasses;

import org.example.Database.databaseInterfaces.DisplayTableDataInterface;
import org.example.Screens.screenClasses.HomeScreen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class DisplayTableData implements DisplayTableDataInterface {
    @Override
    public void writeTableToDb(SqlDatabase sqlDatabase, String databaseName, String tableName, HomeScreen homeScreen) throws IOException {

        homeScreen.fileOutput.write("\n\t\t\t\t\t'" + tableName+ "' Table Data\n");
        for(Map.Entry<String, ArrayList<Table>> entry: sqlDatabase.sqlDatabase.entrySet()){

            if(entry.getKey().equals(databaseName)){
                //int i = 1;

                for(Table t : sqlDatabase.sqlDatabase.get(databaseName)){
                    //ArrayList<String> row_data = t.table.get(i);
                    //System.out.println("Row Data: " + row_data);
                    if(t.tableName.equals(tableName)){

                        for(int x = 0; x < t.table.size(); x++){
                            //System.out.println("Row Data of table: " + t.table.get(x));
                            ArrayList<String> curr_row = t.table.get(x);

                            for(int k = 0; k< curr_row.size(); k++){
                                //System.out.println("Row Data of column: " + curr_row.get(k) + " ");
                                homeScreen.fileOutput.write(curr_row.get(k) + " ");
                            }
                            homeScreen.fileOutput.write("\n");
                        }


                    }
                    //i++;
                }
            }
        }
        homeScreen.fileOutput.write("\n\n");

    }
}
