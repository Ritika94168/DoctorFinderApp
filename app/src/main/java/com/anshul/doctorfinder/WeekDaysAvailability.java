package com.anshul.doctorfinder;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class WeekDaysAvailability extends AppCompatActivity {
    Button save;
    CheckBox su, mo, tu, we, th, fr, sat;
    EditText sus1, sus2, sue1, sue2, mos1, mos2, moe1, moe2, tus1, tus2, tue1, tue2, wes1, wes2, wee1, wee2, ths1, ths2, the1, the2, frs1, frs2, fre1, fre2, sats1, sats2, sate1, sate2;
    String doctorid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_days_availability);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Intent intent=getIntent();
        doctorid=intent.getStringExtra("doctorid");


        Toast toast = Toast.makeText(getApplicationContext(), "S1:-Starttime1 ,E1:-Endtime1 ,S2:-Startime2 ,E2:-Endtime2", Toast.LENGTH_LONG);
        View view = toast.getView();

//Gets the actual oval background of the Toast then sets the colour filter
        view.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);

//Gets the TextView from the Toast so it can be editted
        TextView text = view.findViewById(android.R.id.message);
        text.setTextColor(Color.WHITE);

        toast.show();
        save = (Button) findViewById(R.id.save);
        su = (CheckBox) findViewById(R.id.chsu);
        mo = (CheckBox) findViewById(R.id.chmo);
        tu = (CheckBox) findViewById(R.id.chtu);
        we = (CheckBox) findViewById(R.id.chwe);
        th = (CheckBox) findViewById(R.id.chth);
        fr = (CheckBox) findViewById(R.id.chfr);
        sat = (CheckBox) findViewById(R.id.chsat);
        sus1 = (EditText) findViewById(R.id.sus1);
        sus2 = (EditText) findViewById(R.id.sus2);
        sue1 = (EditText) findViewById(R.id.sue1);
        sue2 = (EditText) findViewById(R.id.sue2);
        mos1 = (EditText) findViewById(R.id.mos1);
        mos2 = (EditText) findViewById(R.id.mos2);
        moe1 = (EditText) findViewById(R.id.moe1);
        moe2 = (EditText) findViewById(R.id.moe2);
        tus1 = (EditText) findViewById(R.id.tus1);
        tus2 = (EditText) findViewById(R.id.tus2);
        tue1 = (EditText) findViewById(R.id.tue1);
        tue2 = (EditText) findViewById(R.id.tue2);
        wes1 = (EditText) findViewById(R.id.wes1);
        wes2 = (EditText) findViewById(R.id.wes2);
        wee1 = (EditText) findViewById(R.id.wee1);
        wee2 = (EditText) findViewById(R.id.wee2);
        ths1 = (EditText) findViewById(R.id.ths1);
        ths2 = (EditText) findViewById(R.id.ths2);
        the1 = (EditText) findViewById(R.id.the1);
        the2 = (EditText) findViewById(R.id.the2);
        frs1 = (EditText) findViewById(R.id.frs1);
        frs2 = (EditText) findViewById(R.id.frs2);
        fre1 = (EditText) findViewById(R.id.fre1);
        fre2 = (EditText) findViewById(R.id.fre2);
        sats1 = (EditText) findViewById(R.id.sats1);
        sats2 = (EditText) findViewById(R.id.sats2);
        sate1 = (EditText) findViewById(R.id.sate1);
        sate2 = (EditText) findViewById(R.id.sate2);
        setTitle("Leaves Menu");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if(!su.isChecked()&& !mo.isChecked()&&!tu.isChecked()&&!we.isChecked()&&!th.isChecked()&&!fr.isChecked()&&!sat.isChecked()){
                   Toast toast = Toast.makeText(getApplicationContext(), "Please Save Your Availability Days", Toast.LENGTH_LONG);
                   View view = toast.getView();

//Gets the actual oval background of the Toast then sets the colour filter
                   view.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);

