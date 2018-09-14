package com.aman.qrsacnner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.aman.qrsacnner.DbHandler.ConnectionClass;

public class ForSpinner extends AppCompatActivity {
    Spinner spinnercountry;
    Connection connect;
    PreparedStatement stmt;
    ResultSet rs;
    Button btn;
    String name;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ConnectionClass connectionClass = new ConnectionClass();
        connect = connectionClass.CONN();
        String query = "select distinct audit_name from multiple_audit";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_for_spinner);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),index.class));
                finish();
            }
        });
        spinnercountry = (Spinner) findViewById(R.id.spinnercountry);
        btn =  findViewById(R.id.button2);
        sp = getSharedPreferences("audit_name", MODE_PRIVATE);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForSpinner.this,MainActivity.class);
                startActivity(intent);
            }
        });
        try {
            stmt = connect.prepareStatement(query);
            rs = stmt.executeQuery();
            ArrayList<String> data = new ArrayList<String>();
            while (rs.next()) {
                String id = rs.getString("audit_name");
                data.add(id);
            }
            String[] array = data.toArray(new String[0]);
            ArrayAdapter NoCoreAdapter = new ArrayAdapter(this,
                    android.R.layout.simple_list_item_1, data);
            spinnercountry.setAdapter(NoCoreAdapter);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        spinnercountry.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                name = spinnercountry.getSelectedItem().toString();
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("audit_name", name);
                editor.commit();
            }
              @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    @Override
    public void onBackPressed() {
    }
}
