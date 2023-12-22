package org.example.Database.databaseInterfaces;

import org.example.Database.databaseClasses.SqlDatabase;
import org.example.Screens.screenClasses.HomeScreen;

import java.io.IOException;

public interface DisplayTableDataInterface {
    public void writeTableToDb(SqlDatabase sqlDatabase, String databaseName, String tableName, HomeScreen homescreen) throws IOException;
}
