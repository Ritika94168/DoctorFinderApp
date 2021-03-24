package com.anshul.doctorfinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddNewPatient extends AppCompatActivity {
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
        setContentView(R.layout.activity_add_new_patient);
       pname=(EditText)findViewById(R.id.pname);
       fname=(EditText)findViewById(R.id.fname);
        patientImage = (ImageView) findViewById(R.id.patientImage);
       contact=(EditText)findViewById(R.id.contact);
       whatsap=(EditText)findViewById(R.id.whatsapp);
       disease=(EditText)findViewById(R.id.disease);
       cardview=(CardView)findViewById(R.id.cardview);
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
       cardview.setBackgroundResource(R.drawable.cardview_background_color);
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

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectDate();
            }
        });
        patientImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               RadioButton radioButton = (RadioButton)findViewById(group.getCheckedRadioButtonId());
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

               if(!emailSTR.equals("")){
                   if(!emailSTR.matches(emailPattern)){
                       email.setError("Please Enter Valid Email-Address");
                       email.requestFocus();
                       return;
                   }
               }
//               genderSTR="";
//               if(male.isChecked()){
//                   genderSTR=male.getText().toString();
//               }
//               else if(female.isChecked()){
//                   genderSTR=female.getText().toString();
//               }
//               else{
//                   genderSTR=others.getText().toString();
//               }

               if(pnameSTR.equals("")){
                   pname.setError("Please Enter Patient Name");
                   pname.requestFocus();
                   return;
               }
//               if(radioButton==null){
//                   Toast toast = Toast.makeText(getApplicationContext(), "Please Select Gender Type", Toast.LENGTH_LONG);
//                   View view = toast.getView();
//
////Gets the actual oval background of the Toast then sets the colour filter
//                   view.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
//
////Gets the TextView from the Toast so it can be editted
//                   TextView text = view.findViewById(android.R.id.message);
//                   text.setTextColor(Color.BLACK);
//
//                   toast.show();
//               }
//               if(fnameSTR.equals("")){
//                   fname.setError("Please Enter Father's Name");
//                   fname.requestFocus();
//                   return;
//               }
               if(dobSTR.equals("")){
                   dob.setError("Please Select Date Of Birth");
                   dob.requestFocus();
                   return;
               }
               if(contactSTR.equals("")){
                   contact.setError("Please Enter Mobile Number");
                   contact.requestFocus();
                   return;
               }
               if(contact.length()!=10){
                   contact.setError("Please Enter Correct Mobile Number");
                   contact.requestFocus();
                   return;
               }
               if(whatsapSTR.equals("")){
                   whatsap.setError("Please Enter WhatsApp Number");
                   whatsap.requestFocus();
                   return;
               }

//               if(diseaseSTR.equals("")){
//                   disease.setError("Please Enter Disease Name");
//                   disease.requestFocus();
//                   return;
//               }

               if(addressSTR.equals("")){
                   address.setError("Please Enter Address");
                   address.requestFocus();
                   return;
               }
               if(usernameSTR.equals("")){
                   username.setError("Please Enter Username");
                   username.requestFocus();
                   return;
               }
               if(passwordSTR.equals("")){
                   password.setError("Please Create Password");
                   password.requestFocus();
                   return;
               }

               new AsyncLogin().execute(pnameSTR,dobSTR,contactSTR,whatsapSTR,emailSTR,addressSTR,usernameSTR,passwordSTR);

           }
       });


        setTitle("Patient Registration Module");


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
        Toast.makeText(getApplicationContext(), "Back Process Not Allowed Through This Key", Toast.LENGTH_LONG).show();
        return;
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

            year  = selectedYear;
            month = selectedMonth;
            day   = selectedDay;

            dob.setText(new StringBuilder().append(day)
                    .append("-").append(month + 1).append("-").append(year)
                    .append(" "));

        }
    };
    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AddNewPatient.this, R.style.PositiveButtonStyle11);
        builder.setTitle("Choose Your Photo !!!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 1);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, 2);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        ListView listView = alertDialog.getListView();
        listView.setDivider(new ColorDrawable(Color.BLACK));
        listView.setDividerHeight(4);
        listView.setFooterDividersEnabled(false);
        listView.addFooterView(new View(getApplicationContext()));
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 1) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            sizeImage = imageBitmap.getRowBytes() * imageBitmap.getHeight();
            fileSizeInKB = sizeImage / 1024;
            if (fileSizeInKB > 250) {
                Toast toast = Toast.makeText(getApplicationContext(), "Please Choose Image less than 250Kb", Toast.LENGTH_LONG);
                View view = toast.getView();
                view.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
                TextView text = view.findViewById(android.R.id.message);
                text.setTextColor(Color.BLACK);
                toast.show();
            } else {
                patientImage.setImageBitmap(imageBitmap);
            }
        }
        if (resultCode == RESULT_OK && requestCode == 2) {
            Uri selectedImageUri;
            selectedImageUri = data.getData();
            try {
                getImageSize(selectedImageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void getImageSize(Uri choosen) throws IOException {
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), choosen);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] imageInByte = stream.toByteArray();
        long lengthbmp = imageInByte.length;
        long lengthbmpkb = lengthbmp / 1024;
        if (lengthbmpkb > 250) {
            Toast toast = Toast.makeText(getApplicationContext(), "Please Choose Image less than 250Kb", Toast.LENGTH_LONG);
            View view = toast.getView();
            view.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
            TextView text = view.findViewById(android.R.id.message);
            text.setTextColor(Color.BLACK);
            toast.show();
        } else {
            patientImage.setImageURI(choosen);
        }


    }
    private class AsyncLogin extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(AddNewPatient.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("Loading...Please Wait");
            pdLoading.setCancelable(false);
            pdLoading.show();
            pdLoading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.YELLOW));
            pdLoading.setIndeterminate(false);
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://doc.gsinfotec.in/loginphpfile.php?action=registerPatient");

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
                        .appendQueryParameter("pname", params[0])
                        .appendQueryParameter("dob", params[1])
                        .appendQueryParameter("contactnumber", params[2])
                        .appendQueryParameter("whatsappnumber", params[3])
                        .appendQueryParameter("email", params[4])
                        .appendQueryParameter("address", params[5])
                        .appendQueryParameter("username", params[6])
                        .appendQueryParameter("password", params[7]);



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

            }
            catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }

            try {

                int response_code = conn.getResponseCode();
                Log.d("code",response_code+"");
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

                    // Pass data to onPostExecute method
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

            pdLoading.dismiss();
            //     Toast.makeText(getApplicationContext(), "" + result, Toast.LENGTH_LONG).show();
            if (result.equalsIgnoreCase("true")) {
                /* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */
                Toast toast = Toast.makeText(getApplicationContext(), "Registration Done Successfully", Toast.LENGTH_LONG);
                View view = toast.getView();

//Gets the actual oval background of the Toast then sets the colour filter
                view.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);

//Gets the TextView from the Toast so it can be editted
                TextView text = view.findViewById(android.R.id.message);
                text.setTextColor(Color.WHITE);

                toast.show();
                finish();
                overridePendingTransition(R.anim.enter, R.anim.leave);

            } else if (result.equalsIgnoreCase("false")) {

                // If username and password does not match display a error message
                Toast.makeText(AddNewPatient.this, "Oops! Something went wrong.", Toast.LENGTH_LONG).show();

            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(AddNewPatient.this, "Oops! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

            }
        }

    }
}
