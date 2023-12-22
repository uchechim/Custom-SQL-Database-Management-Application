package org.example.Database.databaseClasses;

import org.example.Database.databaseInterfaces.TableInterface;

import java.util.ArrayList;
import java.util.HashMap;

public class Table implements TableInterface {

    //Hashmap -> Key = table name val = arrayList of [] since col is constont

    //public HashMap<String, ArrayList<String[]>> table = new HashMap<String, ArrayList<String[]>>();


    public ArrayList<ArrayList<String>> table = new ArrayList<>();
    public String tableName;


    public Table(String tableName){
        this.tableName = tableName;
    }

    /*
    public void getRowValues(int i){
        ArrayList<String[]> vals = table.get(i);

        System.out.println(vals);
    }
    */
}
