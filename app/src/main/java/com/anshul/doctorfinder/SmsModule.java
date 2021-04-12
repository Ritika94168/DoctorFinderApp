package com.anshul.doctorfinder;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SmsModule extends AppCompatActivity {
    EditText number, textmessage;
    Button sendsms;
    SharedPreferences permissionStatus;
    boolean sentToSettings = false;
    LinearLayout l2;
    String mobileSTR, textmessageSTR;
    private static final int SMS_PERMISSION_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    String mobilenumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_module);

        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);
        Intent intent=getIntent();
        mobilenumber=intent.getStringExtra("mobilenumber");

        number = (EditText) findViewById(R.id.mobilenumber);
        textmessage = (EditText) findViewById(R.id.message);
        sendsms = (Button) findViewById(R.id.sendsms);


        l2 = (LinearLayout) findViewById(R.id.l2);
        l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(l2.getApplicationWindowToken(), 0);
            }
        });
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setTitle("SMS Module");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (ActivityCompat.checkSelfPermission(SmsModule.this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(SmsModule.this, Manifest.permission.SEND_SMS)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SmsModule.this, R.style.PositiveButtonStyle11);
                builder.setTitle("Need SMS Permission");
                builder.setMessage("This app needs SMS permission to send Messages.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(SmsModule.this,
                                new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(Manifest.permission.SEND_SMS, false)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SmsModule.this);
                builder.setTitle("Need SMS Permission");
                builder.setMessage("This app needs SMS permission to send Messages.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(getBaseContext(),
                                "Go to Permissions to Grant SMS permissions", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                ActivityCompat.requestPermissions(SmsModule.this, new String[]{Manifest.permission.SEND_SMS}
                        , SMS_PERMISSION_CONSTANT);
            }

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(Manifest.permission.SEND_SMS, true);
            editor.commit();

        }
        if(!mobilenumber.equals("")){
            number.setText(mobilenumber);
            number.setEnabled(false);
        }
        sendsms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobileSTR = number.getText().toString();
                textmessageSTR = textmessage.getText().toString();

                if (mobileSTR.equals("")) {
                    number.setError("Enter Mobile No");
                    number.requestFocus();
                    return;
                }
                if(mobileSTR.length()>10 || mobileSTR.length()<10){
                    number.setError("Enter Valid Mobile No");
                    number.requestFocus();
                    return;
                }
                if(textmessageSTR.equals("")){
                    textmessage.setError("Enter Message Here");
                    textmessage.requestFocus();
                    return;
                }
                try{
                    SmsManager smgr = SmsManager.getDefault();
                    smgr.sendTextMessage(mobileSTR,null,textmessageSTR,null,null);
                    Toast.makeText(SmsModule.this, "SMS Sent Successfully", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    Toast.makeText(SmsModule.this, "Oops!SMS Failed, Please try again later", Toast.LENGTH_SHORT).show();
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
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.enter, R.anim.leave);
    }

}
