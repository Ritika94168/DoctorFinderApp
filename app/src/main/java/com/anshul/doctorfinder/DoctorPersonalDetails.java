package com.anshul.doctorfinder;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DoctorPersonalDetails extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button update;
    ImageView doctorImage;
    Bitmap imageBitmap;
    Button choose;
    String encodedImage;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    EditText dname, contact, whatsap, email, address, fees, qualification, experience, description, username, password;
    RadioGroup group;
    ArrayList<String> doctor_type = new ArrayList<String>();
    Spinner doctorSpinner;
    RadioButton male, female, others;
    String dnameSTR, specializationSTR, contactSTR, whatsapSTR, feesSTR, emailSTR, addressSTR, qualificationSTR, experienceSTR, descriptionSTR, usernameSTR, passwordSTR;
    String genderSTR;
    String doctorImageSTR = "";
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    ImageView pass;
    HashMap<String, String> hashMap = new HashMap<>();
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    int sizeImage;
    CardView cardview;

    String HttpURL = "http://doc.gsinfotec.in/loginphpfile.php?action=doctorupdatedetails";
    String finalResult;
    HttpParse httpParse = new HttpParse();
    TextView tap;
    long fileSizeInKB;
    String doctorid;
    ArrayAdapter<String> langAdapter;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_personal_details);
        Intent intent = getIntent();
        doctorid = intent.getStringExtra("doctorid");

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        }
        update = (Button) findViewById(R.id.update);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        doctorImage = (ImageView) findViewById(R.id.doctorImage);
        tap = (TextView) findViewById(R.id.tap);
        dname = (EditText) findViewById(R.id.dname);
        doctorSpinner = (Spinner) findViewById(R.id.doctorSpinner);
        contact = (EditText) findViewById(R.id.contact);
        whatsap = (EditText) findViewById(R.id.whatsapp);
       // cardview = (CardView) findViewById(R.id.cardview);
        email = (EditText) findViewById(R.id.email);
        address = (EditText) findViewById(R.id.address);
        fees = (EditText) findViewById(R.id.doctorfees);
        description = (EditText) findViewById(R.id.description);
        experience = (EditText) findViewById(R.id.experience);
        qualification = (EditText) findViewById(R.id.qualification);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        group = (RadioGroup) findViewById(R.id.radioSex);
        male = (RadioButton) findViewById(R.id.male);
        pass = (ImageView) findViewById(R.id.image);
        female = (RadioButton) findViewById(R.id.female);
        others = (RadioButton) findViewById(R.id.others);
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
        dnameSTR = dname.getText().toString();
        specializationSTR = doctorSpinner.getSelectedItem().toString();
        contactSTR = contact.getText().toString();
        whatsapSTR = whatsap.getText().toString();
        qualificationSTR = qualification.getText().toString();
        experienceSTR = experience.getText().toString();
        emailSTR = email.getText().toString().trim();
        addressSTR = address.getText().toString();
        descriptionSTR = description.getText().toString();
        usernameSTR = username.getText().toString();
        passwordSTR = password.getText().toString();
        feesSTR = fees.getText().toString();
        genderSTR = "";
        if (male.isChecked()) {
            genderSTR = male.getText().toString();
        } else if (female.isChecked()) {
            genderSTR = female.getText().toString();
        } else {
            genderSTR = others.getText().toString();
        }
        tap.setVisibility(View.GONE);
        doctorImage.setEnabled(false);
        dname.setEnabled(false);
        doctorSpinner.setEnabled(false);
        contact.setEnabled(false);
        whatsap.setEnabled(false);
        email.setEnabled(false);
        address.setEnabled(false);
        fees.setEnabled(false);
        description.setEnabled(false);
        experience.setEnabled(false);
        qualification.setEnabled(false);
        username.setEnabled(false);
        password.setEnabled(false);
        group.setEnabled(false);
        male.setEnabled(false);
        female.setEnabled(false);
        others.setEnabled(false);

        doctorImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setTitle("Personal Details");


        new AsyncLogin().execute(doctorid);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton radioButton = (RadioButton) findViewById(group.getCheckedRadioButtonId());
                dnameSTR = dname.getText().toString();
                specializationSTR = doctorSpinner.getSelectedItem().toString();
                contactSTR = contact.getText().toString();
                whatsapSTR = whatsap.getText().toString();
                qualificationSTR = qualification.getText().toString();
                experienceSTR = experience.getText().toString();
                emailSTR = email.getText().toString().trim();
                addressSTR = address.getText().toString();
                descriptionSTR = description.getText().toString();
                usernameSTR = username.getText().toString();
                passwordSTR = password.getText().toString();
                feesSTR = fees.getText().toString();

                genderSTR = "";
                if (male.isChecked()) {
                    genderSTR = male.getText().toString();
                } else if (female.isChecked()) {
                    genderSTR = female.getText().toString();
                } else {
                    genderSTR = others.getText().toString();
                }

                if (dnameSTR.equals("")) {
                    dname.setError("Please Enter Doctor's Name");
                    dname.requestFocus();
                    return;
                }

                if (radioButton == null) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Please Select Gender Type", Toast.LENGTH_LONG);
                    View view = toast.getView();

