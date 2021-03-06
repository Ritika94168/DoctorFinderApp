package com.anshul.doctorfinder;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
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
import java.util.Calendar;
import java.util.HashMap;

public class PatientPersonalDetails extends AppCompatActivity {
String pid;
    EditText pname, fname, contact, whatsap, disease, email, address,username,password;
    RadioGroup group;
    RadioButton male,female,others;
    Button register;
    CardView cardview;
    EditText dob;
    String pnameSTR,fnameSTR,contactSTR,whatsapSTR,diseaseSTR,emailSTR,addressSTR,usernameSTR,passwordSTR;
    String genderSTR;
    ImageView pass;
    public static final int DATE_PICKER_ID=1;
    HashMap<String, String> hashMap = new HashMap<>();
    String HttpURL = "http://doc.gsinfotec.in/loginphpfile.php?action=patientupdatedetails";
    String finalResult;
    HttpParse httpParse = new HttpParse();
    int day,month,year;
    String dobSTR;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ImageView patientImage;
    int sizeImage;
    long fileSizeInKB;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_personal_details);
        Intent intent = getIntent();
        pid = intent.getStringExtra("pid");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setTitle("Personal Details");
        pname=(EditText)findViewById(R.id.pname);
        fname=(EditText)findViewById(R.id.fname);
        patientImage = (ImageView) findViewById(R.id.patientImage);
        contact=(EditText)findViewById(R.id.contact);
        whatsap=(EditText)findViewById(R.id.whatsapp);
        disease=(EditText)findViewById(R.id.disease);
     //   cardview=(CardView)findViewById(R.id.cardview);
        email=(EditText)findViewById(R.id.email);
        address=(EditText)findViewById(R.id.address);
        username=(EditText)findViewById(R.id.username);
        password=(EditText)findViewById(R.id.password);
        dob=(EditText)findViewById(R.id.dob);
        group=(RadioGroup)findViewById(R.id.radioSex);
        male=(RadioButton)findViewById(R.id.male);
        pass=(ImageView)findViewById(R.id.image);
        female=(RadioButton)findViewById(R.id.female);
        others=(RadioButton)findViewById(R.id.others);
        register=(Button)findViewById(R.id.register);
      //  cardview.setBackgroundResource(R.drawable.cardview_background_color);
        pass.setClickable(true);
        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        register.setVisibility(View.INVISIBLE);
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectDate();
            }
        });
      //  RadioButton radioButton = (RadioButton)findViewById(group.getCheckedRadioButtonId());
        pnameSTR=pname.getText().toString();
        fnameSTR=fname.getText().toString();
        contactSTR=contact.getText().toString();
        whatsapSTR=whatsap.getText().toString();
        diseaseSTR=disease.getText().toString();
        emailSTR=email.getText().toString().trim();
        dobSTR=dob.getText().toString();
        addressSTR=address.getText().toString();
        usernameSTR=username.getText().toString();
        passwordSTR=password.getText().toString();


        pname.setEnabled(false);
        dob.setEnabled(false);
        contact.setEnabled(false);
        whatsap.setEnabled(false);
        email.setEnabled(false);
        address.setEnabled(false);
        username.setEnabled(false);
        password.setEnabled(false);

        new AsyncLogin().execute(pid);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // RadioButton radioButton = (RadioButton) findViewById(group.getCheckedRadioButtonId());
                pnameSTR = pname.getText().toString();
                contactSTR = contact.getText().toString();
                whatsapSTR = whatsap.getText().toString();
                emailSTR = email.getText().toString().trim();
                addressSTR = address.getText().toString();
                usernameSTR = username.getText().toString();
                passwordSTR = password.getText().toString();
                dobSTR = dob.getText().toString();

