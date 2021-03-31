package com.anshul.doctorfinder;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_days_availability);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

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
        setTitle("Doctor's Availability");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
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
