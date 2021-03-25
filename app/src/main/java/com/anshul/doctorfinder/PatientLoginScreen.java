package com.anshul.doctorfinder;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

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

public class PatientLoginScreen extends AppCompatActivity {
    Button login, back;
    TextInputEditText username, password1;
    TextView attempts;
    TextView create;
    LinearLayout l2;
    String email, password;
    TextView text3;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    String LoginPreference;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_login_screen);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        username = (TextInputEditText) findViewById(R.id.username);
        password1 = (TextInputEditText) findViewById(R.id.password);
        text3 = (TextView) findViewById(R.id.text3);
        create=(TextView)findViewById(R.id.createaccountdoctor);
        login = (Button) findViewById(R.id.login);
        attempts = (TextView) findViewById(R.id.attempts);
        back = (Button) findViewById(R.id.back);
        l2 = (LinearLayout) findViewById(R.id.l2);

        String styledText = "<u><font color='blue'>Login Menu</font></u>";
        text3.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE);
        String styledText1 = "<u><b><font color='red'>No Account Yet? Create One</font></b></u>";
        create.setText(Html.fromHtml(styledText1), TextView.BufferType.SPANNABLE);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientLoginScreen.this, AddNewPatient.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });
        l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(l2.getApplicationWindowToken(), 0);
            }
        });
//        image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (password1.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
//                    password1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//                } else {
//                    password1.setTransformationMethod(PasswordTransformationMethod.getInstance());
//                }
//            }
//
//        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.enter,R.anim.leave);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=username.getText().toString();
                password=password1.getText().toString();

                if(email.equals("")){
                    username.setError("Please Enter Username");
                    username.requestFocus();
                    return;
                }
                if(password.equals("")){
                    password1.setError("Please Enter Password");
                    password1.requestFocus();
                    return;
                }
//                Intent intent=new Intent(PatientLoginScreen.this,PatientMainMenu.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.right_in,R.anim.left_out);

                new AsyncLogin().execute(email,password);
            }
        });

    }
    public class AsyncLogin extends AsyncTask<String, String, String> {
        private ProgressDialog dialog = new ProgressDialog(PatientLoginScreen.this);

        HttpURLConnection conn;
        URL url = null;

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog.setMessage("Loading Please wait");
            this.dialog.show();
            this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.YELLOW));
            this.dialog.setCancelable(false);
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

            //this method will be running on UI thread

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            if (result.equalsIgnoreCase("true")) {
                /* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */
//                LoginPreference = "LoggedIn";
//                SharedPreferences.Editor editor = sharedpreferences.edit();
//                editor.putString("LoginSession", LoginPreference);
//                editor.putString("Username", email);
//                editor.putString("Password", password);
//                editor.commit();
//                LoginPreference = "LoggedIn";
//                SharedPreferences.Editor editor = sharedpreferences.edit();
//                editor.putString("LoginSession", LoginPreference);
//                editor.putString("Username", email);
//                editor.putString("Password", password);
//                editor.commit();
                Toast toast = Toast.makeText(getApplicationContext(), "Login Successfully Done", Toast.LENGTH_LONG);
                View view = toast.getView();

//Gets the actual oval background of the Toast then sets the colour filter
                view.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);

//Gets the TextView from the Toast so it can be editted
                TextView text = view.findViewById(android.R.id.message);
                text.setTextColor(Color.WHITE);

                toast.show();
//                Intent intent=new Intent(PatientLoginScreen.this,DoctorMainMenu.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.right_in,R.anim.left_out);
              //  PatientLoginScreen.this.finish();
            } else if (result.equalsIgnoreCase("false")) {

                // If username and password does not match display a error message
                Toast.makeText(PatientLoginScreen.this, "Invalid email or password", Toast.LENGTH_LONG).show();

            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(PatientLoginScreen.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

            }
        }
    }

    @Override
    public void onBackPressed() {

    }
}