//Gets the actual oval background of the Toast then sets the colour filter
                    view.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);

//Gets the TextView from the Toast so it can be editted
                    TextView text = view.findViewById(android.R.id.message);
                    text.setTextColor(Color.BLACK);

                    toast.show();
                }

                if (doctorSpinner.getSelectedItemPosition() == 0) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Please Choose Specialization", Toast.LENGTH_LONG);
                    View view = toast.getView();

//Gets the actual oval background of the Toast then sets the colour filter
                    view.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);

//Gets the TextView from the Toast so it can be editted
                    TextView text = view.findViewById(android.R.id.message);
                    text.setTextColor(Color.BLACK);

                    toast.show();
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

                if (experienceSTR.equals("")) {
                    experience.setError("Please Enter Work Experience(in Years)");
                    experience.requestFocus();
                    return;
                }
                if (experience.getText().toString().length() > 2) {
                    experience.setError("Please Enter Correct Work Experience");
                    experience.requestFocus();
                    return;
                }
                if (Integer.valueOf(experienceSTR) > 70) {
                    experience.setError("Work Experience >70 not considered");
                    experience.requestFocus();
                    return;
                }
                if (qualificationSTR.equals("")) {
                    qualification.setError("Please Enter Your Max Qualification");
                    qualification.requestFocus();
                    return;
                }
                if (feesSTR.equals("")) {
                    fees.setError("Please Enter Your Fees");
                    fees.requestFocus();
                    return;
                }
                if (addressSTR.equals("")) {
                    address.setError("Please Enter Address");
                    address.requestFocus();
                    return;
                }

                if (descriptionSTR.equals("")) {
                    description.setError("Please Enter Your Description");
                    description.requestFocus();
                    return;
                }
                if (password.getText().toString().length() < 8 && !isValidPassword(password.getText().toString())) {
                    password.setError("Please Create Valid&Strong Password");
                    password.requestFocus();
                    return;
                }
                if (usernameSTR.equals("")) {
                    username.setError("Please Enter Username");
                    username.requestFocus();
                    return;
                }
                if (doctorImage.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.whhh).getConstantState())) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Please Upload Your Image", Toast.LENGTH_LONG);
                    View view = toast.getView();
                    view.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
                    TextView text = view.findViewById(android.R.id.message);
                    text.setTextColor(Color.BLACK);
                    toast.show();
                    return;
                }
                Bitmap bitmap =imageBitmap;
                String uploadImage = getStringImage(bitmap);
                StudentRecordUpdate( dnameSTR, genderSTR, contactSTR, whatsapSTR,specializationSTR,  emailSTR, addressSTR, qualificationSTR, feesSTR, experienceSTR, descriptionSTR, usernameSTR, passwordSTR,doctorid,uploadImage);
                finish();
                overridePendingTransition(R.anim.enter,R.anim.leave);
            }
        });


    }
    public String getStringImage(Bitmap bmp){
        if(bmp!=null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 0, baos);
            byte[] imageBytes = baos.toByteArray();
            encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        }
        return encodedImage;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.enter, R.anim.leave);
                return true;
            case R.id.doctor_edit:
                update.setVisibility(View.VISIBLE);
                tap.setVisibility(View.VISIBLE);
                doctorImage.setEnabled(true);
                dname.setEnabled(true);
                doctorSpinner.setEnabled(true);
                contact.setEnabled(true);
                whatsap.setEnabled(true);
                email.setEnabled(true);
                address.setEnabled(true);
                fees.setEnabled(true);
                description.setEnabled(true);
                experience.setEnabled(true);
                qualification.setEnabled(true);
                username.setEnabled(true);
                password.setEnabled(true);
                group.setEnabled(true);
                male.setEnabled(true);
                female.setEnabled(true);
                others.setEnabled(true);

                // doctorImage.setFocusableInTouchMode(true);
                dname.setFocusableInTouchMode(true);
                doctorSpinner.setFocusableInTouchMode(true);
                contact.setFocusableInTouchMode(true);
                whatsap.setFocusableInTouchMode(true);
                email.setFocusableInTouchMode(true);
                address.setFocusableInTouchMode(true);
                fees.setFocusableInTouchMode(true);
                description.setFocusableInTouchMode(true);
                experience.setFocusableInTouchMode(true);
                qualification.setFocusableInTouchMode(true);
                username.setFocusableInTouchMode(true);
                password.setFocusableInTouchMode(true);
                group.setFocusableInTouchMode(true);
                male.setFocusableInTouchMode(true);
                female.setFocusableInTouchMode(true);
                others.setFocusableInTouchMode(true);


                //   doctorImage.setFocusable(true);
                dname.setFocusable(true);
                doctorSpinner.setFocusable(true);
                contact.setFocusable(true);
                whatsap.setFocusable(true);
                email.setFocusable(true);
                address.setFocusable(true);
                fees.setFocusable(true);
                description.setFocusable(true);
                experience.setFocusable(true);
                qualification.setFocusable(true);
                username.setFocusable(true);
                password.setFocusable(true);
                group.setFocusable(true);
                male.setFocusable(true);
                female.setFocusable(true);
                others.setFocusable(true);

                dname.setTypeface(dname.getTypeface(), Typeface.BOLD);
                contact.setTypeface(contact.getTypeface(), Typeface.BOLD);
                whatsap.setTypeface(whatsap.getTypeface(), Typeface.BOLD);
                email.setTypeface(email.getTypeface(), Typeface.BOLD);
                address.setTypeface(address.getTypeface(), Typeface.BOLD);
                fees.setTypeface(fees.getTypeface(), Typeface.BOLD);
                description.setTypeface(description.getTypeface(), Typeface.BOLD);
                experience.setTypeface(experience.getTypeface(), Typeface.BOLD);
                qualification.setTypeface(qualification.getTypeface(), Typeface.BOLD);
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
        overridePendingTransition(R.anim.enter,R.anim.leave);
    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(DoctorPersonalDetails.this, R.style.PositiveButtonStyle11);
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
        listView.setDividerHeight(8);
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
            imageBitmap = (Bitmap) extras.get("data");
