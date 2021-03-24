package com.anshul.doctorfinder;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class ShowDoctor extends AppCompatActivity {
    MyListAdapter adapter;
    ArrayList<DisplayList> displayList = new ArrayList<DisplayList>();
    ListView listview;
    private long mBackPressed;
    private static final int TIME_INTERVAL = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_doctor);
        listview = (ListView) findViewById(R.id.list);
        ConnectivityManager connec = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

            setTitle("Clinic Doctor's List");
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            adapter = new MyListAdapter(ShowDoctor.this, displayList);
            listview.setAdapter(adapter);

            // Add List Manually For Testing,,,because list is loaded from mysql database after Database Creation
//            displayList.add(new DisplayList("" + R.drawable.hospital, "Anshul Gupta", "Eyes Specialist", "I have 2 Year Experience", "20/21 Industrial Area,Ambala Cantt", "Rating:-4"));
//            displayList.add(new DisplayList("" + R.drawable.hospital, "Ritika Gaba", "Heart Specialist", "I have 4 Year Experience At Rotary Hospital Ambala Cantt", "20/21 Industrial Area,Ambala Cantt", "Rating:-5"));
//            displayList.add(new DisplayList("" + R.drawable.hospital, "Bunty Bindra", "Throat Specialist", "I have 5 Year Experience", "20/21 Industrial Area,Ambala Cantt", "Rating:-6"));
//            displayList.add(new DisplayList("" + R.drawable.hospital, "Munish Gautum", "Ear Specialist", "I have 1 Year Experience", "20/21 Industrial Area,Ambala Cantt", "Rating:-8"));
//            adapter = new MyListAdapter(ShowDoctor.this, displayList);
//            listview.setAdapter(adapter);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position,
                                        final long id) {
                    Intent intent = new Intent(ShowDoctor.this, CompleteDoctorProfile.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }
            });

        } else {

            Toast toast = Toast.makeText(getApplicationContext(), "Not Connected To Internet!!!Please Enable Internet And Continue", Toast.LENGTH_LONG);
            View view = toast.getView();
            view.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
            TextView text = view.findViewById(android.R.id.message);
            text.setTextColor(Color.BLACK);
            toast.show();

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
