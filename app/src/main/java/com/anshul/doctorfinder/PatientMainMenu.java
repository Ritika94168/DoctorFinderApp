package com.anshul.doctorfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

public class PatientMainMenu extends AppCompatActivity {
    LinearLayout bookdoctor, editdetails, smsmodule, shareapp, transactions, logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_main_menu);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
         bookdoctor = (LinearLayout) findViewById(R.id.mybookings);
        editdetails = (LinearLayout) findViewById(R.id.editDetails);
        smsmodule = (LinearLayout) findViewById(R.id.smsmodule);
       shareapp = (LinearLayout) findViewById(R.id.recentPayments);
       transactions = (LinearLayout) findViewById(R.id.managepatients);
        logout = (LinearLayout) findViewById(R.id.logout);

        smsmodule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = {"Send SMS", "View Inbox", "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(PatientMainMenu.this, R.style.PositiveButtonStyle11);
                builder.setTitle(Html.fromHtml("<b>" + "Select Option:" + "</b>"));
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0:

                                break;
                            case 1:
                                Intent intent = new Intent(PatientMainMenu.this, SmsReceiver.class);
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
                AlertDialog.Builder alert = new AlertDialog.Builder(PatientMainMenu.this, R.style.PositiveButtonStyle11);
                alert.setCancelable(false);
                alert.setTitle("Alert!!");

                alert.setMessage("Are You Sure You Want To Logout ??");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        finish();
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
}
