package com.anshul.doctorfinder;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
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

public class DoctorManagePatients extends AppCompatActivity {
    MyListAdapterAllPatients adapter;
    ArrayList<DisplayListAllPatientDetails> displayList = new ArrayList<DisplayListAllPatientDetails>();
    ArrayList<String> bookingsList = new ArrayList<String>();
    ListView listview;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private static final int CALL_PERMISSION_CODE = 102;
    String phonenumber;
    String doctorname, doctorspecification, bookingdate, bookingtime, doctorid1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_manage_patients);
        setTitle("Patient's List");
        listview = (ListView) findViewById(R.id.list);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = getIntent();
        final String doctorid = intent.getStringExtra("doctorid");


        checkPermission(
                Manifest.permission.CALL_PHONE,
                CALL_PERMISSION_CODE);


        adapter = new MyListAdapterAllPatients(DoctorManagePatients.this, displayList);
        listview.setAdapter(adapter);
        new AsyncLogin().execute(doctorid);
        displayList.add(new DisplayListAllPatientDetails(doctorname, doctorspecification, bookingdate, bookingtime));
        adapter = new MyListAdapterAllPatients(DoctorManagePatients.this, displayList);
        listview.setAdapter(adapter);


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, final long id) {
                final CharSequence[] items = {"Send SMS", "Make Call", "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(DoctorManagePatients.this, R.style.PositiveButtonStyle11);
                builder.setTitle(Html.fromHtml("<b>" + "Select Option:" + "</b>"));
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0: // Add New Faculty
                                Intent intent1=new Intent(DoctorManagePatients.this,SmsModule.class);
                                intent1.putExtra("mobilenumber",doctorspecification);
                                startActivity(intent1);
                                overridePendingTransition(R.anim.right_in,R.anim.left_out);
                                break;
                            case 1: // Edit Faculty Information
                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + doctorspecification));
                                if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    Toast.makeText(getApplicationContext(), "Please Give Call Permission in Apps under Settings Option", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                startActivity(intent);
                                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                break;
                            case 2: // cancel
                                dialog.cancel();
                                break;
                        }

                    }
                });
                final AlertDialog alert1 = builder.create();
                ListView listView = alert1.getListView();

                // Set the divider color of alert dialog list view
                listView.setDivider(new ColorDrawable(Color.BLACK));

                // Set the divider height of alert dialog list view
                listView.setDividerHeight(8);
                listView.setFooterDividersEnabled(false);
                listView.addFooterView(new View(getApplicationContext()));
                alert1.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
                alert1.setCanceledOnTouchOutside(false);
                alert1.show();




            }
        });

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

    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(DoctorManagePatients.this, permission)
                == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(DoctorManagePatients.this,
                    new String[]{permission},
                    requestCode);
        }


    }

    private class AsyncLogin extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(DoctorManagePatients.this);
        HttpURLConnection conn;
        ArrayList<DisplayListAllPatientDetails> displayList = new ArrayList<DisplayListAllPatientDetails>();
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
                url = new URL("http://doc.gsinfotec.in/loginphpfile.php?action=fetchDoctorPatients");
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
            if (result != null) {

                JSONArray jsonArray;

                try {
                    jsonArray = new JSONArray(result);


                    for (int i = 0; i < jsonArray.length(); i++) {


                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        doctorname = jsonObject.getString("pname");
                        doctorspecification = jsonObject.getString("contactnumber");
                        bookingdate = jsonObject.getString("booking_date");
                        bookingtime = jsonObject.getString("booking_time");

                        displayList.add(new DisplayListAllPatientDetails(doctorname, doctorspecification, bookingdate, bookingtime));
                        bookinglist.add(doctorid1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                adapter = new MyListAdapterAllPatients(DoctorManagePatients.this, displayList);
                listview.setAdapter(adapter);
            }

        }
    }
}
