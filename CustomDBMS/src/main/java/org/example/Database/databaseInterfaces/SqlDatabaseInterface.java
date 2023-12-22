package org.example.Database.databaseInterfaces;

import org.example.Database.databaseClasses.Table;

import java.util.ArrayList;
import java.util.HashMap;


public interface SqlDatabaseInterface {

    public void addTableToDb(String userId, Table newTable);

    public HashMap<String, ArrayList<Table>> getSqlDatabase();
}
