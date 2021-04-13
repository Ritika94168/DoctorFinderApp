package com.anshul.doctorfinder;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.SmsManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BookAppointmentActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextInputEditText bookappintment;
    TextInputEditText bookappointmenttime;
    public static final int DATE_PICKER_ID = 1;
    int day, month, year;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    String dobSTR, timeSTR;
    Button confirmBooking;
    static final int TIME_DIALOG_ID = 1111;
    private ArrayList<String> availabletimedata;
    private int hour;
    private int minute;
    Spinner doctorSpinner;
    ArrayAdapter<String> langAdapter;
    TextView name, doctoraddress, contact, whatsapp, fees;
    String nameSTR, addressSTR, contactSTR, whatsappSTR, feesSTR;
    String currentdate, currentday;
    Button buttonOk;
    TableRow tablerow;
    String doctoridSTR, patientidSTR;
    final long today = System.currentTimeMillis() - 1000;
    ArrayList<String> doctor_type = new ArrayList<String>();
    String doctornameSTR, docaddressSTR, doccontactSTR, docwhatsappSTR, docfeesSTR,pnameSTR;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    public static final int REQUEST_CODE_ASK_PERMISSIONS = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);
        bookappintment = (TextInputEditText) findViewById(R.id.appointmentDate);
        bookappointmenttime = (TextInputEditText) findViewById(R.id.appointmentTime);
        name = (TextView) findViewById(R.id.docname);
        tablerow = (TableRow) findViewById(R.id.availabletimetablerow);
        doctoraddress = (TextView) findViewById(R.id.address);
        contact = (TextView) findViewById(R.id.contact);
        doctorSpinner = (Spinner) findViewById(R.id.doctorSpinner);
        buttonOk = (Button) findViewById(R.id.buttonOk);
        whatsapp = (TextView) findViewById(R.id.whatsapp);
        fees = (TextView) findViewById(R.id.fees);
        confirmBooking = (Button) findViewById(R.id.confirmBooking);
        final Intent intent = getIntent();
        doctoridSTR = intent.getStringExtra("doctorid");
        patientidSTR = intent.getStringExtra("pid");
        doctornameSTR = intent.getStringExtra("docnamenext");
        docaddressSTR = intent.getStringExtra("docaddrress");
        doccontactSTR = intent.getStringExtra("doccontact");
        docwhatsappSTR = intent.getStringExtra("docwhatsapp");
        docfeesSTR = intent.getStringExtra("docfees");
        new AsyncPatientname().execute(patientidSTR);

        setTitle("Dr" + " " + doctornameSTR + " " + "Appointment");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (ContextCompat.checkSelfPermission(BookAppointmentActivity.this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(BookAppointmentActivity.this,
                    Manifest.permission.SEND_SMS)) {

            } else {
                ActivityCompat.requestPermissions(BookAppointmentActivity.this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }
        nameSTR = doctornameSTR;
        addressSTR = docaddressSTR;
        contactSTR = doccontactSTR;
        whatsappSTR = docwhatsappSTR;
        feesSTR = docfeesSTR;

        name.setText(nameSTR);
        doctoraddress.setText(addressSTR);
        contact.setText(contactSTR);
        whatsapp.setText(whatsappSTR);
        fees.setText(feesSTR);

        doctorSpinner.setOnItemSelectedListener(this);
        langAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_text, doctor_type);
        langAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        doctorSpinner.setAdapter(langAdapter);

        final Calendar c = Calendar.getInstance();
        // Current Hour
        hour = c.get(Calendar.HOUR_OF_DAY);
        // Current Minute
        minute = c.get(Calendar.MINUTE);

        // set current time into output textview
        //  updateTime(hour, minute);


        bookappintment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectDate();
            }
        });
        bookappointmenttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(TIME_DIALOG_ID);

            }
        });

        confirmBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dobSTR = bookappintment.getText().toString();
