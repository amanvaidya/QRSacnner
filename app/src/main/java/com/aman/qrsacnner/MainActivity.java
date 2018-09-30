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
    String ast_id, audit_name, user_name;
    SharedPreferences prf, sp;
    TextView txtView;
    EditText remarks;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        user_name = sp.getString("username", null);
        buttonBarCodeScan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                new IntentIntegrator(MainActivity.this).setCaptureActivity(ScannerActivity.class).initiateScan();
            }
        });
        remarks = (EditText)findViewById(R.id.remarks);
        //remarks=rem.getText().toString();
        btn = findViewById(R.id.button3);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Here1");
                SubmitAsset subass = new SubmitAsset();
                System.out.println("here2");
                subass.execute();

            }
        });
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
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("Scan Result", result);
                        clipboard.setPrimaryClip(clip);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public class SubmitAsset extends AsyncTask<String, String, String> {
        String z = "";
        Boolean isSuccess = false;

        @Override
        protected String doInBackground(String... params) {
            String assetid = ast_id==null?"-":ast_id;
            System.out.println("assetid"+assetid);
            String rem = remarks.getText().toString()==null?"-":remarks.getText().toString();
            boolean flag = false;
            if(!assetid.equals("-")||!rem.equals("-")){
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Submitted",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
            System.out.println("rem"+rem);
            try {
                ConnectionClass connectionClass = new ConnectionClass();
                connect = connectionClass.CONN();
                String query = "insert into table_name (column_name,column_name,column_name,column_name,column_name,column_name,column_name,column_name,column_name) values (?,getDate(),'0','0','0','0',?,?,?)";
                stmt = connect.prepareStatement(query);
                stmt.setString(1, assetid);
                stmt.setString(2, audit_name.toString());
                stmt.setString(3, user_name.toString());
                stmt.setString(4, rem);
                stmt.executeUpdate();


            } catch (Exception ex) {
                isSuccess = false;
                z = ex.getMessage();
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
