package com.aman.qrsacnner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.aman.qrsacnner.DbHandler.ConnectionClass;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class scanned extends AppCompatActivity {
    Connection connect;
    PreparedStatement stmt;
    ResultSet rs;
    String user_name, emp_name;
    SharedPreferences sp;
    boolean flag;
    TableLayout tableLayout;
    TextView txtView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sp = getSharedPreferences("user_details", MODE_PRIVATE);
        user_name = sp.getString("username", null);
        emp_name = sp.getString("emp_name", null);
        ConnectionClass connectionClass = new ConnectionClass();
        connect = connectionClass.CONN();

        String query = "select distinct asset_id,date from multiple_audit where initiator=?";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned);
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
        txtView=(TextView) findViewById(R.id.scannedView);
        txtView.setText("Asset Id's Scanned By:"+emp_name.toString());
        tableLayout=(TableLayout) findViewById(R.id.table);

        try {
            stmt = connect.prepareStatement(query);
            stmt.setString(1,user_name.toString());
            rs = stmt.executeQuery();
            ArrayList<String> data = new ArrayList<String>();
            while (rs.next()) {
                tableLayout.removeAllViewsInLayout();
                String id = rs.getString("asset_id");
                data.add(id);
            }
            String[] array = data.toArray(new String[0]);
            for (int i=0;i<array.length;i++){
                TableRow t=new TableRow(scanned.this);
                t.setLayoutParams(new TableLayout.LayoutParams(
                        TableRow.LayoutParams.FILL_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                t.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                TextView b6=new TextView(scanned.this);
                b6.setText("Scanned Id: "+data.get(i));
                b6.setTextColor(Color.BLACK);
                b6.setGravity(Gravity.CENTER);
                b6.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                b6.setPadding(120,20,0,20);
                b6.setTextSize(15);
                t.addView(b6);
                tableLayout.addView(t);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onBackPressed() {
    }
}
