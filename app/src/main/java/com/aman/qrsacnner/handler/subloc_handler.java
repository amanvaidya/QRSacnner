package com.aman.qrsacnner.handler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.aman.qrsacnner.DbHandler.ConnectionClass;
import com.google.gson.Gson;

import java.net.HttpCookie;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class subloc_handler {
    Connection connect;
    PreparedStatement statement;
    ResultSet resultSet;
    SharedPreferences pref;
    private Context mContext;

    @SuppressLint("NewApi")
            public List<String> handler() throws SQLException {
                ConnectionClass connectionClass = new ConnectionClass();
                connect = connectionClass.CONN();


                final List<String> subloc = new ArrayList<>();
                Gson gson = new Gson();
                System.out.println("Aman Subloc Size::"+subloc.size());
                    statement = connect.prepareStatement("select distinct subloc_name from sublocation_master");
                    resultSet = statement.executeQuery();
                    while (resultSet.next()) {
                        final String sublocArray = new String(resultSet.getString(1));
                        subloc.add(sublocArray);
                        //System.out.println(subloc);
                    }
                return subloc;

            }
    }
