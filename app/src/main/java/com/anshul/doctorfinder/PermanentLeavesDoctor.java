package com.anshul.doctorfinder;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
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

import java.util.Calendar;

public class PermanentLeavesDoctor extends AppCompatActivity implements View.OnClickListener {
    String doctorid;
    public static final int DATE_PICKER_ID = 1;
    DatePickerDialog picker;
    String dobSTR;
     EditText finalEdit;
    int day, month, year;
     LinearLayout rowView;
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
        setTitle("Permanent Leaves");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


    }

    public void addRow(View view) {
        addDynamicMOC("");
    }
    public void addDynamicMOC(String leavedate) {

        final LinearLayout linearLayoutForm = (LinearLayout) findViewById(R.id.calibration);
        EditText edit;

        for (int i = 0; i < linearLayoutForm.getChildCount(); i++) {
            LinearLayout innerLayout = (LinearLayout) linearLayoutForm.getChildAt(i);
            edit = (EditText) innerLayout.findViewById(R.id.appointmentTime);
            finalEdit = edit;
            edit.setOnClickListener(this);

        }
        final LinearLayout newView = (LinearLayout) PermanentLeavesDoctor.this
                .getLayoutInflater().inflate(R.layout.add_new_permanent_leaves, null);
        EditText digital = (EditText) newView.findViewById(R.id.appointmentTime);
        digital.setText(leavedate);
        digital.requestFocus();

        ImageView btnRemove = (ImageView) newView.findViewById(R.id.remove);
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linearLayoutForm.getChildCount() > 1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PermanentLeavesDoctor.this,R.style.PositiveButtonStyle11);
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
                                    finalEdit.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                }
                            }, year, month, day);
                    picker.show();
    }
}
