package org.example.QueryLogic.queryInterfaces;

import org.example.Database.databaseClasses.SqlDatabase;
import org.example.Database.databaseClasses.Table;
import org.example.Screens.screenClasses.HomeScreen;

public interface DDLQueryInterface {

    public Table createTable(String userQuery);

}
