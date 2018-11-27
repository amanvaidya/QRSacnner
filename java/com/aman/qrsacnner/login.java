package com.aman.qrsacnner;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.aman.qrsacnner.DbHandler.ConnectionClass;

public class login extends AppCompatActivity {
    Button login;
    EditText username, password;
    ProgressBar progressBar;
    SharedPreferences sp, pref;

    Connection connect;
    PreparedStatement stmt,stmt1;
    ResultSet rs,rs1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = (Button) findViewById(R.id.button);
        username = (EditText) findViewById(R.id.editText);
        sp = getSharedPreferences("login", MODE_PRIVATE);
        pref = getSharedPreferences("user_details", MODE_PRIVATE);
        if (sp.getBoolean("logged", false)) {
            Intent intent = new Intent(login.this, index.class);

            startActivity(intent);
        }
        password = (EditText) findViewById(R.id.editText2);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckLogin checkLogin = new CheckLogin();
                checkLogin.execute("");
            }
        });
    }

    public class CheckLogin extends AsyncTask<String, String, String> {
        String z = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(login.this, r, Toast.LENGTH_SHORT).show();
            if (isSuccess) {
                Toast.makeText(login.this, "Login Successfull", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String usernam = username.getText().toString();
            String passwordd = password.getText().toString();
            String emp_name="";
            if (usernam.trim().equals("") || passwordd.trim().equals(""))
                z = "Please enter Username and Password";
            else try {
                ConnectionClass connectionClass = new ConnectionClass();

                connect = connectionClass.CONN();
                //Currently only using 1 column.. Edit as required
                String query = "select emp_name from user_manager where login_name= ? and password =?";
                //System.out.println(query);
                stmt = connect.prepareStatement(query);
                stmt.setString(1, usernam.toString());
                stmt.setString(2, passwordd.toString());
                rs = stmt.executeQuery();
                if (rs.next()) {
                    emp_name = rs.getString(1);


                    sp.edit().putBoolean("logged", true).apply();
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("username", usernam);
                    editor.putString("emp_name", emp_name);
                    editor.commit();
                    Intent intent = new Intent(login.this, index.class);
                    intent.putExtra("username", usernam);
                    startActivity(intent);

                } else {
                    z = "Invalid Credentials!";
                    isSuccess = false;
                }

            } catch (Exception ex) {
                System.out.print("Error While Connecting" + ex);
                isSuccess = false;
                z = ex.getMessage();
            }
            return z;
        }
    }


    @Override
    public void onBackPressed() {
    }
}
