package com.aman.qrsacnner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.aman.qrsacnner.DbHandler.ConnectionClass;
import com.aman.qrsacnner.handler.audit_handler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SpinnerRemarks extends AppCompatActivity {
    Spinner spinnercountry;
    Button btn;
    String name;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinner_remarks);
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
        spinnercountry = (Spinner) findViewById(R.id.spinnercountry);
        btn =  findViewById(R.id.button2);
        sp = getSharedPreferences("audit_name", MODE_PRIVATE);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SpinnerRemarks.this,scannedRemarks.class);
                startActivity(intent);
            }
        });
        try {
            audit_handler audit_handler=new audit_handler();
            String[] data = audit_handler.audit_handler().toArray(new String[0]);
            ArrayAdapter NoCoreAdapter = new ArrayAdapter(this,
                    android.R.layout.simple_list_item_1, data);
            spinnercountry.setAdapter(NoCoreAdapter);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        spinnercountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        Intent i = new Intent(SpinnerRemarks.this,index.class);
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
            Intent intent = new Intent(SpinnerRemarks.this, index.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}