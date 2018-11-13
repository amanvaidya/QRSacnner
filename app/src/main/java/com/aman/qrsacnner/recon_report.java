package com.aman.qrsacnner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
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

public class recon_report extends AppCompatActivity {
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
        String query = "select m.asset_id, s.subloc_name,sb.subloc_name from multiple_audit m left outer join hardware_register hr on m.asset_id=hr.assetid left outer join sublocation_master s on m.sublocation=s.subloc_id left outer join sublocation_master sb on hr.subloc_id=sb.subloc_id where m.asset_id not in ('-') and m.asset_id in (select assetid from hardware_register)";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recon_report);
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
        txtView.setText("Recon Report For Found Asset Id's Scanned By:"+emp_name.toString());
        TextView reportText = (TextView) findViewById(R.id.reportText);
        reportText.setText("Asset Id"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"Tagged Branch"+"\t"+"\t"+"\t"+"Actual Branch");
        tableLayout=(TableLayout) findViewById(R.id.table);
        try {
            stmt = connect.prepareStatement(query);
            rs = stmt.executeQuery();
            ArrayList<String> data = new ArrayList<String>();
            //data.add("Asset Id"+"\t"+"\t"+"Tagged Branch"+"\t"+"\t"+"Actual Branch");
            while (rs.next()) {
                tableLayout.removeAllViewsInLayout();
                //Here call the column by column name from db
                String id = rs.getString(1);
                String tag = rs.getString(2)==null?"-":rs.getString(2);
                String actual = rs.getString(3);
                data.add(id+"\t"+"\t"+"\t"+"\t"+tag+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+actual);
            }
            String[] array = data.toArray(new String[0]);
            for (int i=0;i<array.length;i++){
                System.out.print(array[i]);
                TableRow t=new TableRow(recon_report.this);

                t.setLayoutParams(new TableLayout.LayoutParams(
                        TableRow.LayoutParams.FILL_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                t.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
                TextView b6=new TextView(recon_report.this);
                b6.setText(data.get(i));
                b6.setTextColor(Color.BLACK);
                b6.setGravity(Gravity.LEFT);
                b6.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
                b6.setPadding(170,20,40,20);
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
        Intent i = new Intent(recon_report.this,index.class);
        startActivity(i);
    }
}
