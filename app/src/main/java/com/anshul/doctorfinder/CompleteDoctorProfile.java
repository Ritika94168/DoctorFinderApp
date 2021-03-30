package com.anshul.doctorfinder;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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

public class CompleteDoctorProfile extends AppCompatActivity {

    ImageButton locImage;
    ImageView doctorimage;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    Button bookAppointment;
    String doctorid,finallocationSTR;
    String s1;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    String ps1, ps2, ps3, ps4;
    Boolean resultSTR = false;
    Boolean resultSTR1 = false;
    String docimageSTR;
    TextView docname,specification,address,mobileno,whatsappnumber,email,rating,reviews,fees,description;
    String docnameSTR,specificationSTR,addressSTR,mobilenoSTR,whatsappnumberSTR,emailSTR,ratingSTR,reviewsSTR,feesSTR,descriptionSTR;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_doctor_profile);
        locImage = (ImageButton) findViewById(R.id.locationImage);
        doctorimage=(ImageView)findViewById(R.id.doctorImage);
        bookAppointment=(Button)findViewById(R.id.book_now);
        Intent intent=getIntent();
        doctorid=intent.getStringExtra("doctorid");
         finallocationSTR=intent.getStringExtra("finallocationSTR");
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        s1 = sharedpreferences.getString("LoginSession", "");
        ps1 = sharedpreferences.getString("LoginSession1", "");
        ps2 = sharedpreferences.getString("Username1", "");
        ps3 = sharedpreferences.getString("Password1", "");
        ps4 = sharedpreferences.getString("pid", "");
        ConnectivityManager connec = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

        } else {

            Toast toast = Toast.makeText(getApplicationContext(), "Not Connected To Internet!!!Please Connected To Internet ", Toast.LENGTH_LONG);
            View view = toast.getView();

//Gets the actual oval background of the Toast then sets the colour filter
            view.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);

//Gets the TextView from the Toast so it can be editted
            TextView text = view.findViewById(android.R.id.message);
            text.setTextColor(Color.WHITE);

            toast.show();

        }

        locImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strUri = "http://maps.google.com/maps?q=Location:-" + finallocationSTR;
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });

        docname=(TextView)findViewById(R.id.nameTextView);
        specification=(TextView)findViewById(R.id.estimatedTimeTextView);
        address=(TextView)findViewById(R.id.transactionIdTextView);
        mobileno=(TextView)findViewById(R.id.opdChargesTextView);
        whatsappnumber=(TextView)findViewById(R.id.doctorNameTextView);
        email=(TextView)findViewById(R.id.email_id);
        rating=(TextView)findViewById(R.id.doctorRoomTextView);
        reviews=(TextView)findViewById(R.id.opdIdTextView);
        fees=(TextView)findViewById(R.id.feesTv);
        description=(TextView)findViewById(R.id.idTextView);


        docnameSTR=docname.getText().toString();
        specificationSTR=specification.getText().toString();
        addressSTR=address.getText().toString();
        mobilenoSTR=mobileno.getText().toString();
        whatsappnumberSTR=whatsappnumber.getText().toString();
        emailSTR=email.getText().toString();
        ratingSTR=rating.getText().toString();
        reviewsSTR=reviews.getText().toString();
        feesSTR=fees.getText().toString();
        descriptionSTR=description.getText().toString();


        setTitle("Doctor Profile");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        new AsyncLogin().execute(doctorid);

        bookAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ps1.equals("LoggedInPatient") && resultSTR1) {
//                    Intent intent = new Intent(CompleteDoctorProfile.this, PatientMainMenu.class);
//                    intent.putExtra("pid", ps4);
//                    startActivity(intent);
//                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    Intent intent1 = new Intent(CompleteDoctorProfile.this, BookAppointmentActivity.class);
                    intent1.putExtra("docnamenext", docnameSTR);
                    intent1.putExtra("docaddrress", addressSTR);
                    intent1.putExtra("doccontact", mobilenoSTR);
                    intent1.putExtra("docwhatsapp", whatsappnumberSTR);
                    intent1.putExtra("docfees", feesSTR);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }
                else {
                    Intent intent = new Intent(CompleteDoctorProfile.this, PatientLoginScreen.class);
                   // intent.putExtra("pid", ps4);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);

                }
            }
        });
    }
    private class AsyncLogin extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(CompleteDoctorProfile.this);
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
                url = new URL("http://doc.gsinfotec.in/loginphpfile.php?action=fetchCompleteDetailsDoctor");
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
                        .appendQueryParameter("doctorId", params[0]);

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

            pdLoading.dismiss();

            if (result != null) {
                //  Toast.makeText(getApplicationContext(), "Result:-" + result, Toast.LENGTH_LONG).show();
                JSONArray jsonArray;

                try {
                    jsonArray = new JSONArray(result);


                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        docimageSTR = jsonObject.getString("docimage");
                        docnameSTR = jsonObject.getString("docname");
                        specificationSTR = jsonObject.getString("specialization");
                        addressSTR = jsonObject.getString("address");
                        mobilenoSTR = jsonObject.getString("contactnumber");
                        whatsappnumberSTR = jsonObject.getString("whatsappnumber");
                        emailSTR = jsonObject.getString("email");
//                        ratingSTR = jsonObject.getString("ipd_payment_recieved");
//                        reviewsSTR = jsonObject.getString("ipd_discharge_date");
                        feesSTR = jsonObject.getString("fees");
                        descriptionSTR = jsonObject.getString("description");



                        docname.setText(docnameSTR);
                        specification.setText(specificationSTR);
                        address.setText(addressSTR);
                        mobileno.setText(mobilenoSTR);
                        whatsappnumber.setText(whatsappnumberSTR);
                        email.setText(emailSTR);
                        rating.setText(ratingSTR);
                        reviews.setText(reviewsSTR);
                        fees.setText(feesSTR);
                        description.setText(descriptionSTR);

                        new AsyncLogin1().execute(ps2, ps3);


                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            }
        }
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
    public class AsyncLogin1 extends AsyncTask<String, String, String> {
        private ProgressDialog dialog = new ProgressDialog(CompleteDoctorProfile.this);

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

            if (!result.equals("")) {

                resultSTR1 = true;

            }
        }
    }
}
