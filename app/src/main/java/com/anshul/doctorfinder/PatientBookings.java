package com.anshul.doctorfinder;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class PatientBookings extends AppCompatActivity {
    MyListAdapterAllBookings adapter;
    ArrayList<DisplayListBookingDetails> displayList = new ArrayList<DisplayListBookingDetails>();
    ArrayList<String> bookingsList = new ArrayList<String>();
    ListView listview;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    String doctorname, doctorspecification, bookingdate, bookingtime, doctorid1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_bookings);
        setTitle("Patient Bookings");
        listview = (ListView) findViewById(R.id.list);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = getIntent();
        final String pid = intent.getStringExtra("pid");

        adapter = new MyListAdapterAllBookings(PatientBookings.this, displayList);
        listview.setAdapter(adapter);
        new AsyncLogin().execute(pid);
        displayList.add(new DisplayListBookingDetails(doctorname, doctorspecification, bookingdate, bookingtime));
        adapter = new MyListAdapterAllBookings(PatientBookings.this, displayList);
        listview.setAdapter(adapter);



    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.enter, R.anim.leave);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.enter, R.anim.leave);
    }

    private class AsyncLogin extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(PatientBookings.this);
        HttpURLConnection conn;
        ArrayList<DisplayListBookingDetails> displayList = new ArrayList<DisplayListBookingDetails>();
        URL url = null;
        ArrayList<String> bookinglist = new ArrayList<String>();


        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            pdLoading.setMessage("Loading Please Wait");
            pdLoading.setCancelable(false);
            pdLoading.show();
            pdLoading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.YELLOW));
            pdLoading.setIndeterminate(false);
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                //url = new URL("http://192.51.17.206/ds.accounts.mdi/api/loginphpfile.php?action=showAll");
                url = new URL("http://doc.gsinfotec.in/loginphpfile.php?action=fetchpatientbookingDetails");
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("pid", params[0]);
                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }


            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }


        }


        @Override
        protected void onPostExecute(String result) {

            pdLoading.dismiss();

//Toast.makeText()
            if (result != null) {

                JSONArray jsonArray;
                // JSONObject jobj;


//
//jobj=new JSONObject(result);


                try {
                    jsonArray = new JSONArray(result);


                    for (int i = 0; i < jsonArray.length(); i++) {


                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        Toast.makeText(getApplicationContext(),"hhhhhhhhhhhhhh"+doctorname,Toast.LENGTH_LONG).show();
                        doctorname = jsonObject.getString("docname");
                        doctorspecification = jsonObject.getString("specialization");
                        //  Toast.makeText(getApplicationContext(),"hhhhhhhhhhhhhh"+doctorname,Toast.LENGTH_LONG).show();
                        bookingdate = jsonObject.getString("booking_date");
                        bookingtime = jsonObject.getString("booking_time");
                        displayList.add(new DisplayListBookingDetails(doctorname, doctorspecification, bookingdate, bookingtime));
                        bookinglist.add(doctorid1);
                    }


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                adapter = new MyListAdapterAllBookings(PatientBookings.this, displayList);
                listview.setAdapter(adapter);


            }
        }
    }
}
