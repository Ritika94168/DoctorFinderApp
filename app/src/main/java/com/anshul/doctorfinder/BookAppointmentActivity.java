package com.anshul.doctorfinder;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class BookAppointmentActivity extends AppCompatActivity {
    TextInputEditText bookappintment;
    TextInputEditText bookappointmenttime;
    public static final int DATE_PICKER_ID = 1;
    int day, month, year;
    String dobSTR,timeSTR;
    Button confirmBooking;
    static final int TIME_DIALOG_ID = 1111;
    private int hour;
    private int minute;
    TextView name, doctoraddress, contact, whatsapp, fees;
    String nameSTR, addressSTR, contactSTR, whatsappSTR, feesSTR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);
        bookappintment = (TextInputEditText) findViewById(R.id.appointmentDate);
        bookappointmenttime = (TextInputEditText) findViewById(R.id.appointmentTime);
        name = (TextView) findViewById(R.id.docname);
        doctoraddress = (TextView) findViewById(R.id.address);
        contact = (TextView) findViewById(R.id.contact);
        whatsapp = (TextView) findViewById(R.id.whatsapp);
        fees = (TextView) findViewById(R.id.fees);
        confirmBooking = (Button) findViewById(R.id.confirmBooking);
        Intent intent = getIntent();
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
//                final Calendar mcurrentTime = Calendar.getInstance();
//                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//                int minute = mcurrentTime.get(Calendar.MINUTE);
//
//                TimePickerDialog mTimePicker;
//                mTimePicker = new TimePickerDialog(BookAppointmentActivity.this, new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                        bookappointmenttime.setText( selectedHour + ":" + selectedMinute + "" + am_pm);
//                    }
//                }, hour, minute, false);//Yes 24 hour time
//                mTimePicker.setTitle("Select Time");
//                mTimePicker.show();
            }
        });
        confirmBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dobSTR = bookappintment.getText().toString();
                timeSTR=bookappointmenttime.getText().toString();

                if (dobSTR.equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Please Select Appointment Date", Toast.LENGTH_LONG);
                    View view = toast.getView();

//Gets the actual oval background of the Toast then sets the colour filter
                    view.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);

//Gets the TextView from the Toast so it can be editted
                    TextView text = view.findViewById(android.R.id.message);
                    text.setTextColor(Color.BLACK);

                    toast.show();
                }
                if (timeSTR.equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Please Select Appointment Time", Toast.LENGTH_LONG);
                    View view = toast.getView();

//Gets the actual oval background of the Toast then sets the colour filter
                    view.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);

//Gets the TextView from the Toast so it can be editted
                    TextView text = view.findViewById(android.R.id.message);
                    text.setTextColor(Color.BLACK);

                    toast.show();
                }


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

    private static String utilTime(int value) {

        if (value < 10)
            return "0" + String.valueOf(value);
        else
            return String.valueOf(value);
    }

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

            year  = selectedYear;
            month = selectedMonth;
            day   = selectedDay;

            bookappintment.setText(new StringBuilder().append(day)
                    .append("-").append(month + 1).append("-").append(year)
                    .append(" "));
            bookappointmenttime.setVisibility(View.VISIBLE);

        }
    };
}
