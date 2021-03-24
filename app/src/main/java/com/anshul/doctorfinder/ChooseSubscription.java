package com.anshul.doctorfinder;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

public class ChooseSubscription extends AppCompatActivity {
    private long mBackPressed;
    private static final int TIME_INTERVAL = 2000;

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


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.subscription, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
                    finish();
                    overridePendingTransition(R.anim.enter, R.anim.leave);
                } else {
                    Toast.makeText(getBaseContext(), "Double tap Back Button to back", Toast.LENGTH_SHORT).show();
                }
                mBackPressed = System.currentTimeMillis();

                return true;

            case R.id.subscription:
                AlertDialog.Builder alert = new AlertDialog.Builder(ChooseSubscription.this, R.style.PositiveButtonStyle11);
                alert.setCancelable(false);
                alert.setTitle("Subscription Benefits !!!");
                alert.setMessage("Subscription feature is very useful thing for becoming popular through this App.It is especially designed for Doctor's ,through this feature your profile can be seen to more and more patients ,according to the subscription pack.Here are some packs defined through which profile of various doctors can be saw to patients.So I suggest every doctor to choose some subscription pack,so that you get more patients and you earn more money.Every Subscription Pack is valid for only 1 year,after that you can renew your subscription pack.Don't worry,you will get notification 2 days ago,when your plan expires. ");

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