//               timeSTR = doctorSpinner.getSelectedItem().toString();
                dobSTR = bookappintment.getText().toString();
                timeSTR = doctorSpinner.getSelectedItem().toString();
                if (dobSTR.equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Please Select Appointment Date", Toast.LENGTH_LONG);
                    View view = toast.getView();

//Gets the actual oval background of the Toast then sets the colour filter
                    view.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);

//Gets the TextView from the Toast so it can be editted
                    TextView text = view.findViewById(android.R.id.message);
                    text.setTextColor(Color.BLACK);

                    toast.show();
                    return;
                }

                AlertDialog.Builder alert = new AlertDialog.Builder(BookAppointmentActivity.this, R.style.PositiveButtonStyle11);
                alert.setCancelable(false);
                alert.setTitle("Alert!!");
                alert.setMessage("Sure To Confirm Booking ??");

                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {


                        new AsyncLoginBooking().execute(dobSTR, doctorSpinner.getSelectedItem().toString(), doctoridSTR, patientidSTR);
                        LayoutInflater factory = LayoutInflater.from(BookAppointmentActivity.this);

//text_entry is an Layout XML file containing two text field to display in alert dialog
                        View textEntryView = factory.inflate(R.layout.confirm_booking_layout, null);
                        TextView doctname = textEntryView.findViewById(R.id.doctname);
                        final TextView appdate = textEntryView.findViewById(R.id.appointmentdate);
                        final TextView apptime = textEntryView.findViewById(R.id.appointmenttime);
                        doctname.setText(nameSTR);
                        appdate.setText(dobSTR);
                        apptime.setText(doctorSpinner.getSelectedItem().toString());

                        final AlertDialog.Builder alert = new AlertDialog.Builder(BookAppointmentActivity.this, R.style.PositiveButtonStyle11);
                        alert.setView(textEntryView).setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int whichButton) {


                                        try{
                                            SmsManager smgr = SmsManager.getDefault();
                                            smgr.sendTextMessage(contactSTR,null,"Hi, Dr."+" "+doctornameSTR+" "+ "Your Appointment with :-"+" "+pnameSTR+" "+"is confirmed on"+" "+dobSTR+" "+"between"+" "+doctorSpinner.getSelectedItem().toString(),null,null);
                                        }
                                        catch (Exception e){

                                        }
                                        //  screenshot(getWindow().getDecorView().getRootView(), "result");
                                        Intent intent1 = new Intent(BookAppointmentActivity.this, PatientMainMenu.class);
                                        intent1.putExtra("doctorid", doctoridSTR);
                                        intent1.putExtra("pid", patientidSTR);
                                        startActivity(intent1);
                                        overridePendingTransition(R.anim.enter, R.anim.leave);


                                    }
                                });
                        AlertDialog alertDialog = alert.create();
                        alertDialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.show();

                    }
                });

                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert1 = alert.create();
                alert1.setCanceledOnTouchOutside(false);
                alert1.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
                alert1.show();
                // get current date in android
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                String formattedDate = df.format(c);
                currentdate = formattedDate;


                //get currentDay in android
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                Date d = new Date();
                String dayOfTheWeek = sdf.format(d);
                currentday = dayOfTheWeek;


                // Here you can retrieve time values into spinner and after select time,you get alert message for booking confirmation


            }
        });


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(getApplicationContext(),
                            "MESSAGE RECIEVED SUCCESSFULLY", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }
    public void openWhatsApp() {
//        String toNumber = "+91"+" "+whatsappSTR; // contains spaces.
//        toNumber = toNumber.replace("+", "").replace(" ", "");
//        Uri uri = Uri.parse("smsto:" + toNumber);
//        Intent sendIntent = new Intent("android.intent.action.MAIN");
//        sendIntent.putExtra("jid", toNumber + "@s.whatsapp.net");
//        sendIntent.putExtra(Intent.EXTRA_TEXT, "Your Appointment with Patient-ID:-"+" "+patientidSTR+" "+"is confirmed on"+" "+dobSTR+" "+"at"+" "+doctorSpinner.getSelectedItem().toString());
//        sendIntent.setAction(Intent.ACTION_SEND);
//        sendIntent.setPackage("com.whatsapp");
//        sendIntent.setType("text/plain");
//        startActivity(sendIntent);

    }
    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {


        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            // TODO Auto-generated method stub
            hour = hourOfDay;
            minute = minutes;

            updateTime(hour, minute);

        }

    };

    // Used to convert 24hr format to 12hr format with AM/PM values
    private void updateTime(int hours, int mins) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";


        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        bookappointmenttime.setText(aTime);
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

    private void SelectDate() {

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);


        showDialog(DATE_PICKER_ID);

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:

                return new DatePickerDialog(this, pickerListener, year, month, day);

            // return null;

            case TIME_DIALOG_ID:

                // set time picker as current time
                return new TimePickerDialog(this, timePickerListener, hour, minute,
                        false);


            //    return null;


        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {

            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            bookappintment.setText(new StringBuilder().append(day)
                    .append("-").append(month + 1).append("-").append(year)
                    .append(" "));

            dobSTR = bookappintment.getText().toString();
            tablerow.setVisibility(View.INVISIBLE);
            confirmBooking.setVisibility(View.INVISIBLE);
            //  bookappointmenttime.setVisibility(View.VISIBLE);
            new AsyncLogin().execute(dobSTR, doctoridSTR);


        }
    };

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class AsyncLogin extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(BookAppointmentActivity.this);
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
                url = new URL("http://doc.gsinfotec.in/loginphpfile.php?action=fetchDateDetails");
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
                        .appendQueryParameter("appointmentdate", params[0])
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

            pdLoading.dismiss();
            doctor_type.clear();
            if (result != null) {
                Toast toast = Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG);
                View view = toast.getView();

