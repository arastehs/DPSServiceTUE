package com.dev.model.dao;

import java.sql.SQLException;
import java.util.Vector;

/*
An interface class that has 3 methods:
 VehicleProvider() is the main method that calls the bellow 2 methods and returns a list of volunteer carIds that s
 should be processed in threads
getVehListCount() is a method used for getting number of all vehicles that are volunteer for the desired job
 fillVectorList() fetches the list from db

 */
public interface VehicleListProvider {

    public Vector<String> VehicleProvider() throws SQLException;
    public  int getVehListCount() throws SQLException;
    public Vector<String> fillVectorList() throws SQLException ;
}
