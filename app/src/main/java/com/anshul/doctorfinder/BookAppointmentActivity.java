package com.anshul.doctorfinder;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

    ArrayList<String> doctor_type = new ArrayList<String>();

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
        buttonOk=(Button)findViewById(R.id.buttonOk);
        whatsapp = (TextView) findViewById(R.id.whatsapp);
        fees = (TextView) findViewById(R.id.fees);
        confirmBooking = (Button) findViewById(R.id.confirmBooking);
        final Intent intent = getIntent();
        final String doctoridSTR = intent.getStringExtra("doctorid");
        final String patientidSTR = intent.getStringExtra("pid");
        final String doctornameSTR = intent.getStringExtra("docnamenext");
        final String docaddressSTR = intent.getStringExtra("docaddrress");
        final String doccontactSTR = intent.getStringExtra("doccontact");
        final String docwhatsappSTR = intent.getStringExtra("docwhatsapp");
        final String docfeesSTR = intent.getStringExtra("docfees");


        setTitle("Dr" + " " + doctornameSTR + " " + "Appointment");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

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
                dobSTR = bookappintment.getText().toString();
                timeSTR = bookappointmenttime.getText().toString();

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

                 new AsyncLogin().execute(dobSTR);
                // new AsyncLogin1().execute(currentday);


                AlertDialog.Builder alert = new AlertDialog.Builder(BookAppointmentActivity.this, R.style.PositiveButtonStyle11);
                alert.setCancelable(false);
                alert.setTitle("Alert!!");
                alert.setMessage("Sure To Confirm Booking ??");

                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Congrats!!Your Appointment is Confirmed", Toast.LENGTH_LONG);
                        View view = toast.getView();

//Gets the actual oval background of the Toast then sets the colour filter
                        view.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);

//Gets the TextView from the Toast so it can be editted
                        TextView text = view.findViewById(android.R.id.message);
                        text.setTextColor(Color.BLACK);
                        toast.show();

                        LayoutInflater factory = LayoutInflater.from(BookAppointmentActivity.this);

//text_entry is an Layout XML file containing two text field to display in alert dialog
                        View textEntryView = factory.inflate(R.layout.confirm_booking_layout, null);
                        TextView doctname=textEntryView.findViewById(R.id.doctname);
                        TextView appdate=textEntryView.findViewById(R.id.appointmentdate);
                        TextView apptime=textEntryView.findViewById(R.id.appointmenttime);
                        doctname.setText(nameSTR);
                        appdate.setText(dobSTR);
                        apptime.setText("2.00-4.00");

                        final AlertDialog.Builder alert = new AlertDialog.Builder(BookAppointmentActivity.this, R.style.PositiveButtonStyle11);
                        alert.setView(textEntryView).setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int whichButton) {

                                   Intent intent1=new Intent(BookAppointmentActivity.this,PatientMainMenu.class);
                                   intent1.putExtra("doctorid",doctoridSTR);
                                   intent1.putExtra("pid",patientidSTR);
                                   startActivity(intent1);
                                   overridePendingTransition(R.anim.enter,R.anim.leave);



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


            }
        });


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
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            bookappintment.setText(new StringBuilder().append(day)
                    .append("-").append(month + 1).append("-").append(year)
                    .append(" "));
            //  bookappointmenttime.setVisibility(View.VISIBLE);
            tablerow.setVisibility(View.VISIBLE);


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
                        .appendQueryParameter("appointmentdate", params[0]);

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


        }
    }


}
