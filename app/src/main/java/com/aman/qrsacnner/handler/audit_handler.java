package com.aman.qrsacnner.handler;

import android.annotation.SuppressLint;

import com.aman.qrsacnner.DbHandler.ConnectionClass;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class audit_handler{
    Connection connect;
    PreparedStatement statement;
    ResultSet resultSet;
    @SuppressLint("NewApi")
    public List<String> audit_handler() throws SQLException {
        ConnectionClass connectionClass = new ConnectionClass();
        connect = connectionClass.CONN();
        List<String> audit = new ArrayList<>();
        audit.add("Select Audit");
        Gson gson = new Gson();
        statement = connect.prepareStatement("select distinct column_name from table_name");
        resultSet = statement.executeQuery();
        while (resultSet.next()) {
            String audit_array = new String(resultSet.getString(1));
            audit.add(audit_array);
            //System.out.println(audit);
        }
        return audit;

    }

}
