package com.anshul.doctorfinder;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

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
import java.util.Calendar;

public class PermanentLeavesDoctor extends AppCompatActivity{
    String doctorid;
    public static final int DATE_PICKER_ID = 1;
    DatePickerDialog picker;
    String dobSTR;


    int day, month, year;
    LinearLayout rowView;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    LinearLayout parentLinearLayout;
    EditText leavedate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permanent_leaves_doctor);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        parentLinearLayout = (LinearLayout) findViewById(R.id.permanemtlayout);


        Intent intent = getIntent();
        doctorid = intent.getStringExtra("doctorid");
        setTitle("Leaves");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        new AsyncLogin1().execute(doctorid);

    }

    public void addRow(View view) {
        addDynamicMOC("",0);
    }

    public void addDynamicMOC(String leavedate,int check) {

        final LinearLayout linearLayoutForm = (LinearLayout) findViewById(R.id.calibration);
        EditText edit;

        for (int i = 0; i < linearLayoutForm.getChildCount(); i++) {
            LinearLayout innerLayout = (LinearLayout) linearLayoutForm.getChildAt(i);
            edit = (EditText) innerLayout.findViewById(R.id.appointmentTime);
            edit.requestFocus();





        }
        final LinearLayout newView = (LinearLayout) PermanentLeavesDoctor.this
                .getLayoutInflater().inflate(R.layout.add_new_permanent_leaves, null);
        final EditText digital = (EditText) newView.findViewById(R.id.appointmentTime);
        digital.setText(leavedate);
        digital.requestFocus();
        if(check==0) {
            digital.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Calendar cldr = Calendar.getInstance();
                    int day = cldr.get(Calendar.DAY_OF_MONTH);
                    int month = cldr.get(Calendar.MONTH);
                    int year = cldr.get(Calendar.YEAR);
                    // date picker dialog
                    picker = new DatePickerDialog(PermanentLeavesDoctor.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    digital.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                }
                            }, year, month, day);
                    picker.show();
                }
            });
        }
        ImageView btnRemove = (ImageView) newView.findViewById(R.id.remove);
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linearLayoutForm.getChildCount() > 1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PermanentLeavesDoctor.this, R.style.PositiveButtonStyle11);
                    builder.setMessage("Do You Want To Delete??")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {

                                    linearLayoutForm.removeView(newView);
                                }
                            })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();


                } else {
                    Toast.makeText(getApplicationContext(), "Not Allowed",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        linearLayoutForm.addView(newView);

    }

    public void onDelete(View v) {
        if (parentLinearLayout.getChildCount() > 1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(PermanentLeavesDoctor.this);
            builder.setMessage("Do You Want To Delete")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                            parentLinearLayout.removeView(rowView);
                        }
                    })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();


        } else {
            Toast.makeText(getApplicationContext(), "Not Allowed",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.permanentleavesmenu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.enter, R.anim.leave);
                return true;
            case R.id.personalleavesmenu:
                new AsyncLoginDelete().execute("", doctorid);
                final LinearLayout linearLayoutForm = (LinearLayout) findViewById(R.id.calibration);
                EditText edit;
                for (int i = 0; i < linearLayoutForm.getChildCount(); i++) {
                    LinearLayout innerLayout = (LinearLayout) linearLayoutForm.getChildAt(i);
                    edit = (EditText) innerLayout.findViewById(R.id.appointmentTime);
                    if (!edit.getText().toString().equals("")) {
                        new AsyncLogin().execute(edit.getText().toString(), doctorid);
                    }

                }


                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.enter, R.anim.leave);
    }




    private class AsyncLogin extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(PermanentLeavesDoctor.this);
        HttpURLConnection conn;
        URL url = null;


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

//                url = new URL("http://rotaryapp.mdimembrane.com/HMS_API/hospital_activity_status_api.php?action=showAll");
                url = new URL("http://doc.gsinfotec.in/loginphpfile.php?action=addpermanentleaves");
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
                        .appendQueryParameter("leaves", params[0])
                        .appendQueryParameter("doctorid", params[1]);

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
                Log.d("dfcds", "Response Code:-" + response_code);
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
            Log.d("ssdgggfdddsd", result);

            pdLoading.dismiss();
            finish();
            overridePendingTransition(R.anim.enter,R.anim.leave);

        }
    }

    private class AsyncLoginDelete extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(PermanentLeavesDoctor.this);
        HttpURLConnection conn;
        URL url = null;


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

//                url = new URL("http://rotaryapp.mdimembrane.com/HMS_API/hospital_activity_status_api.php?action=showAll");
                url = new URL("http://doc.gsinfotec.in/loginphpfile.php?action=deletepermanentleaves");
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
                        .appendQueryParameter("leaves", params[0])
                        .appendQueryParameter("doctorid", params[1]);

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
                Log.d("dfcds", "Response Code:-" + response_code);
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
            Log.d("ssdgggfdddsd", result);

            pdLoading.dismiss();


        }
    }
    private class AsyncLogin1 extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(PermanentLeavesDoctor.this);
        HttpURLConnection conn;
        URL url = null;


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

//                url = new URL("http://rotaryapp.mdimembrane.com/HMS_API/hospital_activity_status_api.php?action=showAll");
                url = new URL("http://doc.gsinfotec.in/loginphpfile.php?action=fetchpermanentleaves");
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
                        .appendQueryParameter("doctorid", params[0]);

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
                Log.d("dfcds", "Response Code:-" + response_code);
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
            //  Log.d("ssdgggfdddsd", result);

            pdLoading.dismiss();
            if (result != null) {
                //   Toast.makeText(getApplicationContext(), "Result:-" + result, Toast.LENGTH_LONG).show();
                JSONArray jsonArray;

                try {
                    jsonArray = new JSONArray(result);


                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String fixdate = jsonObject.getString("fixdate");
                        addDynamicMOC(fixdate,1);
//                        final LinearLayout newView = (LinearLayout) PermanentLeavesDoctor.this
//                                .getLayoutInflater().inflate(R.layout.add_new_permanent_leaves, null);
//                        EditText digital = (EditText) newView.findViewById(R.id.appointmentTime);
//                        digital.setText(fixdate);
//                        digital.requestFocus();

                    }


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            }
        }
    }
}

