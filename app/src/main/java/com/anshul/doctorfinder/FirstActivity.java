package com.anshul.doctorfinder;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

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

public class FirstActivity extends AppCompatActivity {
LinearLayout doctorlogin,patientlogin;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    String ps1, ps2, ps3, ps4;
    Boolean resultSTR = false;
    String resultpid;
    Boolean resultSTR1 = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        ps1 = sharedpreferences.getString("LoginSession1", "");
        ps2 = sharedpreferences.getString("Username1", "");
        ps3 = sharedpreferences.getString("Password1", "");
        ps4 = sharedpreferences.getString("pid", "");
        doctorlogin=(LinearLayout) findViewById(R.id.doctorlogin);
        patientlogin=(LinearLayout) findViewById(R.id.patientLogin);
        new AsyncLogin1().execute(ps2, ps3);
         getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        doctorlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FirstActivity.this,DoctorLoginScreen.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in,R.anim.left_out);
            }
        });
        patientlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ps1.equals("LoggedInPatient") && resultSTR1) {
//                    Intent intent = new Intent(CompleteDoctorProfile.this, PatientMainMenu.class);
//                    intent.putExtra("pid", ps4);
//                    startActivity(intent);
//                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    Intent intent1 = new Intent(FirstActivity.this, PatientMainMenu.class);
                    intent1.putExtra("pid",resultpid);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }
                else {
                    Intent intent=new Intent(FirstActivity.this,MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in,R.anim.left_out);

                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
    public class AsyncLogin1 extends AsyncTask<String, String, String> {
        private ProgressDialog dialog = new ProgressDialog(FirstActivity.this);

        HttpURLConnection conn;
        URL url = null;

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                url = new URL("http://doc.gsinfotec.in/loginphpfile.php?action=loginPatient");

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
                        .appendQueryParameter("username", params[0])
                        .appendQueryParameter("password", params[1]);
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

            resultpid=result;

            if (!result.equals("")) {

                resultSTR1 = true;

            }
        }
    }
}