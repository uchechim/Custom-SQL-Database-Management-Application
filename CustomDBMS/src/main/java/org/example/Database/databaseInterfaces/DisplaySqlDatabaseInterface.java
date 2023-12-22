package org.example.Database.databaseInterfaces;

import org.example.Database.databaseClasses.SqlDatabase;
import org.example.Screens.screenClasses.HomeScreen;

import java.io.IOException;

public interface DisplaySqlDatabaseInterface {

    public void displaySqlDatabaseTables(SqlDatabase database, HomeScreen homeScreen) throws IOException;
}
