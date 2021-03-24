package com.anshul.doctorfinder;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, AdapterView.OnItemSelectedListener {
    TextView locationEt;
    String s1;
    EditText location;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    public static final int MAINMENU_CODE = 200;
    private static final int NETWORK_PERMISSION_CODE = 100;
    String locationSTR = "";
    String locationmanualSTR = "";
    ArrayList<String> doctor_type = new ArrayList<String>();
    private Location mylocation;
    private GoogleApiClient googleApiClient;
    private final static int REQUEST_CHECK_SETTINGS_GPS = 0x1;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS = 0x2;
    Geocoder geocoder;
    List<Address> addresses;
    Button register;
    Button patientlogin, doctorlogin, myaccountpatient, mybookingspatient;
    Spinner doctorSpinner;
    ArrayAdapter<String> langAdapter;
    Button search;
    //    BottomNavigationView navigation;
//    FloatingActionButton addbutton;
    RelativeLayout relativeLayout;
    Toolbar toolbar;
    String patientSTR, doctorSTR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      //  Toast.makeText(getApplicationContext(),"New",Toast.LENGTH_LONG).show();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ConnectivityManager connec = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {


            checkPermission(
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    NETWORK_PERMISSION_CODE);


        } else {

            Toast toast = Toast.makeText(getApplicationContext(), "Not Connected To Internet!!!Please Enable Internet ", Toast.LENGTH_LONG);
            View view = toast.getView();

//Gets the actual oval background of the Toast then sets the colour filter
            view.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);

//Gets the TextView from the Toast so it can be editted
            TextView text = view.findViewById(android.R.id.message);
            text.setTextColor(Color.BLACK);

            toast.show();
        }
        Intent intent = getIntent();
        final String previousActivityId = intent.getStringExtra("value");
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        s1 = sharedpreferences.getString("LoginSession", "");
        // Toast.makeText(getApplicationContext(),"Value:-"+previousActivityId,Toast.LENGTH_LONG).show();
        search = (Button) findViewById(R.id.loginButton);
        locationEt = (TextView) findViewById(R.id.location);
        location = (EditText) findViewById(R.id.manuallylocation);
        register = (Button) findViewById(R.id.registerButton);
        patientlogin = (Button) findViewById(R.id.loginPatient);
        doctorlogin = (Button) findViewById(R.id.loginDoctor);
        myaccountpatient = (Button) findViewById(R.id.myaccount);
        mybookingspatient = (Button) findViewById(R.id.mybookings);
        doctorSpinner = (Spinner) findViewById(R.id.doctorSpinner);
//        addbutton = (FloatingActionButton) findViewById(R.id.floatingButton);
//        navigation = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        relativeLayout = (RelativeLayout) findViewById(R.id.rel1);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Find Your Doctor");
        toolbar.setTitleTextColor(Color.WHITE);
        setTitleColor(Color.WHITE);

        if (s1.equals("LoggedIn")) {
            patientlogin.setVisibility(View.GONE);
            doctorlogin.setVisibility(View.GONE);
            myaccountpatient.setVisibility(View.VISIBLE);
            mybookingspatient.setVisibility(View.VISIBLE);
        } else {
            patientlogin.setVisibility(View.VISIBLE);
            doctorlogin.setVisibility(View.VISIBLE);
            myaccountpatient.setVisibility(View.GONE);
            mybookingspatient.setVisibility(View.GONE);
        }

//        if(previousActivityId.equals("0")){
//             patientlogin.setText("My Bookings");
//            doctorlogin.setText("My Account");
//        }
//
//
//        if(previousActivityId.equals("1")){
//            patientlogin.setText("Login As Patient");
//            doctorlogin.setText("Login As Doctor");
//        }

        patientSTR = patientlogin.getText().toString();
        doctorSTR = doctorlogin.getText().toString();


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddNewDoctor.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });
        patientlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, PatientLoginScreen.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);

            }
        });
        doctorlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, DoctorLoginScreen.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);


            }
        });

