package com.anshul.doctorfinder;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
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

public class ChooseSubscription extends AppCompatActivity {
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    Button subscribe199,subscribe499,subscribe1099,subscribe1499;
    String doctorid,doctornameSTR, doccontactSTR,docspecializationpnameSTR;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_subscription);
        setTitle("");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Intent intent=getIntent();
        doctorid=intent.getStringExtra("doctorid");

        subscribe199=(Button)findViewById(R.id.subsrcibe199);
        subscribe499=(Button)findViewById(R.id.subscribe499);
        subscribe1099=(Button)findViewById(R.id.subscribe1099);
        subscribe1499=(Button)findViewById(R.id.subscribe1499);

        subscribe199.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(ChooseSubscription.this,PackagePayment.class);
                intent1.putExtra("doctorid",doctorid);
                intent1.putExtra("planname","3 Month Normal Plan");
                intent1.putExtra("planamount","199");
                startActivity(intent1);
                overridePendingTransition(R.anim.right_in,R.anim.left_out);

            }
        });
        subscribe499.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(ChooseSubscription.this,PackagePayment.class);
                intent1.putExtra("doctorid",doctorid);
                intent1.putExtra("planname","1 Year Normal Plan");
                intent1.putExtra("planamount","499");
                startActivity(intent1);
                overridePendingTransition(R.anim.right_in,R.anim.left_out);
            }
        });
        subscribe1099.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(ChooseSubscription.this,PackagePayment.class);
                intent1.putExtra("doctorid",doctorid);
                intent1.putExtra("planname","6 months Premium Plan");
                intent1.putExtra("planamount","1099");
                startActivity(intent1);
                overridePendingTransition(R.anim.right_in,R.anim.left_out);
            }
        });
        subscribe1499.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(ChooseSubscription.this,PackagePayment.class);
                intent1.putExtra("doctorid",doctorid);
                intent1.putExtra("planname","1 year Premium Plan");
                intent1.putExtra("planamount","1499");
                startActivity(intent1);
                overridePendingTransition(R.anim.right_in,R.anim.left_out);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.subscription, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                    finish();
                    overridePendingTransition(R.anim.enter, R.anim.leave);

                return true;

            case R.id.subscription:
                AlertDialog.Builder alert = new AlertDialog.Builder(ChooseSubscription.this, R.style.PositiveButtonStyle11);
                alert.setCancelable(false);
                alert.setTitle("Subscription Benefits !!!");
                alert.setMessage("Subscription feature is very useful thing for becoming popular through this App.It is especially designed for Doctor's ,through this feature your profile can be seen to more and more patients up in the list ,according to the subscription pack.Here are some packs defined through which profile of various doctors can be saw to patients.So I suggest every doctor to choose some subscription pack,so that you get more patients and you earn more money.Every Subscription Pack is valid for different time plan,after that you can renew your subscription pack.Don't worry,you will get notification 2 days ago,when your plan expires.If You face an issue,Please freely Contact on 7988327513 or email directly on anshulgupta2498@gmail.com");

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
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
        return super.onOptionsItemSelected(item);
    }


}
