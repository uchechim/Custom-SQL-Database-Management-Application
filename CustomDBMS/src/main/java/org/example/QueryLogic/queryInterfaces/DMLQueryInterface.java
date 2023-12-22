package org.example.QueryLogic.queryInterfaces;

import org.example.Database.databaseClasses.SqlDatabase;
import org.example.Screens.screenClasses.HomeScreen;

import java.io.IOException;

public interface DMLQueryInterface {

    public void select(SqlDatabase sqlDatabase, String databaseName, HomeScreen homeScreen) throws IOException;
    public void insert(SqlDatabase sqlDatabase, String databaseName, HomeScreen homeScreen) throws IOException;

    public void delete(SqlDatabase sqlDatabase, String databaseName, HomeScreen homeScreen) throws IOException;

    public void update(SqlDatabase sqlDatabase, String databaseName, HomeScreen homeScreen) throws IOException;


}