//        // underline particular text in complete textview in android studio and give color to that text
//        String next = "<u><font color='#0000ff'>Register As Doctor</font></u>";
//        String next2 = "<u><font color='#0000ff'>Register As Clinic</font></u>";
//        String next3 = "<u><font color='#0000ff'>Register As Patient</font></u>";
//        doctorRegister.setText(Html.fromHtml(next));
//        ClinicRegister.setText(Html.fromHtml(next2));
//        patientRegister.setText(Html.fromHtml(next3));


        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(relativeLayout.getApplicationWindowToken(), 0);
            }
        });

//        addbutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final CharSequence[] items = {"Login As Patient", "Login As Doctor", "Login As Clinic", "Cancel"};
//                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.PositiveButtonStyle11);
//                builder.setTitle(Html.fromHtml("<b>" + "Select Option:" + "</b>"));
//                builder.setItems(items, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int item) {
//                        switch (item) {
//                            case 0:
//                                Intent intent = new Intent(MainActivity.this, PatientLoginScreen.class);
//                                startActivity(intent);
//                                overridePendingTransition(R.anim.right_in, R.anim.left_out);
//                                break;
//                            case 1:
//                                Intent intent11 = new Intent(MainActivity.this, DoctorLoginScreen.class);
//                                startActivity(intent11);
//                                overridePendingTransition(R.anim.right_in, R.anim.left_out);
//
//                                break;
//                            case 2:
//                                Intent intent2 = new Intent(MainActivity.this, ClinicLoginScreen.class);
//                                startActivity(intent2);
//                                overridePendingTransition(R.anim.right_in, R.anim.left_out);
//                                break;
//
//                            case 3:
//                                dialog.cancel();
//                                break;
//                        }
//
//                    }
//                });
//                final AlertDialog alert1 = builder.create();
//                ListView listView = alert1.getListView();
//                listView.setDivider(new ColorDrawable(Color.BLACK));
//                listView.setDividerHeight(8);
//                listView.setFooterDividersEnabled(false);
//                listView.addFooterView(new View(getApplicationContext()));
//                alert1.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
//                alert1.setCanceledOnTouchOutside(false);
//                alert1.show();
//            }
//        });

        doctor_type.add("Select Doctor Type");
        doctor_type.add("Skin Specialist");
        doctor_type.add("Hair Specialist");
        doctor_type.add("Medicine Specialist");
        doctor_type.add("Eyes Specialist");
        doctor_type.add("Throat Specialist");
        doctor_type.add("BP Specialist");
        doctor_type.add("Heart Specialist");
        doctor_type.add("Children Specialist");
        doctor_type.add("All");


        doctorSpinner.setOnItemSelectedListener(this);
        langAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_text, doctor_type);
        langAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        doctorSpinner.setAdapter(langAdapter);


        doctorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        geocoder = new Geocoder(this, Locale.getDefault());

        changeStatusBarColor();
        setTitle("");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        locationEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpGClient();
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationSTR = locationEt.getText().toString();
                locationmanualSTR = location.getText().toString();
                if ((locationEt.getText().toString().equals("")) && (location.getText().toString().equals(""))) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Please Select Current Location To Proceed", Toast.LENGTH_LONG);
                    View view = toast.getView();
                    view.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
                    TextView text = view.findViewById(android.R.id.message);
                    text.setTextColor(Color.BLACK);
                    toast.show();
                    return;
                }

                if (doctorSpinner.getSelectedItemPosition() == 0) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Please Choose Doctor Type", Toast.LENGTH_LONG);
                    View view = toast.getView();

//Gets the actual oval background of the Toast then sets the colour filter
                    view.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);

//Gets the TextView from the Toast so it can be editted
                    TextView text = view.findViewById(android.R.id.message);
                    text.setTextColor(Color.BLACK);

                    toast.show();
                    return;
                }


                Intent intent = new Intent(MainActivity.this, SearchDoctorList.class);
                intent.putExtra("Location", locationSTR);
                intent.putExtra("LocationManual", locationmanualSTR);
                intent.putExtra("DoctorType", doctorSpinner.getSelectedItem().toString());
                startActivityForResult(intent, MAINMENU_CODE);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);


            }
        });


    }


    private synchronized void setUpGClient() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, 0, this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            googleApiClient.connect();
        }
    }

    //    @Override
//    public void onStop() {
//        super.onStop();
//        if (googleApiClient != null && googleApiClient.isConnected()) {
//            googleApiClient.stopAutoManage(this);
//            googleApiClient.disconnect();
//        }
//    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, HelpActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }
    }
