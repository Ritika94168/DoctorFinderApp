package com.anshul.doctorfinder;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

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

public class WelcomeActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button btnSkip, btnNext;
    private static final int NETWORK_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;
    private static final int WIFI_PERMISSION_CODE = 102;
    private static final int READ_PERMISSION_CODE = 103;
    float width;
    SharedPreferences sharedpreferences;
    //  SharedPreferences sharedpreferences1;
    // public static final String mypreference1= "mypref1";
    public static final String mypreference = "mypref";
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    String s1, s2, s3, s4;
    String ps1, ps2, ps3, ps4;
    private PrefManager prefManager;
    Boolean resultSTR = false;
    Boolean resultSTR1 = false;
    TextView textview1;
    private int no_of_dots = 2;

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefManager = new PrefManager(this);


        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        setContentView(R.layout.activity_welcome);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ConnectivityManager connec = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

            checkPermission(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    STORAGE_PERMISSION_CODE);
            checkPermission(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    READ_PERMISSION_CODE);
            checkPermission(
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    NETWORK_PERMISSION_CODE);
            checkPermission(
                    Manifest.permission.CHANGE_WIFI_STATE,
                    WIFI_PERMISSION_CODE);


        } else {


            AlertDialog.Builder builder = new AlertDialog.Builder(WelcomeActivity.this, R.style.PositiveButtonStyle11);
            String titleText = "Alert!";
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.RED);
            SpannableStringBuilder ssBuilder = new SpannableStringBuilder(titleText);
            ssBuilder.setSpan(
                    foregroundColorSpan,
                    0,
                    titleText.length(),

                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            );
            builder.setTitle(ssBuilder);
            String titletext1 = "Your Internet Connection Is Unavailable,Please Enable Internet And Restart Your App.";
            ForegroundColorSpan foregroundColorSpan1 = new ForegroundColorSpan(Color.BLACK);
            SpannableStringBuilder ssBuilder1 = new SpannableStringBuilder(titletext1);
            ssBuilder1.setSpan(
                    foregroundColorSpan1,
                    0,
                    titletext1.length(),

                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            );
            builder.setMessage(ssBuilder1);
            builder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            moveTaskToBack(true);
                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(1);


                        }
                    });
            builder.setCancelable(false);
            builder.show();
        }
        checkPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                STORAGE_PERMISSION_CODE);
        checkPermission(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                READ_PERMISSION_CODE);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        btnSkip = (Button) findViewById(R.id.btn_skip);
        btnNext = (Button) findViewById(R.id.btn_next);


        // layouts of welcome sliders
        layouts = new int[]{
                R.layout.welcome_slide1,
                R.layout.welcome_slide2};
//
//        StartAnimations();
        // adding bottom dots
        addBottomDots(0);

        // making notification bar transparent
        changeStatusBarColor();

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        //  sharedpreferences1 = getSharedPreferences(mypreference1, Context.MODE_PRIVATE);


        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        s1 = sharedpreferences.getString("LoginSession", "");
        s2 = sharedpreferences.getString("Username", "");
        s3 = sharedpreferences.getString("Password", "");
        s4 = sharedpreferences.getString("docid", "");
        ps1 = sharedpreferences.getString("LoginSession1", "");
        ps2 = sharedpreferences.getString("Username1", "");
        ps3 = sharedpreferences.getString("Password1", "");
        ps4 = sharedpreferences.getString("pid", "");
      //  Toast.makeText(getApplicationContext(),ps4,Toast.LENGTH_LONG).show();


//
//        sharedpreferences1 = getSharedPreferences(mypreference1, Context.MODE_PRIVATE);
//        ps1 = sharedpreferences.getString("LoginSession", "");
//        ps2 = sharedpreferences.getString("Username", "");
//        ps3 = sharedpreferences.getString("Password", "");
//        new AsyncLogin().execute(ps2, ps3);
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (s1.equals("LoggedInDoctor")) {
                       // Toast.makeText(getApplicationContext(),"logged",Toast.LENGTH_LONG).show();
                        new AsyncLogin().execute(s2, s3);


                    }
                    if (ps1.equals("LoggedInPatient")) {
                        new AsyncLogin1().execute(ps2, ps3);

                    }
                    if((!s1.equals("LoggedInDoctor")&&(!ps1.equals("LoggedInPatient"))))
                    {
                        launchHomeScreen();
                        //   overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page if true launch MainActivity

                int current = getItem(+1);

                if (current < layouts.length) {
                    // move to next screen

                    viewPager.setCurrentItem(current);
                } else {

                    try {
                        if (s1.equals("LoggedInDoctor"))
                        {
                            new AsyncLogin().execute(s2, s3);
                        }
                        if (ps1.equals("LoggedInPatient"))
                        {
                            new AsyncLogin1().execute(ps2, ps3);

                        }
                        if((!s1.equals("LoggedInDoctor")&&(!ps1.equals("LoggedInPatient"))))
                        {
                            launchHomeScreen();
                            //   overridePendingTransition(R.anim.right_in, R.anim.left_out);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dots[i].setAlpha(0);
//            dots[i].setTextColor(Color.BLACK);
//            dots[i].setBackground((i!=currentPage)?
//                    getResources().getDrawable(R.drawable.transparent_circle):getResources().getDrawable(R.drawable.active_dot_slider));

            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[currentPage].setTextColor(colorsActive[currentPage]);
        }
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        Intent intent = new Intent(WelcomeActivity.this, FirstActivity.class);
        intent.putExtra("value", "1");
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
                btnNext.setText(getString(R.string.start));
                btnSkip.setVisibility(View.GONE);
            } else {
                // still pages are left
                btnNext.setText(getString(R.string.next));
                btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    // Making notification bar transparent

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }
    }

    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(WelcomeActivity.this, permission)
                == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(WelcomeActivity.this,
                    new String[]{permission},
                    requestCode);
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
        overridePendingTransition(R.anim.enter, R.anim.leave);
    }

    public class AsyncLogin1 extends AsyncTask<String, String, String> {
        private ProgressDialog dialog = new ProgressDialog(WelcomeActivity.this);

        HttpURLConnection conn;
        URL url = null;

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                url = new URL("http://doc.gsinfotec.in/loginphpfile.php?action=loginPatient");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("username", params[0])
                        .appendQueryParameter("password", params[1]);
                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {

            if (!result.equals("")) {

                resultSTR1 = true;

            }
            if (resultSTR1) {

                Intent intent = new Intent(WelcomeActivity.this, PatientMainMenu.class);
                intent.putExtra("pid", ps4);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        }
    }

    public class AsyncLogin extends AsyncTask<String, String, String> {
        private ProgressDialog dialog = new ProgressDialog(WelcomeActivity.this);

        HttpURLConnection conn;
        URL url = null;

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                url = new URL("http://doc.gsinfotec.in/loginphpfile.php?action=loginDoctor");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("username", params[0])
                        .appendQueryParameter("password", params[1]);
                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {
            if (!result.equals("")) {
         //       Toast.makeText(getApplicationContext(),"true",Toast.LENGTH_LONG).show();

                resultSTR = true;
            }
         //   Toast.makeText(getApplicationContext(),"postexecute",Toast.LENGTH_LONG).show();
            if(resultSTR){
                Intent intent = new Intent(WelcomeActivity.this, DoctorMainMenu.class);
                intent.putExtra("doctorid", s4);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        }

    }
}