//                genderSTR = "";
//                if (male.isChecked()) {
//                    genderSTR = male.getText().toString();
//                } else if (female.isChecked()) {
//                    genderSTR = female.getText().toString();
//                } else {
//                    genderSTR = others.getText().toString();
//                }

                if (pnameSTR.equals("")) {
                    pname.setError("Please Enter Patient's Name");
                    pname.requestFocus();
                    return;
                }




                if (contactSTR.equals("")) {
                    contact.setError("Please Enter Mobile Number");
                    contact.requestFocus();
                    return;
                }

                if (contact.length() != 10) {
                    contact.setError("Please Enter Correct Mobile Number");
                    contact.requestFocus();
                    return;
                }

                if (whatsapSTR.equals("")) {
                    whatsap.setError("Please Enter WhatsApp Number");
                    whatsap.requestFocus();
                    return;
                }

                if (emailSTR.equals("")) {
                    email.setError("Please Enter Email-Address");
                    email.requestFocus();
                    return;
                }

                if (!emailSTR.matches(emailPattern)) {
                    email.setError("Please Enter Valid Email-Address");
                    email.requestFocus();
                    return;
                }


                if (addressSTR.equals("")) {
                    address.setError("Please Enter Address");
                    address.requestFocus();
                    return;
                }

                if (dobSTR.equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Please Upload Your Image", Toast.LENGTH_LONG);
                    View view = toast.getView();
                    view.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
                    TextView text = view.findViewById(android.R.id.message);
                    text.setTextColor(Color.BLACK);
                    toast.show();
                    return;
                }
                if(passwordSTR.equals("")){
                    password.setError("Please Enter Password");
                    password.requestFocus();
                    return;
                }
                if (usernameSTR.equals("")) {
                    username.setError("Please Enter Username");
                    username.requestFocus();
                    return;
                }

                PatientRecordUpdate( pnameSTR,  contactSTR, whatsapSTR,dobSTR, emailSTR, addressSTR,  usernameSTR, passwordSTR,pid);
                recreate();
            }
        });



    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.enter, R.anim.leave);
                return true;
            case R.id.doctor_edit:

                register.setVisibility(View.VISIBLE);
                pname.setEnabled(true);
                contact.setEnabled(true);
                whatsap.setEnabled(true);
                email.setEnabled(true);
                address.setEnabled(true);
                dob.setEnabled(true);
                username.setEnabled(true);
                password.setEnabled(true);


                // doctorImage.setFocusableInTouchMode(true);
                pname.setFocusableInTouchMode(true);
                contact.setFocusableInTouchMode(true);
                whatsap.setFocusableInTouchMode(true);
                email.setFocusableInTouchMode(true);
                address.setFocusableInTouchMode(true);
                dob.setFocusableInTouchMode(true);
                username.setFocusableInTouchMode(true);
                password.setFocusableInTouchMode(true);



                //   doctorImage.setFocusable(true);
                pname.setFocusable(true);
                contact.setFocusable(true);
                whatsap.setFocusable(true);
                email.setFocusable(true);
                address.setFocusable(true);
                dob.setFocusable(true);
                username.setFocusable(true);
                password.setFocusable(true);


                pname.setTypeface(pname.getTypeface(), Typeface.BOLD);
                contact.setTypeface(contact.getTypeface(), Typeface.BOLD);
                whatsap.setTypeface(whatsap.getTypeface(), Typeface.BOLD);
                email.setTypeface(email.getTypeface(), Typeface.BOLD);
                address.setTypeface(address.getTypeface(), Typeface.BOLD);
                dob.setTypeface(dob.getTypeface(), Typeface.BOLD);
                username.setTypeface(username.getTypeface(), Typeface.BOLD);
                password.setTypeface(password.getTypeface(), Typeface.BOLD);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.monitoring_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.enter, R.anim.leave);
    }
    private void SelectDate() {

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);


        showDialog(DATE_PICKER_ID);

    }
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:

                return new DatePickerDialog(this, pickerListener, year, month,day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            view.setMaxDate(System.currentTimeMillis());
            year  = selectedYear;
            month = selectedMonth;
            day   = selectedDay;

            dob.setText(new StringBuilder().append(day)
                    .append("-").append(month + 1).append("-").append(year)
                    .append(" "));

        }
    };
    private class AsyncLogin extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(PatientPersonalDetails.this);
        HttpURLConnection conn;
        URL url = null;


        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            pdLoading.setMessage("Loading Please Wait");
            pdLoading.setCancelable(false);
            pdLoading.show();
            pdLoading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.YELLOW));
            pdLoading.setIndeterminate(false);
        }

        @Override
        protected String doInBackground(String... params) {
            try {

//                url = new URL("http://rotaryapp.mdimembrane.com/HMS_API/hospital_activity_status_api.php?action=showAll");
                url = new URL("http://doc.gsinfotec.in/loginphpfile.php?action=fetchCompletePatientPersonaldetails");
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
                        .appendQueryParameter("pid", params[0]);

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
                Log.d("dfcds", "Response Code:-" + response_code);
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

            pdLoading.dismiss();

            if (result != null) {
                //  Toast.makeText(getApplicationContext(), "Result:-" + result, Toast.LENGTH_LONG).show();
                JSONArray jsonArray;

                try {
                    jsonArray = new JSONArray(result);


                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        pnameSTR = jsonObject.getString("pname");
                        dobSTR=jsonObject.getString("dob");
                        addressSTR = jsonObject.getString("address");
                        contactSTR = jsonObject.getString("contactnumber");
                        whatsapSTR = jsonObject.getString("whatsappnumber");
                        emailSTR = jsonObject.getString("email");
                        usernameSTR = jsonObject.getString("username");
                        passwordSTR = jsonObject.getString("password");


                        pname.setText(pnameSTR);
                        address.setText(addressSTR);
                        contact.setText(contactSTR);
                        whatsap.setText(whatsapSTR);
                        email.setText(emailSTR);
                         dob.setText(dobSTR);
                        username.setText(usernameSTR);
                        password.setText(passwordSTR);
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            }
        }
    }
    public void PatientRecordUpdate(final String pname,  final String contact, final String whatsapp  , final String dob ,  final String email,final String address, final String username, final String password,final String patientid) {

        class StudentRecordUpdateClass extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {


                hashMap.put("pname", params[0]);
                hashMap.put("contactnumber", params[1]);
                hashMap.put("whatsappnumber", params[2]);
                hashMap.put("dob", params[3]);
                hashMap.put("email", params[4]);
                hashMap.put("address", params[5]);
                hashMap.put("username", params[6]);
                hashMap.put("password", params[7]);
                hashMap.put("pid", params[8]);
                // hashMap.put("docimage", params[14]);


                Log.d("update111", "" + hashMap);
                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                Log.d("ssddsddddd", "" + httpResponseMsg);
                super.onPostExecute(httpResponseMsg);


            }


        }

        StudentRecordUpdateClass studentRecordUpdateClass = new StudentRecordUpdateClass();

        studentRecordUpdateClass.execute(pname,  contact, whatsapp,dob, email,address, username, password,patientid);
    }

}