//    @Override
//    public void onPause() {
//        super.onPause();
//        googleApiClient.stopAutoManage(this);
//        googleApiClient.disconnect();
//    }

    @Override
    public void onLocationChanged(Location location) {
        mylocation = location;
        if (mylocation != null) {
            double latitude = mylocation.getLatitude();
            double longitude = mylocation.getLongitude();

            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                locationSTR = addresses.get(0).getLocality();
                locationEt.setText(locationSTR);


            } catch (IOException e) {
                e.printStackTrace();
            }

            //Or Do whatever you want with your location
        } else {
            Toast.makeText(getApplicationContext(), "No Location Found", Toast.LENGTH_LONG).show();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        checkPermissions();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void getMyLocation() {
        if (googleApiClient != null) {
            if (googleApiClient.isConnected()) {
                int permissionLocation = ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                    mylocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                    LocationRequest locationRequest = new LocationRequest();
                    locationRequest.setInterval(3000);
                    locationRequest.setFastestInterval(3000);
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                            .addLocationRequest(locationRequest);
                    builder.setAlwaysShow(true);
                    LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
                    PendingResult<LocationSettingsResult> result =
                            LocationServices.SettingsApi
                                    .checkLocationSettings(googleApiClient, builder.build());
                    result.setResultCallback(new ResultCallback<LocationSettingsResult>() {

                        @Override
                        public void onResult(LocationSettingsResult result) {
                            final Status status = result.getStatus();
                            switch (status.getStatusCode()) {
                                case LocationSettingsStatusCodes.SUCCESS:
                                    // All location settings are satisfied.
                                    // You can initialize location requests here.
                                    int permissionLocation = ContextCompat
                                            .checkSelfPermission(MainActivity.this,
                                                    Manifest.permission.ACCESS_FINE_LOCATION);
                                    if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                                        mylocation = LocationServices.FusedLocationApi
                                                .getLastLocation(googleApiClient);
                                    }
                                    break;
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    // Location settings are not satisfied.
                                    // But could be fixed by showing the user a dialog.
                                    try {
                                        // Show the dialog by calling startResolutionForResult(),
                                        // and check the result in onActivityResult().
                                        // Ask to turn on GPS automatically
                                        status.startResolutionForResult(MainActivity.this,
                                                REQUEST_CHECK_SETTINGS_GPS);
                                    } catch (IntentSender.SendIntentException e) {
                                        // Ignore the error.
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:

                                    break;
                            }
                        }
                    });
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS_GPS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        getMyLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        finish();
                        break;
                    case Activity.RESULT_FIRST_USER:
                        Settings();
                        break;
                }
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        int permissionLocation = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
            getMyLocation();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    private void checkPermissions() {
        int permissionLocation = ContextCompat.checkSelfPermission(MainActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(this,
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            }
        } else {
            getMyLocation();
        }

    }

    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission)
                == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{permission},
                    requestCode);
        }


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private void Settings() {
        locationEt.setText(locationSTR);
        locationEt.setFocusable(true);
        locationEt.setEnabled(true);
        locationEt.requestFocus();

    }

//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.exit:
//                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this, R.style.PositiveButtonStyle11);
//                    alert.setCancelable(false);
//                    alert.setTitle("Alert!!!");
//                    alert.setMessage("Are You Sure You Want To Exit ??");
//                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int whichButton) {
//                            moveTaskToBack(true);
//                            android.os.Process.killProcess(android.os.Process.myPid());
//                            System.exit(1);
//                        }
//                    });
//
//                    alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int whichButton) {
//                            dialog.cancel();
//                        }
//                    });
//                    AlertDialog alert1 = alert.create();
//                    alert1.setCanceledOnTouchOutside(false);
//                    alert1.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
//                    alert1.show();
//                    return true;
//                case R.id.home:
//                    Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
//                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(i);
//                    finish();
//                    overridePendingTransition(R.anim.enter, R.anim.leave);
//                    return true;
//
//                case R.id.settings:
//                    Intent dialogIntent = new Intent(android.provider.Settings.ACTION_SETTINGS);
//                    dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(dialogIntent);
//                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
//                    return true;
//            }
//            return false;
//        }
//    };

}
