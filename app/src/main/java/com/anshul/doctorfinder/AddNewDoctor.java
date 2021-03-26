package com.anshul.doctorfinder;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddNewDoctor extends AppCompatActivity implements  AdapterView.OnItemSelectedListener{
    ImageView doctorImage;
    Button choose;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    EditText dname, contact, whatsap, email, address,fees, qualification, experience, description, username, password;
    RadioGroup group;
    Spinner doctortype;
    RadioButton male, female, others;
    Button register;
    String dnameSTR, specializationSTR, contactSTR, whatsapSTR,feesSTR, emailSTR, addressSTR, qualificationSTR, experienceSTR, descriptionSTR, usernameSTR, passwordSTR;
    String genderSTR;
    String doctorImageSTR = "";
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    ImageView pass;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    int sizeImage;
    ArrayAdapter<String> langAdapter;
    CardView cardview;
    long fileSizeInKB;
    ArrayList<String> doctor_type = new ArrayList<String>();
    Spinner doctorSpinner;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_doctor);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        doctorImage = (ImageView) findViewById(R.id.doctorImage);
        dname = (EditText) findViewById(R.id.dname);
        doctorSpinner = (Spinner) findViewById(R.id.doctorSpinner);
        contact = (EditText) findViewById(R.id.contact);
        whatsap = (EditText) findViewById(R.id.whatsapp);
        cardview = (CardView) findViewById(R.id.cardview);
        email = (EditText) findViewById(R.id.email);
        address = (EditText) findViewById(R.id.address);
        fees=(EditText)findViewById(R.id.doctorfees);
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
        register = (Button) findViewById(R.id.register);
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


        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        }
        doctorImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });


        setTitle("Doctor Registration Module");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
        register.setOnClickListener(new View.OnClickListener() {
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

                new AsyncLogin().execute(dnameSTR,genderSTR,contactSTR,whatsapSTR,specializationSTR,emailSTR,addressSTR,qualificationSTR,feesSTR,experienceSTR,descriptionSTR,usernameSTR,passwordSTR,doctorImageSTR);
//                AlertDialog.Builder alert = new AlertDialog.Builder(AddNewDoctor.this, R.style.PositiveButtonStyle111);
//                alert.setCancelable(false);
//                String titleText = "Alert!!";
//                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.GREEN);
//                SpannableStringBuilder ssBuilder = new SpannableStringBuilder(titleText);
//                ssBuilder.setSpan(
//                        foregroundColorSpan,
//                        0,
//                        titleText.length(),
//                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
//                );
//                alert.setTitle(ssBuilder);
//                String titleText1 = "Are You Want To Choose Subscription ??";
//                ForegroundColorSpan foregroundColorSpan1 = new ForegroundColorSpan(Color.WHITE);
//                SpannableStringBuilder ssBuilder1 = new SpannableStringBuilder(titleText1);
//                ssBuilder1.setSpan(
//                        foregroundColorSpan1,
//                        0,
//                        titleText1.length(),
//                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
//                );
//                alert.setMessage(ssBuilder1);
//
//                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        Intent intent = new Intent(AddNewDoctor.this, ChooseSubscription.class);
//                        startActivity(intent);
//                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
//                    }
//                });
//
//                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        AlertDialog.Builder alert = new AlertDialog.Builder(AddNewDoctor.this, R.style.PositiveButtonStyle11);
//                        alert.setCancelable(false);
//                        alert.setTitle("Subscription Benefits !!!");
//                        alert.setMessage("Subscription feature is very useful thing for becoming popular through this App.It is especially designed for Doctor's ,through this feature your profile can be seen to more and more patients ,according to the subscription pack.Here are some packs defined through which profile of various doctors can be saw to patients.So I suggest every doctor to choose some subscription pack,so that you get more patients and you earn more money.Every Subscription Pack is valid for only 1 year,after that you can renew your subscription pack.Don't worry,you will get notification 2 days ago,when your plan expires. ");
//
//                        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int whichButton) {
//                                AlertDialog.Builder alert = new AlertDialog.Builder(AddNewDoctor.this, R.style.PositiveButtonStyle111);
//                                alert.setCancelable(false);
//                                String titleText = "Alert!!";
//                                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.GREEN);
//                                SpannableStringBuilder ssBuilder = new SpannableStringBuilder(titleText);
//                                ssBuilder.setSpan(
//                                        foregroundColorSpan,
//                                        0,
//                                        titleText.length(),
//                                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
//                                );
//                                alert.setTitle(ssBuilder);
//                                String titleText1 = "Are You Want To Choose Subscription ??";
//                                ForegroundColorSpan foregroundColorSpan1 = new ForegroundColorSpan(Color.WHITE);
//                                SpannableStringBuilder ssBuilder1 = new SpannableStringBuilder(titleText1);
//                                ssBuilder1.setSpan(
//                                        foregroundColorSpan1,
//                                        0,
//                                        titleText1.length(),
//                                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
//                                );
//                                alert.setMessage(ssBuilder1);
//
//                                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int whichButton) {
//                                        Intent intent = new Intent(AddNewDoctor.this, ChooseSubscription.class);
//                                        startActivity(intent);
//                                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
//                                    }
//                                });
//
//                                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int whichButton) {
//                                        finish();
//                                        overridePendingTransition(R.anim.enter, R.anim.leave);
//                                    }
//                                });
//                                AlertDialog alert1 = alert.create();
//                                alert1.setCanceledOnTouchOutside(false);
//                                alert1.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
//                                alert1.show();
//
//                            }
//                        });
//
//                        AlertDialog alert1 = alert.create();
//                        alert1.setCanceledOnTouchOutside(false);
//                        alert1.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
//                        alert1.show();
//
//                    }
//                });
//                AlertDialog alert1 = alert.create();
//                alert1.setCanceledOnTouchOutside(false);
//                alert1.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
//                alert1.show();


            }
        });
        doctorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
        finish();
        overridePendingTransition(R.anim.enter, R.anim.leave);
    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AddNewDoctor.this, R.style.PositiveButtonStyle11);
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
                doctorImage.setImageBitmap(imageBitmap);
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
        ProgressDialog pdLoading = new ProgressDialog(AddNewDoctor.this);
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
                url = new URL("http://doc.gsinfotec.in/loginphpfile.php?action=registerDoctor");

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
                        .appendQueryParameter("docname", params[0])
                        .appendQueryParameter("gendertype", params[1])
                        .appendQueryParameter("contactnumber", params[2])
                        .appendQueryParameter("whatsappnumber", params[3])
                        .appendQueryParameter("specialization", params[4])
                        .appendQueryParameter("email", params[5])
                        .appendQueryParameter("address", params[6])
                        .appendQueryParameter("qualification", params[7])
                        .appendQueryParameter("fees", params[8])
                        .appendQueryParameter("experience", params[9])
                        .appendQueryParameter("description", params[10])
                        .appendQueryParameter("username", params[11])
                        .appendQueryParameter("password", params[12])
                        .appendQueryParameter("docimage", params[13]);



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
            Toast toast = Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG);
            View view = toast.getView();

