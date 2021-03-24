package com.anshul.doctorfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;


public class HelpActivity extends AppCompatActivity {
    TextView number, email;
    String numberSTR1 = "";
    Button back;
    Button whatsapp;
    TextView directOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        number = (TextView) findViewById(R.id.number);
        email = (TextView) findViewById(R.id.email);
        back = (Button) findViewById(R.id.back);
        whatsapp = (Button) findViewById(R.id.whatsapp);

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "anshulgupta2498@gmail.com"));
                intent.putExtra(Intent.EXTRA_SUBJECT, "your_subject");
                intent.putExtra(Intent.EXTRA_TEXT, "your_text");
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);

            }
        });
        number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberSTR1 = number.getText().toString();
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + numberSTR1));
                startActivity(callIntent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);

            }
        });
        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contact = "+91 7988327513"; // use country code with your phone number
                String url = "https://api.whatsapp.com/send?phone=" + contact;
                try {
                    PackageManager pm = getApplicationContext().getPackageManager();
                    pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);

                } catch (PackageManager.NameNotFoundException e) {
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.whatsapp&hl=en_IN"));
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    e.printStackTrace();
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.enter, R.anim.leave);

            }
        });
    }

}
