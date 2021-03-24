package com.anshul.doctorfinder;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Pattern;

public class AddNewClinic extends AppCompatActivity {
    EditText clinicName, landlineno, mobileno, email, password, address, pincode;
    Button clinicRegister;
    TextView alreadyAccount;
    String nameSTR, landlineSTR, mobileSTR, emailSTR, passwordSTR, addressSTR, pincodeSTR;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_clinic);
        setTitle("Clinic Registration Module");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        clinicName = (EditText) findViewById(R.id.editTextName);
        landlineno = (EditText) findViewById(R.id.editTextLandline);
        mobileno = (EditText) findViewById(R.id.editTextMobile);
        email = (EditText) findViewById(R.id.editTextEmail);
        password = (EditText) findViewById(R.id.editTextPassword);
        address = (EditText) findViewById(R.id.editTextAddress);
        pincode = (EditText) findViewById(R.id.editTextPincode);
        clinicRegister = (Button) findViewById(R.id.clinicRegister);
        alreadyAccount = (TextView) findViewById(R.id.alreadyAccount);

        alreadyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.enter, R.anim.leave);
            }
        });
        clinicRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameSTR = clinicName.getText().toString();
                landlineSTR = landlineno.getText().toString();
                mobileSTR = mobileno.getText().toString();
                emailSTR = email.getText().toString();
                passwordSTR = password.getText().toString();
                addressSTR = address.getText().toString();
                pincodeSTR = pincode.getText().toString();

                if (nameSTR.equals("")) {
                    clinicName.setError("Please Enter Clinic Name");
                    clinicName.requestFocus();
                    return;
                }
                if (landlineSTR.equals("")) {
                    landlineno.setError("Please Enter Landline Number");
                    landlineno.requestFocus();
                    return;
                }
                if (emailSTR.equals("")) {
                    email.setError("Please Enter Email-Address");
                    email.requestFocus();
                    return;
                }
                if (passwordSTR.equals("")) {
                    password.setError("Please Create Strong Password");
                    password.requestFocus();
                    return;
                }
                if (addressSTR.equals("")) {
                    address.setError("Please Enter Clinic Address");
                    address.requestFocus();
                    return;
                }

                if (mobileno.length() != 10) {
                    mobileno.setError("Please Enter Valid Mobile Number");
                    mobileno.requestFocus();
                    return;
                }
                if (isPincodeValid(pincodeSTR)) {
                    pincode.setError("Please Enter Valid Pincode");
                    pincode.requestFocus();
                    return;
                }
                if (!emailSTR.matches(emailPattern)) {
                    email.setError("Please Enter Valid Email-Address");
                    email.requestFocus();
                    return;
                }

                if (isNumberValid(landlineSTR)) {
                    landlineno.setError("Please Enter Valid Landline Number");
                    landlineno.requestFocus();
                    return;
                }


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
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.enter, R.anim.leave);
    }

    private boolean isNumberValid(String number) {
        return Pattern.matches("^[0-9][1-9]{9}$", number);
    }

    private boolean isPincodeValid(String number) {
        return Pattern.matches( "^[1-9]{1}[0-9]{2}\\s{0,1}[0-9]{3}$", number);
    }
}