//Gets the actual oval background of the Toast then sets the colour filter
            view.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);

//Gets the TextView from the Toast so it can be editted
            TextView text = view.findViewById(android.R.id.message);
            text.setTextColor(Color.WHITE);

            toast.show();
//       //     Toast.makeText(getApplicationContext(), "" + result, Toast.LENGTH_LONG).show();
//            if (result.equalsIgnoreCase("true")) {
//                /* Here launching another activity when login successful. If you persist login state
//                use sharedPreferences of Android. and logout button to clear sharedPreferences.
//                 */
//                Toast toast = Toast.makeText(getApplicationContext(), "Registration Done Successfully", Toast.LENGTH_LONG);
//                View view = toast.getView();
//
////Gets the actual oval background of the Toast then sets the colour filter
//                view.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
//
////Gets the TextView from the Toast so it can be editted
//                TextView text = view.findViewById(android.R.id.message);
//                text.setTextColor(Color.WHITE);
//
//                toast.show();
//                finish();
//                overridePendingTransition(R.anim.enter, R.anim.leave);
//
//            } else if (result.equalsIgnoreCase("false")) {
//
//                // If username and password does not match display a error message
//                Toast.makeText(AddNewDoctor.this, "Oops! Something went wrong.", Toast.LENGTH_LONG).show();
//
//            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {
//
//                Toast.makeText(AddNewDoctor.this, "Oops! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
//
//            }
        }

    }
}