//            sizeImage = imageBitmap.getRowBytes() * imageBitmap.getHeight();
//            fileSizeInKB = sizeImage / 1024;
//            if (fileSizeInKB > 250) {
//                Toast toast = Toast.makeText(getApplicationContext(), "Please Choose Image less than 250Kb", Toast.LENGTH_LONG);
//                View view = toast.getView();
//                view.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
//                TextView text = view.findViewById(android.R.id.message);
//                text.setTextColor(Color.BLACK);
//                toast.show();
//            } else {
                doctorImage.setImageBitmap(imageBitmap);
           // }
        }
        if (resultCode == RESULT_OK && requestCode == 2) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    // String path = saveImage(bitmap);
                    //Toast.makeText(MainActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    doctorImage.setImageBitmap(imageBitmap);
                    // UploadImageOnServerButton.setVisibility(View.VISIBLE);

                } catch (IOException e) {
                    e.printStackTrace();
                    //Toast.makeText(MainActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
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

    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

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
            doctorImage.setImageURI(choosen);
        }


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class AsyncLogin extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(DoctorPersonalDetails.this);
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
                url = new URL("http://doc.gsinfotec.in/loginphpfile.php?action=fetchCompletePersonaldetails");
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
                        .appendQueryParameter("doctorid", params[0]);

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
             //   Toast.makeText(getApplicationContext(), "Result:-" + result, Toast.LENGTH_LONG).show();
                JSONArray jsonArray;

                try {
                    jsonArray = new JSONArray(result);
                    Bitmap image=null;

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        doctorImageSTR = jsonObject.getString("docimage");
                        String  docid = jsonObject.getString("docid");
                        dnameSTR = jsonObject.getString("docname");
                        genderSTR = jsonObject.getString("gendertype");
                        specializationSTR = jsonObject.getString("specialization");
                        addressSTR = jsonObject.getString("address");
                        contactSTR = jsonObject.getString("contactnumber");
                        whatsapSTR = jsonObject.getString("whatsappnumber");
                        emailSTR = jsonObject.getString("email");
                        qualificationSTR = jsonObject.getString("qualification");
                        experienceSTR = jsonObject.getString("experience");
                        feesSTR = jsonObject.getString("fees");
                        descriptionSTR = jsonObject.getString("description");
                        usernameSTR = jsonObject.getString("username");
                        passwordSTR = jsonObject.getString("password");
                        byte[] decodeString = Base64.decode(jsonObject.getString("password"), Base64.DEFAULT);
                        Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString,
                                0, decodeString.length);
