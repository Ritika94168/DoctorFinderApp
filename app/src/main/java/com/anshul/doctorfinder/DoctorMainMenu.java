package com.anshul.doctorfinder;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
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

public class DoctorMainMenu extends AppCompatActivity {
    LinearLayout todayavaliability, patients, editdetails, sms, payment, logout,subsription;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    String doctorid,doctornameSTR, doccontactSTR,docspecializationpnameSTR;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_main_menu);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        final Intent intent=getIntent();
        doctorid=intent.getStringExtra("doctorid");
        logout = (LinearLayout) findViewById(R.id.logout);
        todayavaliability = (LinearLayout) findViewById(R.id.todayavailability);
        patients = (LinearLayout) findViewById(R.id.managepatients);
        editdetails = (LinearLayout) findViewById(R.id.editDetails);
        subsription=(LinearLayout)findViewById(R.id.subscription);
        sms = (LinearLayout) findViewById(R.id.smsmodule);
        payment = (LinearLayout) findViewById(R.id.recentPayments);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);

     //   new AsyncDoctorname().execute(doctorid);

        patients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(DoctorMainMenu.this, DoctorManagePatients.class);
                intent1.putExtra("doctorid", doctorid);
                startActivity(intent1);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });
        subsription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(DoctorMainMenu.this, ChooseSubscription.class);
                intent1.putExtra("doctorid", doctorid);
                startActivity(intent1);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });

        todayavaliability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = {"Week Days", "Set Permanent Leaves", "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(DoctorMainMenu.this, R.style.PositiveButtonStyle11);
                builder.setTitle(Html.fromHtml("<b>" + "Select Option:" + "</b>"));
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0: // Add New Faculty
                                Intent intent1=new Intent(DoctorMainMenu.this,WeekDaysAvailability.class);
                                intent1.putExtra("doctorid",doctorid);
                                startActivity(intent1);
                                overridePendingTransition(R.anim.right_in,R.anim.left_out);
                                break;
                            case 1: // Edit Faculty Information
                                Intent intent=new Intent(DoctorMainMenu.this,PermanentLeavesDoctor.class);
                                intent.putExtra("doctorid",doctorid);
                                startActivity(intent);
                                overridePendingTransition(R.anim.right_in,R.anim.left_out);
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

        editdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(DoctorMainMenu.this,DoctorPersonalDetails.class);
                intent1.putExtra("doctorid",doctorid);
                startActivity(intent1);
                overridePendingTransition(R.anim.right_in,R.anim.left_out);
            }
        });
        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = {"Send SMS", "View Inbox", "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(DoctorMainMenu.this, R.style.PositiveButtonStyle11);
                builder.setTitle(Html.fromHtml("<b>" + "Select Option:" + "</b>"));
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0: // Add New Faculty
                               Intent intent1=new Intent(DoctorMainMenu.this,SmsModule.class);
                                intent1.putExtra("mobilenumber","");
                               startActivity(intent1);
                               overridePendingTransition(R.anim.right_in,R.anim.left_out);
                                break;
                            case 1: // Edit Faculty Information
                                Intent intent = new Intent(DoctorMainMenu.this, SmsReceiver.class);
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
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(DoctorMainMenu.this, R.style.PositiveButtonStyle11);
                alert.setCancelable(false);

                alert.setTitle("Alert!!");

                alert.setMessage("Are You Sure You Want To Logout ??");

                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {

                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.remove("LoginSession");
                        editor.remove("Username");
                        editor.remove("Password");
                        editor.remove("docid");
                        editor.apply();
                        Intent intent1=new Intent(DoctorMainMenu.this,FirstActivity.class);
                        startActivity(intent1);
                        overridePendingTransition(R.anim.enter, R.anim.leave);
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

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert = new AlertDialog.Builder(DoctorMainMenu.this, R.style.PositiveButtonStyle11);
        alert.setCancelable(false);

        alert.setTitle("Alert!!");

        alert.setMessage("Are You Sure You Want To Logout ??");

        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.remove("LoginSession");
                editor.remove("Username");
                editor.remove("Password");
                editor.remove("docid");
                editor.apply();
                Intent intent1=new Intent(DoctorMainMenu.this,FirstActivity.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.enter, R.anim.leave);
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

        return;
    }

}
