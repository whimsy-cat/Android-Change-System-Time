package com.example.setdateandtime;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TimePicker time;
    DatePicker date;

    Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        time = findViewById(R.id.time);
        date = findViewById(R.id.date);
        time.setEnabled(true);
        date.setEnabled(true);
        time.setIs24HourView(false);
        date.setSpinnersShown(true);

        String currentdate = String.valueOf(date.getDayOfMonth());
        String currentmonth = String.valueOf(date.getMonth() + 1);
        String currentyear = String.valueOf(date.getYear());
        String currenthr = String.valueOf(time.getCurrentHour());
        String currentmin = String.valueOf(time.getCurrentMinute());
        Toast.makeText(this, "" + currenthr + ":" + currentmin + "..." + currentdate + "/" + currentmonth + "/" + currentyear, Toast.LENGTH_LONG).show();
        b = findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                String hrs = String.valueOf(time.getHour());
                String mins = String.valueOf(time.getMinute());

                String setdate = String.valueOf(date.getDayOfMonth());
                String setmonth = String.valueOf(date.getMonth() + 1);
                String setyear = String.valueOf(date.getYear());
                // te.setText(hrs+":"+mins+"..."+setdate+"/"+setmonth+"/"+setyear);
                if (hrs == null || mins == null || setdate == null || setdate == null || setyear == null) {
                    Toast.makeText(MainActivity.this, "PLEASE SELECT CORRECT DATE AND TIME", Toast.LENGTH_LONG);
                } else {

                    Intent i = new Intent(MainActivity.this, time1.class);
                    i.putExtra("Date", setdate +"/"+ setmonth +"/"+ setyear);
                    i.putExtra("Time", hrs +":"+ mins);

                    startActivity(i);
                }

            }
        });
    }
}