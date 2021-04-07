package com.anshul.doctorfinder;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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

public class WeekDaysAvailability extends AppCompatActivity {
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    Button save;
    static final int TIME_DIALOG_ID = 1111;
    private int hour;
    private int minute;
    HashMap<String, String> hashMap = new HashMap<>();
    String HttpURL = "http://doc.gsinfotec.in/loginphpfile.php?action=weekdaysupdatedetails";
    String finalResult;
    HttpParse httpParse = new HttpParse();
    CheckBox su, mo, tu, we, th, fr, sat;
    EditText sus1, sus2, sue1, sue2, mos1, mos2, moe1, moe2, tus1, tus2, tue1, tue2, wes1, wes2, wee1, wee2, ths1, ths2, the1, the2, frs1, frs2, fre1, fre2, sats1, sats2, sate1, sate2;
    String doctorid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_days_availability);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Intent intent=getIntent();
        doctorid=intent.getStringExtra("doctorid");


        Toast toast = Toast.makeText(getApplicationContext(), "S1:-Starttime1 ,E1:-Endtime1 ,S2:-Startime2 ,E2:-Endtime2", Toast.LENGTH_LONG);
        View view = toast.getView();

//Gets the actual oval background of the Toast then sets the colour filter
        view.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);

//Gets the TextView from the Toast so it can be editted
        TextView text = view.findViewById(android.R.id.message);
        text.setTextColor(Color.WHITE);

        toast.show();
        save = (Button) findViewById(R.id.save);
        su = (CheckBox) findViewById(R.id.chsu);
        mo = (CheckBox) findViewById(R.id.chmo);
        tu = (CheckBox) findViewById(R.id.chtu);
        we = (CheckBox) findViewById(R.id.chwe);
        th = (CheckBox) findViewById(R.id.chth);
        fr = (CheckBox) findViewById(R.id.chfr);
        sat = (CheckBox) findViewById(R.id.chsat);
        sus1 = (EditText) findViewById(R.id.sus1);
        sus2 = (EditText) findViewById(R.id.sus2);
        sue1 = (EditText) findViewById(R.id.sue1);
        sue2 = (EditText) findViewById(R.id.sue2);
        mos1 = (EditText) findViewById(R.id.mos1);
        mos2 = (EditText) findViewById(R.id.mos2);
        moe1 = (EditText) findViewById(R.id.moe1);
        moe2 = (EditText) findViewById(R.id.moe2);
        tus1 = (EditText) findViewById(R.id.tus1);
        tus2 = (EditText) findViewById(R.id.tus2);
        tue1 = (EditText) findViewById(R.id.tue1);
        tue2 = (EditText) findViewById(R.id.tue2);
        wes1 = (EditText) findViewById(R.id.wes1);
        wes2 = (EditText) findViewById(R.id.wes2);
        wee1 = (EditText) findViewById(R.id.wee1);
        wee2 = (EditText) findViewById(R.id.wee2);
        ths1 = (EditText) findViewById(R.id.ths1);
        ths2 = (EditText) findViewById(R.id.ths2);
        the1 = (EditText) findViewById(R.id.the1);
        the2 = (EditText) findViewById(R.id.the2);
        frs1 = (EditText) findViewById(R.id.frs1);
        frs2 = (EditText) findViewById(R.id.frs2);
        fre1 = (EditText) findViewById(R.id.fre1);
        fre2 = (EditText) findViewById(R.id.fre2);
        sats1 = (EditText) findViewById(R.id.sats1);
        sats2 = (EditText) findViewById(R.id.sats2);
        sate1 = (EditText) findViewById(R.id.sate1);
        sate2 = (EditText) findViewById(R.id.sate2);



        setTitle("Leaves Menu");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        sus1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(WeekDaysAvailability.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        sus1.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Set Time");
                mTimePicker.show();
            }
        });
        sus2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(WeekDaysAvailability.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        sus2.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Set Time");
                mTimePicker.show();
            }
        });
        sue2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(WeekDaysAvailability.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        sue2.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Set Time");
                mTimePicker.show();
            }
        });
        sue1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(WeekDaysAvailability.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        sue1.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Set Time");
                mTimePicker.show();
            }
        });
        mos1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(WeekDaysAvailability.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        mos1.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Set Time");
                mTimePicker.show();
            }
        });
        mos2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(WeekDaysAvailability.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        mos2.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Set Time");
                mTimePicker.show();
            }
        });
        moe2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(WeekDaysAvailability.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        moe2.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Set Time");
                mTimePicker.show();
            }
        });
        moe1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(WeekDaysAvailability.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        moe1.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Set Time");
                mTimePicker.show();
            }
        });

        tus1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(WeekDaysAvailability.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        tus1.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Set Time");
                mTimePicker.show();
            }
        });
        tue1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(WeekDaysAvailability.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        tue1.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Set Time");
                mTimePicker.show();
            }
        });
        tus2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(WeekDaysAvailability.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        tus2.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Set Time");
                mTimePicker.show();
            }
        });
        tue2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(WeekDaysAvailability.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        tue2.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Set Time");
                mTimePicker.show();
            }
        });

        wes1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(WeekDaysAvailability.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        wes1.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Set Time");
                mTimePicker.show();
            }
        });
        wee1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(WeekDaysAvailability.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        wee1.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Set Time");
                mTimePicker.show();
            }
        });
        wes2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(WeekDaysAvailability.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        wes2.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Set Time");
                mTimePicker.show();
            }
        });
        wee2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(WeekDaysAvailability.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        wee2.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Set Time");
                mTimePicker.show();
            }
        });
        ths1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(WeekDaysAvailability.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        ths1.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Set Time");
                mTimePicker.show();
            }
        });
        the1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(WeekDaysAvailability.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        the1.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Set Time");
                mTimePicker.show();
            }
        });
        ths2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(WeekDaysAvailability.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        ths2.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Set Time");
                mTimePicker.show();
            }
        });
        the2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(WeekDaysAvailability.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        the2.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Set Time");
                mTimePicker.show();
            }
        });
        frs1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(WeekDaysAvailability.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        frs1.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Set Time");
                mTimePicker.show();
            }
        });
        fre1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(WeekDaysAvailability.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        fre1.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Set Time");
                mTimePicker.show();
            }
        });
        frs2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(WeekDaysAvailability.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        frs2.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Set Time");
                mTimePicker.show();
            }
        });
        fre2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(WeekDaysAvailability.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        fre2.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Set Time");
                mTimePicker.show();
            }
        });
        sats1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(WeekDaysAvailability.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        sats1.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Set Time");
                mTimePicker.show();
            }
        });
        sate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(WeekDaysAvailability.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        sate1.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Set Time");
                mTimePicker.show();
            }
        });
        sats2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(WeekDaysAvailability.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        sats2.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Set Time");
                mTimePicker.show();
            }
        });
        sate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(WeekDaysAvailability.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        sate2.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Set Time");
                mTimePicker.show();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if(!su.isChecked()&& !mo.isChecked()&&!tu.isChecked()&&!we.isChecked()&&!th.isChecked()&&!fr.isChecked()&&!sat.isChecked()){
                   Toast toast = Toast.makeText(getApplicationContext(), "Please Save Your Availability Days", Toast.LENGTH_LONG);
                   View view = toast.getView();

//Gets the actual oval background of the Toast then sets the colour filter
                   view.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);

//Gets the TextView from the Toast so it can be editted
                   TextView text = view.findViewById(android.R.id.message);
                   text.setTextColor(Color.WHITE);

                   toast.show();
               }
               if(su.isChecked()){
                   if(sus1.getText().toString().equals("")||(sue1.getText().toString().equals(""))){
                      Toast.makeText(getApplicationContext(),"Please Enter Sunday Timings",Toast.LENGTH_LONG).show();
                       return;
                   }
                   if(sus2.getText().toString().equals("")){

                   }

                   WeekDaysRecordUpdate(doctorid,sus1.getText().toString(),sue1.getText().toString(),sus2.getText().toString(),sue2.getText().toString(),"Sun");

               }
                if(mo.isChecked()){
                    if(mos1.getText().toString().equals("")||(moe1.getText().toString().equals(""))){
                        Toast.makeText(getApplicationContext(),"Please Enter Monday Timings",Toast.LENGTH_LONG).show();
                        return;
                    }
                    WeekDaysRecordUpdate(doctorid,mos1.getText().toString(),moe1.getText().toString(),mos2.getText().toString(),moe2.getText().toString(),"Mon");

                }
                if(tu.isChecked()){
                    if(tus1.getText().toString().equals("")||(tue1.getText().toString().equals(""))){
                        Toast.makeText(getApplicationContext(),"Please Enter Tuesday Timings",Toast.LENGTH_LONG).show();
                        return;
                    }
                    WeekDaysRecordUpdate(doctorid,tus1.getText().toString(),tue1.getText().toString(),tus2.getText().toString(),tue2.getText().toString(),"Tue");

                }
                if(we.isChecked()){
                    if(wes1.getText().toString().equals("")||(wee1.getText().toString().equals(""))){
                        Toast.makeText(getApplicationContext(),"Please Enter Wednesday Timings",Toast.LENGTH_LONG).show();
                        return;
                    }
                    WeekDaysRecordUpdate(doctorid,wes1.getText().toString(),wee1.getText().toString(),wes2.getText().toString(),wee2.getText().toString(),"Wed");

                }
                if(th.isChecked()){
                    if(ths1.getText().toString().equals("")||(the1.getText().toString().equals(""))){
                        Toast.makeText(getApplicationContext(),"Please Enter Thursday Timings",Toast.LENGTH_LONG).show();
                        return;
                    }
                    WeekDaysRecordUpdate(doctorid,ths1.getText().toString(),the1.getText().toString(),ths2.getText().toString(),the2.getText().toString(),"Thr");

                }
                if(fr.isChecked()){
                    if(frs1.getText().toString().equals("")||(fre1.getText().toString().equals(""))){
                        Toast.makeText(getApplicationContext(),"Please Enter Friday Timings",Toast.LENGTH_LONG).show();
                        return;
                    }
                    WeekDaysRecordUpdate(doctorid,frs1.getText().toString(),fre1.getText().toString(),frs2.getText().toString(),fre2.getText().toString(),"Fri");

                }
                if(sat.isChecked()){
                    if(sats1.getText().toString().equals("")||(sate1.getText().toString().equals(""))){
                        Toast.makeText(getApplicationContext(),"Please Enter Saturday Timings",Toast.LENGTH_LONG).show();
                        return;
                    }
                    WeekDaysRecordUpdate(doctorid,sats1.getText().toString(),sate1.getText().toString(),sats2.getText().toString(),sate2.getText().toString(),"Sat");

                }
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.weekdaysmenu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.enter, R.anim.leave);
                return true;
            case R.id.weekdays:
                AlertDialog.Builder alert = new AlertDialog.Builder(WeekDaysAvailability.this, R.style.PositiveButtonStyle11);
                alert.setCancelable(false);
                alert.setTitle("Welcome To Help Section !!!");
                String titleText12 ="This is WeekDays Leaves Section which you can fill once and edit later on so that patients can see your availability when patient can book your appointment. In this You have to enable checkbox for week days for which you are present,otherwise you will be marked as absent on these days.So after enable as per suitable,you have to enter shift timings for which you are present.In this 2 shifts are available,in which if you have to available at only one shift either on morning or everning,you have to fill first shift timings,if you have available on both shifts,you have to enter timings of both shifts......If you have some query after reading this,(Please contact :- 7988327513) or (Email directly to :- anshulgupta2498@gmail.com)";
                ForegroundColorSpan foregroundColorSpan12 = new ForegroundColorSpan(Color.BLACK);
                SpannableStringBuilder ssBuilder12 = new SpannableStringBuilder(titleText12);
                ssBuilder12.setSpan(
                        foregroundColorSpan12,
                        0,
                        titleText12.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                );
                alert.setMessage(ssBuilder12);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();

                    }
                });

                AlertDialog alert1 = alert.create();
                alert1.setCanceledOnTouchOutside(false);
                alert1.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
                alert1.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.enter, R.anim.leave);
    }

    public void WeekDaysRecordUpdate(final String doctorid,  final String starttime1, final String endtime1,final String starttime2,  final String endtime2,final String dayname) {

        class StudentRecordUpdateClass extends AsyncTask<String, Void, String> {
            private ProgressDialog dialog = new ProgressDialog(WeekDaysAvailability.this);
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


                hashMap.put("docid", params[0]);
                hashMap.put("start1", params[1]);
                hashMap.put("end1", params[2]);
                hashMap.put("start2", params[3]);
                hashMap.put("end2", params[4]);
                hashMap.put("dayname", params[5]);
                // hashMap.put("docimage", params[14]);


                Log.d("update111", "" + hashMap);
                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                Log.d("ssddsddddd", "" + httpResponseMsg);
                super.onPostExecute(httpResponseMsg);
                Toast toast = Toast.makeText(getApplicationContext(), httpResponseMsg, Toast.LENGTH_LONG);
                View view = toast.getView();

//Gets the actual oval background of the Toast then sets the colour filter
                view.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);

//Gets the TextView from the Toast so it can be editted
                TextView text = view.findViewById(android.R.id.message);
                text.setTextColor(Color.WHITE);

                toast.show();

            }


        }

        StudentRecordUpdateClass studentRecordUpdateClass = new StudentRecordUpdateClass();

        studentRecordUpdateClass.execute(doctorid,  starttime1, endtime1,starttime2, endtime2,dayname);
    }
}
