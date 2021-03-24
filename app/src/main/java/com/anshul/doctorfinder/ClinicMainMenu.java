package com.anshul.doctorfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class ClinicMainMenu extends AppCompatActivity {
   Button doctors,patients,editdetails,sms,payment,logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_main_menu);
        logout=(Button)findViewById(R.id.logout);
        doctors=(Button)findViewById(R.id.managedoctors);
        patients=(Button)findViewById(R.id.managepatients);
        editdetails=(Button)findViewById(R.id.editdetails);
        sms=(Button)findViewById(R.id.smsmodule);
        payment=(Button)findViewById(R.id.payment);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = { "Send SMS", "View Inbox", "Cancel" };
                AlertDialog.Builder builder = new AlertDialog.Builder(ClinicMainMenu.this,R.style.PositiveButtonStyle11);
                builder.setTitle(Html.fromHtml("<b>"+ "Select Option:"+"</b>"));
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0:

                                break;
                            case 1:
                                Intent intent=new Intent(ClinicMainMenu.this,SmsReceiver.class);
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
        doctors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = { "Add New Doctor", "View Doctor's List", "Cancel" };
                AlertDialog.Builder builder = new AlertDialog.Builder(ClinicMainMenu.this,R.style.PositiveButtonStyle11);
                builder.setTitle(Html.fromHtml("<b>"+ "Select Option:"+"</b>"));
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0: // Add New Faculty
                                Intent intent=new Intent(ClinicMainMenu.this,AddNewDoctor.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                break;
                            case 1: // Edit Faculty Information
                                Intent intent1=new Intent(ClinicMainMenu.this,ShowDoctor.class);
                                startActivity(intent1);
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
                AlertDialog.Builder alert = new AlertDialog.Builder(ClinicMainMenu.this, R.style.PositiveButtonStyle11);
                alert.setCancelable(false);
                alert.setTitle("Alert!!");

                alert.setMessage("Are You Sure You Want To Logout ??");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                      finish();
                      overridePendingTransition(R.anim.enter,R.anim.leave);
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
        Toast.makeText(getApplicationContext(),"Logout Feature Not Allowed",Toast.LENGTH_LONG).show();
        return;
    }
}
