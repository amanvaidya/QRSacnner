package com.aman.qrsacnner;
//To display things in spinner(dropdown)

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aman.qrsacnner.DbHandler.ConnectionClass;
import com.aman.qrsacnner.handler.audit_handler;
import com.aman.qrsacnner.handler.subloc_handler;

public class ForSpinner extends AppCompatActivity {
    Spinner spinnercountry;
    Connection connect;
    PreparedStatement stmt;
    ResultSet rs;
    Button btn;
    String name, selectedFromList, subloc;
    SharedPreferences sp, sharedPreferences;
    private ListView lv;
    ArrayAdapter<String> adapter;
    EditText inputSearch;
    ArrayList<HashMap<String, String>> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ConnectionClass connectionClass = new ConnectionClass();
        connect = connectionClass.CONN();
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


        sp = getSharedPreferences("audit_name", MODE_PRIVATE);
        sharedPreferences = getSharedPreferences("selectedFromList", MODE_PRIVATE);


        /*Audit Name Selection*/

        try {

            audit_handler audit_handler=new audit_handler();
            String[] data = audit_handler.audit_handler().toArray(new String[0]);
            ArrayAdapter NoCoreAdapter = new ArrayAdapter(this,
                    android.R.layout.simple_list_item_1, data);
            spinnercountry.setAdapter(NoCoreAdapter);


        } catch (SQLException e) {
            e.printStackTrace();
        }

        /*Branch Name Selection*/
        try {
            subloc_handler subloc_handler = new subloc_handler();
            String [] array = subloc_handler.handler().toArray(new String[0]);
            lv=(ListView) findViewById(R.id.list_view);
            lv.setVisibility(View.INVISIBLE);
            inputSearch=(EditText) findViewById(R.id.inputSearch);
            adapter=new ArrayAdapter<String>(this,R.layout.list_item,R.id.product_name,array);
            lv.setAdapter(adapter);

            /**
             * Enabling Search Filter
             * */

            inputSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                    // When user changed the Text
                    lv.setVisibility(View.VISIBLE);

                    ForSpinner.this.adapter.getFilter().filter(cs);
                    lv.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                            selectedFromList = (String) lv.getItemAtPosition(position);

                            if(!name.toString().contains("Select Audit")){
                                System.out.println(selectedFromList);
                                //System.out.println(selectedFromList.toString());
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("audit_name", name);
                                System.out.println(selectedFromList);
                                editor.putString("selectedFromList", selectedFromList);
                                editor.commit();

                             Intent intent = new Intent(ForSpinner.this,MainActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(ForSpinner.this,"Please Select Audit",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                              int arg3) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void afterTextChanged(Editable arg0) {
                    // TODO Auto-generated method stub

                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }


        spinnercountry.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                name = spinnercountry.getSelectedItem().toString();

            }
              @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(ForSpinner.this,index.class);
        startActivity(i);
    }
}
