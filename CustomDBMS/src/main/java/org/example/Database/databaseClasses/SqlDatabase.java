package org.example.Database.databaseClasses;




import org.example.Database.databaseInterfaces.SqlDatabaseInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SqlDatabase implements SqlDatabaseInterface {

    //Structure = Hashmap of key = userID & val = array list of table objects since 1 user can have N tables
    public HashMap<String, ArrayList<Table>> sqlDatabase = new HashMap<String, ArrayList<Table>>();

    public SqlDatabase(){

    }

    public void addTableToDb(String databaseName, Table newTable){
        Set databases = sqlDatabase.keySet();
        if(sqlDatabase.size() == 0 || !databases.contains(databaseName)){
            ArrayList<Table> firstDbTable = new ArrayList<>();
            firstDbTable.add(newTable);
            sqlDatabase.put(databaseName, firstDbTable);

            System.out.println("Successfuly added table: '" + newTable.tableName +"' to database: '" + databaseName + "'");

        }else{
            for(Map.Entry<String, ArrayList<Table>> entry: sqlDatabase.entrySet()){

                String db_name = entry.getKey();
                System.out.println("Database name: '" + db_name +"'");
                if(db_name.equals(databaseName)){
                    //get the corresponding table list and add the new table.
                    sqlDatabase.get(db_name).add(newTable);

                    System.out.println("Successfuly added table: '" + newTable.tableName +"' to database: '" + databaseName + "'");
                }
            }
        }

    }


    public HashMap<String, ArrayList<Table>> getSqlDatabase(){
        return this.sqlDatabase;
    }
}