//                        img.setImageBitmap(decodebitmap);
//                        image = BitmapFactory.decodeStream(jsonObject.getString("docimage"));

                        dname.setText(dnameSTR);
                        new GetImage().execute(docid);
//                        GetImage gi = new GetImage();
//                        gi.execute(docid);
//                        if (male.isChecked()) {
//                            genderSTR = male.getText().toString();
//                        } else if (female.isChecked()) {
//                            genderSTR = female.getText().toString();
//                        } else {
//                            genderSTR = others.getText().toString();
//                        }
                        if (genderSTR.equals("Male")) {
                            male.setChecked(true);
                        }
                        if (genderSTR.equals("Female")) {
                            female.setChecked(true);
                        }
                        if (genderSTR.equals("Others")) {
                            others.setChecked(true);
                        }
                        int spinnerPosition = langAdapter.getPosition(specializationSTR);
                        doctorSpinner.setSelection(spinnerPosition);

                        address.setText(addressSTR);
                        contact.setText(contactSTR);
                        whatsap.setText(whatsapSTR);
                        email.setText(emailSTR);
                        qualification.setText(qualificationSTR);
                        experience.setText(experienceSTR);
                        fees.setText(feesSTR);
                        description.setText(descriptionSTR);
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
    class GetImage extends AsyncTask<String,Void,Bitmap>{
        ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(DoctorPersonalDetails.this, "Loading Please Wait...", null,true,true);
        }

        @Override
        protected void onPostExecute(Bitmap b) {
            super.onPostExecute(b);
            loading.dismiss();
//            Toast.makeText(getApplicationContext(),"come",Toast.LENGTH_LONG).show();
            if(b!=null){
                doctorImage.setImageBitmap(b);
                imageBitmap=b;
            }

        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String id = params[0];
            String add = "http://doc.gsinfotec.in/loginphpfile.php?action=get_doctor_image&id="+id;
            URL url = null;
            Bitmap image = null;
            try {
                url = new URL(add);
                image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return image;
        }
    }

//    GetImage gi = new GetImage();
//        gi.execute(id);

    public void StudentRecordUpdate(final String docname, final String gender, final String contact, final String whatsapp,final String specialization,  final String email,final String address, final String qualification, final String fees,final String experience,  final String description, final String username, final String password,final String doctorid,final String uploadImage) {

        class StudentRecordUpdateClass extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {


                hashMap.put("docname", params[0]);
                hashMap.put("gendertype", params[1]);
                hashMap.put("contactnumber", params[2]);
                hashMap.put("whatsappnumber", params[3]);
                hashMap.put("specialization", params[4]);
                hashMap.put("email", params[5]);
                hashMap.put("address", params[6]);
                hashMap.put("qualification", params[7]);
                hashMap.put("fees", params[8]);
                hashMap.put("experience", params[9]);
                hashMap.put("description", params[10]);
                hashMap.put("username", params[11]);
                hashMap.put("password", params[12]);
                hashMap.put("docid", params[13]);
                hashMap.put("docimage", params[14]);


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

        studentRecordUpdateClass.execute(docname, gender,  contact, whatsapp,specialization, email,address, qualification,fees, experience,  description, username, password,doctorid,uploadImage);
    }
}
