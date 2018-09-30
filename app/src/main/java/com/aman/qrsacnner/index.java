package com.aman.qrsacnner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.aman.qrsacnner.DbHandler.ConnectionClass;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class index extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,OnChartValueSelectedListener {
    String header_text;
    View mHeaderView;
    TextView txtView;
    SharedPreferences prf;
    Connection connect;
    PreparedStatement stmt;
    ResultSet rs;
    SharedPreferences sp;
    String user_name;
    String query;
    int user_count,audit_count,user_audit_count,asset_count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*Queries for pie chart*/
        sp = getSharedPreferences("user_details", MODE_PRIVATE);
        user_name = sp.getString("username", null);
        prf = getSharedPreferences("user_details", MODE_PRIVATE);
        try {
            ConnectionClass connectionClass = new ConnectionClass();
            connect = connectionClass.CONN();
            //Query for pie charts
            //Qry-1
            query = "select count(distinct column_name) from table_name where initiator not in ('-')";
            stmt = connect.prepareStatement(query);
            rs = stmt.executeQuery();
            if(rs.next()){
                asset_count=rs.getInt(1);
            }
            //Qry-2
            query = "select count(distinct column_name) from table_name where initiator not in ('-')";
            stmt = connect.prepareStatement(query);
            rs = stmt.executeQuery();
            if(rs.next()){
                user_count=rs.getInt(1);
            }
            //Qry-3
            query = "select count (distinct column_name) from table_name where initiator =?";
            stmt = connect.prepareStatement(query);
            stmt.setString(1,user_name.toString());
            rs = stmt.executeQuery();
            if(rs.next()){
                user_audit_count=rs.getInt(1);
            }
            //Qry-4
            query = "select count (distinct column_name) from table_name ";
            stmt = connect.prepareStatement(query);
            rs = stmt.executeQuery();
            if(rs.next()){
                audit_count=rs.getInt(1);
            }
        }catch (Exception ex){

        }
        /**/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /**/
        //Pie chart Code

        PieChart pieChart = (PieChart) findViewById(R.id.piechart);
        //pieChart.setUsePercentValues(true);
        ArrayList<Entry> yvalues = new ArrayList<Entry>();
        yvalues.add(new Entry(Math.round(asset_count), 0));
        yvalues.add(new Entry(Math.round(user_count), 1));
        yvalues.add(new Entry(Math.round(user_audit_count), 2));
        yvalues.add(new Entry(Math.round(audit_count), 3));


        PieDataSet dataSet = new PieDataSet(yvalues, "Audits");

        ArrayList<String> xVals = new ArrayList<String>();

        xVals.add("Total Assets Audited");
        xVals.add("Total Auditors");
        xVals.add("Asset Audited By:"+prf.getString("emp_name", null));
        xVals.add("Total Audits");

        PieData data = new PieData(xVals, dataSet);
        // In Percentage term
        //data.setValueFormatter(new PercentFormatter());
        // Default value
        //data.setValueFormatter(new DefaultValueFormatter(0));
        pieChart.setData(data);
        pieChart.setDescription("Mobile Audit Overview");

        pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleRadius(25f);
        pieChart.setHoleRadius(25f);

        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        data.setValueTextSize(13f);
        data.setValueTextColor(Color.DKGRAY);
        pieChart.setOnChartValueSelectedListener(this);

        pieChart.animateXY(1400, 1400);
        /**/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        mHeaderView = navigationView.getHeaderView(0);

        txtView = (TextView) mHeaderView.findViewById(R.id.textView);
        txtView.setText("Hello, " + prf.getString("emp_name", null));
        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.index, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        System.out.print("here");
        int id = item.getItemId();
        if (id == R.id.nav_camera) {
            Intent intent = new Intent(index.this, ForSpinner.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            Intent newInt = new Intent(index.this, scanned.class);
            startActivity(newInt);
        }
        else if (id == R.id.nav_report) {
            Intent newInt = new Intent(index.this, scannedRemarks.class);
            startActivity(newInt);
        }
        else if (id == R.id.nav_logout) {
            SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
            sp.edit().putBoolean("logged", false).apply();
            SharedPreferences.Editor editor = prf.edit();
            editor.clear();
            editor.commit();
            Intent newInt = new Intent(index.this, login.class);
            startActivity(newInt);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    /**/
    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
                        + ", DataSet index: " + dataSetIndex);
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }
    /**/


}
