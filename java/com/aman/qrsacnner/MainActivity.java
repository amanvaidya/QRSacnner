package com.aman.qrsacnner;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.aman.qrsacnner.DbHandler.ConnectionClass;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {
    Connection connect;
    PreparedStatement stmt;
    ResultSet rs;
    String ast_id, audit_name, user_name, subloc_name;
    SharedPreferences prf, sp, sharedPreferences;
    TextView txtView;
    EditText remarks;
    EditText remarks1;
    Button btn;
    int subloc_id=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ConnectionClass connectionClass = new ConnectionClass();
        connect = connectionClass.CONN();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setLogo(R.drawable.logo_toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ForSpinner.class));
                finish();
            }
        });
        Button buttonBarCodeScan = findViewById(R.id.buttonScan);
        prf = getSharedPreferences("audit_name", MODE_PRIVATE);
        sp = getSharedPreferences("user_details", MODE_PRIVATE);
        audit_name = prf.getString("audit_name", null);
        subloc_name = prf.getString("selectedFromList",null);

        try {
            stmt=connect.prepareStatement("select subloc_id from sublocation_master where subloc_name=?");
            stmt.setString(1,subloc_name);
            rs=stmt.executeQuery();
            if(rs.next()){
                subloc_id=rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println(audit_name.toString());
        System.out.println(subloc_name);
        user_name = sp.getString("username", null);
        buttonBarCodeScan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                new IntentIntegrator(MainActivity.this).setCaptureActivity(ScannerActivity.class).initiateScan();
            }
        });
        remarks = (EditText)findViewById(R.id.remarks);
        remarks1 = (EditText) findViewById(R.id.remarks1);
        //remarks=rem.getText().toString();
        btn = findViewById(R.id.button3);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubmitAsset subass = new SubmitAsset();
                subass.execute();
                remarks.setText("");
                remarks1.setText("");

            }
        });
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
            Intent intent = new Intent(MainActivity.this, index.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Scan Cancelled", Toast.LENGTH_LONG).show();
            } else {
                showResultDialogue(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
   public void showResultDialogue(final String result) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Scan Result")
                .setMessage("Scanned result is " + result)
                .setPositiveButton("Scan Next", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ast_id = result;
                        SubmitAsset subass = new SubmitAsset();
                        subass.execute();
                        new IntentIntegrator(MainActivity.this).setCaptureActivity(ScannerActivity.class).initiateScan();

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setNeutralButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ast_id = result;
                SubmitAsset subass = new SubmitAsset();
                subass.execute();
                    }
        })
                .show();
    }

    public class SubmitAsset extends AsyncTask<String, String, String> {
        String z = "";
        Boolean isSuccess = false;

        @Override
        protected String doInBackground(String... params) {
            String assetid = ast_id==null?"":ast_id;
            if(assetid.equals("")){
                assetid=remarks1.getText().toString();
            }
            System.out.println("assetid"+assetid);
            String rem = remarks.getText().toString()==null?"-":remarks.getText().toString();
            System.out.println("Remarks"+rem);
            boolean flag = false;
            if(assetid.contains("-")){
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Submitted",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                try {
                    ConnectionClass connectionClass = new ConnectionClass();
                    connect = connectionClass.CONN();
                    int loc_id=0;
                    int sub_id=0;
                    int fac_id=0;
                    int cub_id=0;
                    String qry="select loc_id,subloc_id,facility_id,cubicle_id from hardware_register where assetid=?";
                    stmt = connect.prepareStatement(qry);
                    stmt.setString(1,assetid);
                    rs=stmt.executeQuery();
                    if(rs.next()) {
                        loc_id = rs.getInt(1);
                        sub_id = rs.getInt(2);
                        fac_id = rs.getInt(3);
                        cub_id = rs.getInt(4);
                    }
                        String query = "insert into multiple_audit (asset_id,date,location,sublocation,facility,cubicle,audit_name,initiator,remarks) values (?,getDate(),?,?,?,?,?,?,?)";
                        stmt = connect.prepareStatement(query);
                        stmt.setString(1, assetid);
                        stmt.setInt(2, loc_id);
                        stmt.setInt(3, sub_id);
                        stmt.setInt(4, fac_id);
                        stmt.setInt(5, cub_id);
                        stmt.setString(6, audit_name.toString());
                        stmt.setString(7, user_name.toString());
                        stmt.setString(8, rem);
                        stmt.executeUpdate();
                        ast_id = "";


                } catch (Exception ex) {
                    isSuccess = false;
                    z = ex.getMessage();
                    System.out.println("Aman Error Here is::"+ex);
                }
            }



            return z;
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(MainActivity.this,ForSpinner.class);
        startActivity(i);
    }

}
