package com.aman.qrsacnner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.aman.qrsacnner.DbHandler.ConnectionClass;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class scannedRemarks extends AppCompatActivity {
    Connection connect;
    PreparedStatement stmt;
    ResultSet rs;
    String user_name, emp_name, audit_name;
    SharedPreferences sp,prf;
    boolean flag;
    TableLayout tableLayout;
    TextView txtView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sp = getSharedPreferences("user_details", MODE_PRIVATE);
        prf = getSharedPreferences("audit_name", MODE_PRIVATE);
        audit_name = prf.getString("audit_name", null);
        user_name = sp.getString("username", null);
        emp_name = sp.getString("emp_name", null);
        ConnectionClass connectionClass = new ConnectionClass();
        connect = connectionClass.CONN();
        String query = "select distinct remarks,date from multiple_audit where initiator=?  and audit_name=?";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned_remarks);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setLogo(R.drawable.logo_toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),index.class));
                finish();
            }
        });
        txtView=(TextView) findViewById(R.id.scannedView);
        txtView.setText("Remarks Provided By:"+emp_name.toString());

        tableLayout=(TableLayout) findViewById(R.id.table);
        try {
            stmt = connect.prepareStatement(query);
            stmt.setString(1,user_name.toString());
            stmt.setString(2,audit_name.toString());
            rs = stmt.executeQuery();
            ArrayList<String> data = new ArrayList<String>();
            while (rs.next()) {
                tableLayout.removeAllViewsInLayout();
                //Here call the column by column name from db
                String id = rs.getString("remarks");
                data.add(id);
            }
            String[] array = data.toArray(new String[0]);
            for (int i=0;i<array.length;i++){
                TableRow t=new TableRow(scannedRemarks.this);
                t.setLayoutParams(new TableLayout.LayoutParams(
                        TableRow.LayoutParams.FILL_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                t.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                TextView b6=new TextView(scannedRemarks.this);
                TextView b7=new TextView(scannedRemarks.this);
                b6.setText("Remarks:"+"\t"+"\t");
                b6.setTextColor(Color.BLACK);
                b6.setGravity(Gravity.CENTER);
                b6.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                b6.setPadding(170,20,0,20);
                b6.setTextSize(15);
                b7.setText(data.get(i));
                b7.setTextColor(Color.BLACK);
                b7.setGravity(Gravity.CENTER);
                b7.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                b7.setPadding(30,20,0,20);
                b7.setTextSize(15);
                t.addView(b6);
                t.addView(b7);
                tableLayout.addView(t);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(scannedRemarks.this,index.class);
        startActivity(i);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_item_one) {
            Intent intent = new Intent(scannedRemarks.this, index.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