//Gets the actual oval background of the Toast then sets the colour filter
                view.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);

//Gets the TextView from the Toast so it can be editted
                TextView text = view.findViewById(android.R.id.message);
                text.setTextColor(Color.WHITE);

                toast.show();
                JSONArray jsonArray;

                try {
                    jsonArray = new JSONArray(result);


                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String st1 = jsonObject.getString("start_time_1");
                        String et1 = jsonObject.getString("end_time_1");
                        String st2 = jsonObject.getString("start_time_2");
                        String et2 = jsonObject.getString("end_time_2");
                        if ((!st1.equals("") && !et1.equals(""))) {
                            doctor_type.add(st1 + "-" + et1);
                        }
                        if ((!st2.equals("") && !et2.equals(""))) {
                            doctor_type.add(st2 + "-" + et2);
                        }
                        tablerow.setVisibility(View.VISIBLE);
                        langAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_text, doctor_type);
                        langAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
                        doctorSpinner.setAdapter(langAdapter);

                        confirmBooking.setVisibility(View.VISIBLE);


                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            }

        }
    }

    private class AsyncLoginBooking extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(BookAppointmentActivity.this);
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
                url = new URL("http://doc.gsinfotec.in/loginphpfile.php?action=addappointmentDetails");
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
                        .appendQueryParameter("appointmentdate", params[0])
                        .appendQueryParameter("appointmenttime", params[1])
                        .appendQueryParameter("doctorid", params[2])
                        .appendQueryParameter("patientid", params[3]);

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

        }
    }

    private class AsyncPatientname extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(BookAppointmentActivity.this);
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
                url = new URL("http://doc.gsinfotec.in/loginphpfile.php?action=fetchCompletePatientPersonaldetails");
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
                //  Toast.makeText(getApplicationContext(), "Result:-" + result, Toast.LENGTH_LONG).show();
                JSONArray jsonArray;

                try {
                    jsonArray = new JSONArray(result);


                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        pnameSTR = jsonObject.getString("pname");

                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            }

        }
    }

}