//Gets the TextView from the Toast so it can be editted
                   TextView text = view.findViewById(android.R.id.message);
                   text.setTextColor(Color.WHITE);

                   toast.show();
               }
               if(su.isChecked()){
                   if(sus1.getText().toString().equals("")){
                       sus1.setError("Please Enter Start Time");
                       sus1.requestFocus();
                       return;
                   }
                   if(sue1.getText().toString().equals("")){
                       sue1.setError("Please Enter End Time");
                       sue1.requestFocus();
                       return;
                   }
               }
                if(mo.isChecked()){
                    if(mos1.getText().toString().equals("")){
                        mos1.setError("Please Enter Start Time");
                        mos1.requestFocus();
                        return;
                    }
                    if(moe1.getText().toString().equals("")){
                        moe1.setError("Please Enter End Time");
                        moe1.requestFocus();
                        return;
                    }
                }
                if(tu.isChecked()){
                    if(tus1.getText().toString().equals("")){
                        tus1.setError("Please Enter Start Time");
                        tus1.requestFocus();
                        return;
                    }
                    if(tue1.getText().toString().equals("")){
                        tue1.setError("Please Enter End Time");
                      tue1.requestFocus();
                        return;
                    }
                }
                if(we.isChecked()){
                    if(wes1.getText().toString().equals("")){
                        wes1.setError("Please Enter Start Time");
                        wes1.requestFocus();
                        return;
                    }
                    if(wee1.getText().toString().equals("")){
                        wee1.setError("Please Enter End Time");
                        wee1.requestFocus();
                        return;
                    }
                }
                if(th.isChecked()){
                    if(ths1.getText().toString().equals("")){
                        ths1.setError("Please Enter Start Time");
                        ths1.requestFocus();
                        return;
                    }
                    if(the1.getText().toString().equals("")){
                        the1.setError("Please Enter End Time");
                        the1.requestFocus();
                        return;
                    }
                }
                if(fr.isChecked()){
                    if(frs1.getText().toString().equals("")){
                        frs1.setError("Please Enter Start Time");
                        frs1.requestFocus();
                        return;
                    }
                    if(fre1.getText().toString().equals("")){
                        fre1.setError("Please Enter End Time");
                        fre1.requestFocus();
                        return;
                    }
                }
                if(sat.isChecked()){
                    if(sats1.getText().toString().equals("")){
                        sats1.setError("Please Enter Start Time");
                        sats1.requestFocus();
                        return;
                    }
                    if(sate1.getText().toString().equals("")){
                        sate1.setError("Please Enter End Time");
                        sate1.requestFocus();
                        return;
                    }
                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.weekdaysmenu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.enter, R.anim.leave);
                return true;
            case R.id.weekdays:
                AlertDialog.Builder alert = new AlertDialog.Builder(WeekDaysAvailability.this, R.style.PositiveButtonStyle11);
                alert.setCancelable(false);
                alert.setTitle("Welcome To Help Section !!!");
                String titleText12 ="This is WeekDays Leaves Section which you can fill once and edit later on so that patients can see your availability when patient can book your appointment. In this You have to enable checkbox for week days for which you are present,otherwise you will be marked as absent on these days.So after enable as per suitable,you have to enter shift timings for which you are present.In this 2 shifts are available,in which if you have to available at only one shift either on morning or everning,you have to fill first shift timings,if you have available on both shifts,you have to enter timings of both shifts......If you have some query after reading this,(Please contact :- 7988327513) or (Email directly to :- anshulgupta2498@gmail.com)";
                ForegroundColorSpan foregroundColorSpan12 = new ForegroundColorSpan(Color.BLACK);
                SpannableStringBuilder ssBuilder12 = new SpannableStringBuilder(titleText12);
                ssBuilder12.setSpan(
                        foregroundColorSpan12,
                        0,
                        titleText12.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                );
                alert.setMessage(ssBuilder12);
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
}
