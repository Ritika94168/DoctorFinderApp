package com.anshul.doctorfinder;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
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


public class SearchDoctorList extends AppCompatActivity {
    TextView locationTV;
    Bitmap bmp;
    TextView doctortypeTV;
    MyListAdapter adapter;
    ArrayList<DisplayList> displayList = new ArrayList<DisplayList>();
    ArrayList<String> doctorList = new ArrayList<String>();
    ListView listview;
    private long mBackPressed;
    private static final int TIME_INTERVAL = 2000;
    Button reset, modify;

    String locationSTR = "";
    String locationManualSTR = "";
    String finalLocationSTR="";
    String docSTR = "";
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    String docImage,doctor_name, specialization, description, address, doctorid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_doctor_list);
        listview = (ListView) findViewById(R.id.list);
        locationTV = (TextView) findViewById(R.id.locationTV);
        doctortypeTV = (TextView) findViewById(R.id.doctorTypeTV);
        reset = (Button) findViewById(R.id.reset);
        modify = (Button) findViewById(R.id.modify);

        ConnectivityManager connec = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {


            Intent intent = getIntent();
            locationSTR = intent.getStringExtra("Location");
            locationManualSTR = intent.getStringExtra("LocationManual");
            docSTR = intent.getStringExtra("DoctorType");
            setTitle("Doctor's List");
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);


            if ((locationSTR.equals("")) && (!locationManualSTR.equals(""))) {
                locationTV.setText(locationManualSTR);
                finalLocationSTR=locationManualSTR;
            }
            if ((!locationSTR.equals("")) && (locationManualSTR.equals(""))) {
                locationTV.setText(locationSTR);
                finalLocationSTR=locationSTR;
            }


            doctortypeTV.setText(docSTR);

            doctortypeTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "Doctor Type is not editable", Toast.LENGTH_LONG).show();
                }
            });

            reset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(SearchDoctorList.this, MainActivity.class);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.enter, R.anim.leave);

                }
            });

            modify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = getIntent();
                    setResult(RESULT_FIRST_USER, intent);
                    finish();
                    overridePendingTransition(R.anim.enter, R.anim.leave);
                }
            });

            adapter = new MyListAdapter(SearchDoctorList.this, displayList);
            listview.setAdapter(adapter);
            new AsyncLogin().execute(docSTR,locationManualSTR,locationSTR);
            // Add List Manually For Testing,,,because list is loaded from mysql database after Database Creation
            displayList.add(new DisplayList(bmp , doctor_name, docSTR, description, address));
//            displayList.add(new DisplayList("" + R.drawable.hospital, "Ritika Gaba", docSTR, "I have 4 Year Experience At Rotary Hospital Ambala Cantt", "20/21 Industrial Area,Ambala Cantt", "Rating:-5"));
//            displayList.add(new DisplayList("" + R.drawable.hospital, "Bunty Bindra", docSTR, "I have 5 Year Experience", "20/21 Industrial Area,Ambala Cantt", "Rating:-6"));
//            displayList.add(new DisplayList("" + R.drawable.hospital, "Munish Gautum", docSTR, "I have 1 Year Experience", "20/21 Industrial Area,Ambala Cantt", "Rating:-8"));
            adapter = new MyListAdapter(SearchDoctorList.this, displayList);
            listview.setAdapter(adapter);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, final long id) {
                    Intent intent = new Intent(SearchDoctorList.this, CompleteDoctorProfile.class);
                    intent.putExtra("doctorid",doctorList.get(position));
                    intent.putExtra("finallocationSTR",finalLocationSTR);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }
            });

        } else {

            Toast toast = Toast.makeText(getApplicationContext(), "Not Connected To Internet!!!Please Enable Internet And Continue", Toast.LENGTH_LONG);
            View view = toast.getView();
            view.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
            TextView text = view.findViewById(android.R.id.message);
            text.setTextColor(Color.BLACK);
            toast.show();

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
    private class AsyncLogin extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(SearchDoctorList.this);
        HttpURLConnection conn;
        ArrayList<DisplayList> displayList = new ArrayList<DisplayList>();
        URL url = null;
        ArrayList<String> arraylist = new ArrayList<String>();
        ArrayList<String> amountlist = new ArrayList<String>();

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
                url = new URL("http://doc.gsinfotec.in/loginphpfile.php?action=showAll");
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
                        .appendQueryParameter("DoctorType", params[0])
                        .appendQueryParameter("Location", params[1])
                        .appendQueryParameter("LocationManual", params[2]);
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
                        doctorid = jsonObject.getString("docid");
                        docImage = jsonObject.getString("docimage");
                        doctor_name = jsonObject.getString("docname");
                        specialization = jsonObject.getString("specialization");
                        description = jsonObject.getString("description");
                        address = jsonObject.getString("address");

//                        String Qrimage = jsonObject.getString("imagefieldname");
                        System.out.println(docImage);

                        byte[] qrimage = Base64.decode(docImage.getBytes(),Base64.DEFAULT);

                        System.out.println(qrimage);
                        bmp = BitmapFactory.decodeByteArray(qrimage, 0, qrimage.length);
//                        ImageView imageview = (ImageView) findViewById(R.id.flag);

//                        imageview.setImageBitmap(bmp);

//jsonObject.get
                        displayList.add(new DisplayList(bmp, doctor_name, specialization, description, address));
                        doctorList.add(doctorid);

                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                adapter = new MyListAdapter(SearchDoctorList.this, displayList);
                listview.setAdapter(adapter);
            }

        }
    }
    }