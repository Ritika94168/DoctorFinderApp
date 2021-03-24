package com.anshul.doctorfinder;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ClinicLoginScreen extends AppCompatActivity {
    Button login, back;
    EditText username, password1;
    TextView attempts;
    ImageButton image;
    LinearLayout l2;
    String usernameSTR, passwordSTR;
    TextView text3,create;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_login_screen);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        username = (EditText) findViewById(R.id.username);
        image = (ImageButton) findViewById(R.id.image);
        password1 = (EditText) findViewById(R.id.password);
        text3 = (TextView) findViewById(R.id.text3);
        create=(TextView)findViewById(R.id.createaccountclinic);
        login = (Button) findViewById(R.id.login);
        attempts = (TextView) findViewById(R.id.attempts);
        back = (Button) findViewById(R.id.back);
        l2 = (LinearLayout) findViewById(R.id.l2);


        // Underline some text from a particular TextView and give font color(Blue) to it...
        String styledText = "<u><font color='blue'>Clinic Login Menu</font></u>";
        text3.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE);
        String styledText1 = "<u><b><font color='red'>No Account Yet? Create One</font></b></u>";
        create.setText(Html.fromHtml(styledText1), TextView.BufferType.SPANNABLE);


        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClinicLoginScreen.this, AddNewClinic.class);
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
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (password1.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                    password1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    password1.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }

        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ClinicLoginScreen.this,MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter,R.anim.leave);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameSTR=username.getText().toString();
                passwordSTR=password1.getText().toString();

                if(usernameSTR.equals("")){
                    username.setError("Please Enter Username");
                    username.requestFocus();
                    return;
                }
                if(passwordSTR.equals("")){
                    password1.setError("Please Enter Password");
                    password1.requestFocus();
                    return;
                }
                Intent intent=new Intent(ClinicLoginScreen.this,ClinicMainMenu.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in,R.anim.left_out);


            }
        });

    }


    @Override
    public void onBackPressed() {

    }
}
