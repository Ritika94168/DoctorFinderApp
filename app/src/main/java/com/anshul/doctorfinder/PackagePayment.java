package com.anshul.doctorfinder;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

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

public class PackagePayment extends AppCompatActivity implements PaymentResultListener {
    String doctorid, doctornameSTR, doccontactSTR, docspecializationSTR, plannameSTR, planamountSTR,docemailSTR;
    public static final int CONNECTION_TIMEOUT = 10000;
    TextView docname, specialization, contactno, planname, planamount;
   Float plnamountFloat;
    Button proceed;
    public static final int READ_TIMEOUT = 15000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_payment);

        docname = (TextView) findViewById(R.id.docname);
        specialization = (TextView) findViewById(R.id.specialization);
        contactno = (TextView) findViewById(R.id.contactnumber);
        planname = (TextView) findViewById(R.id.planname);
        planamount = (TextView) findViewById(R.id.planamount);
        proceed = (Button) findViewById(R.id.procced);


        setTitle("Payment Module");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Intent intent = getIntent();
        doctorid = intent.getStringExtra("doctorid");
        plannameSTR = intent.getStringExtra("planname");
        planamountSTR = intent.getStringExtra("planamount");

        plnamountFloat=Float.parseFloat(planamountSTR);

        new AsyncDoctorname().execute(doctorid);


        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // rounding off the amount.
                int amount = Math.round(plnamountFloat * 100);

                // initialize Razorpay account.
                Checkout checkout = new Checkout();

                // set your id as below
                checkout.setKeyID("rzp_test_4KxIjLlk6a66DF");

                // initialize json object
                JSONObject object = new JSONObject();
                try {
                    // to put name
                    object.put("name", doctornameSTR);

                    // put description
                    object.put("description", "Subscription payment");

                    // set images
                    object.put("image", "https://rzp-mobile.s3.amazonaws.com/images/rzp.png");

                    // to set theme color
                    object.put("theme.color", "#00ADC1");

                    // put the currency
                    object.put("currency", "INR");

                    // put amount
                    object.put("amount", amount);

                    // put mobile number
                    object.put("prefill.contact", doccontactSTR);

                    // put email
                    object.put("prefill.email", docemailSTR);

                    // open razorpay to checkout activity
                    checkout.open(PackagePayment.this, object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
    public void onPaymentSuccess(String s) {
     Toast.makeText(getApplicationContext(),"Payment Successfully Done",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(getApplicationContext(),"Oops!! Something Went Wrong",Toast.LENGTH_LONG).show();

    }

    private class AsyncDoctorname extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(PackagePayment.this);
        HttpURLConnection conn;
        URL url = null;


        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


//            pdLoading.setMessage("Loading Please Wait");
//            pdLoading.setCancelable(false);
//            pdLoading.show();
//            pdLoading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.YELLOW));
//            pdLoading.setIndeterminate(false);
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
                Log.d("ddghkjss",params[0]);

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
                Log.d("dfcdsddsdffd", "Response Code:-" + response_code);
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
                        doctornameSTR = jsonObject.getString("docname");
                        docspecializationSTR = jsonObject.getString("specialization");
                        doccontactSTR = jsonObject.getString("contactnumber");
                        docemailSTR=jsonObject.getString("email");
                        docname.setText(doctornameSTR);
                        specialization.setText(docspecializationSTR);
                        contactno.setText(doccontactSTR);
                        planname.setText(plannameSTR);
                        planamount.setText("Rs" + " " + planamountSTR);

                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            }

        }
    }
}
