package com.anshul.doctorfinder;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class SmsReceiver extends AppCompatActivity implements OnItemClickListener {

    private static SmsReceiver inst;
    ArrayList<String> smsMessagesList = new ArrayList<String>();
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    public static final int REQUEST_CODE_ASK_PERMISSIONS = 1;
    ListView smsListView;
    ArrayAdapter arrayAdapter;
    private Context mContext;
    public static SmsReceiver instance() {
        return inst;
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_receiver);
        smspermission();
        smsListView = (ListView) findViewById(R.id.SMSList);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, smsMessagesList);
        smsListView.setAdapter(arrayAdapter);
        smsListView.setOnItemClickListener(this);
        setTitle("Inbox");

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        refreshSmsInbox();
    }

    public void updateList(final String smsMessage) {
        arrayAdapter.insert(smsMessage, 0);
        arrayAdapter.notifyDataSetChanged();
    }

    public void refreshSmsInbox() {
        ContentResolver contentResolver = getContentResolver();
        Cursor smsInboxCursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null);
        int indexBody = smsInboxCursor.getColumnIndex("body");
        int indexAddress = smsInboxCursor.getColumnIndex("address");
        if (indexBody < 0 || !smsInboxCursor.moveToFirst()) return;
        arrayAdapter.clear();
        do {
            String str = "SMS From: " + smsInboxCursor.getString(indexAddress) +
                    "\n" + smsInboxCursor.getString(indexBody) + "\n";
            arrayAdapter.add(str);
        } while (smsInboxCursor.moveToNext());

        if (ContextCompat.checkSelfPermission(SmsReceiver.this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(SmsReceiver.this,
                    Manifest.permission.SEND_SMS)) {

            } else {
                ActivityCompat.requestPermissions(SmsReceiver.this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }
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

    public void onItemClick(final AdapterView<?> parent, View view, final int pos, final long id) {
//        AlertDialog.Builder alert = new AlertDialog.Builder(SmsReceiver.this, R.style.PositiveButtonStyle111);
//        alert.setCancelable(false);
//        String titleText = "Alert!!";
//        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.GREEN);
//        SpannableStringBuilder ssBuilder = new SpannableStringBuilder(titleText);
//        ssBuilder.setSpan(
//                foregroundColorSpan,
//                0,
//                titleText.length(),
//                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
//        );
//        alert.setTitle(ssBuilder);
//        String titleText1 = "Are You Want To Delete";
//        ForegroundColorSpan foregroundColorSpan1 = new ForegroundColorSpan(Color.WHITE);
//        SpannableStringBuilder ssBuilder1 = new SpannableStringBuilder(titleText1);
//        ssBuilder1.setSpan(
//                foregroundColorSpan1,
//                0,
//                titleText1.length(),
//                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
//        );
//        alert.setMessage(ssBuilder1);
//
//        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int whichButton) {
////                if (deleteSMS()) {
////                    Toast.makeText(getApplicationContext(), "Your message is deleted.", Toast.LENGTH_LONG).show();
////                } else {
////                    Toast.makeText(getApplicationContext(), "Sorry we can't delete messages.", Toast.LENGTH_LONG).show();
////                }
//                deleteSms(""+smsMessagesList.remove(pos));
//                recreate();
//            }
//        });
//
//        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int whichButton) {
//                dialog.cancel();
//            }
//        });
//        AlertDialog alert1 = alert.create();
//        alert1.setCanceledOnTouchOutside(false);
//        alert1.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
//        alert1.show();


    }

    private boolean deleteSMS() {
        boolean isDeleted = false;
        try {
            getApplicationContext().getContentResolver().delete(Uri.parse("content://sms/"), null, null);

            isDeleted = true;
            recreate();
        } catch (Exception ex) {
            isDeleted = false;
        }
        return isDeleted;
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
    public boolean deleteSms(String smsId) {
        boolean isSmsDeleted = false;
        try {
            getApplicationContext().getContentResolver().delete(Uri.parse("content://sms/inbox/" + smsId), null, null);
            isSmsDeleted = true;

        } catch (Exception ex) {
            isSmsDeleted = false;
        }
        return isSmsDeleted;
    }
    public void smspermission() {
        if (ContextCompat.checkSelfPermission(getBaseContext(), "android.permission.READ_SMS") == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(SmsReceiver.this, new String[]{"android.permission.READ_SMS"}, REQUEST_CODE_ASK_PERMISSIONS);
        }
    }

}