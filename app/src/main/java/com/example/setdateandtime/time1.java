package com.example.setdateandtime;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class time1 extends AppCompatActivity {
    TextView date, time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time1);

        time = findViewById(R.id.time);
        date = findViewById(R.id.date);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Toast.makeText(this, "NO date received", Toast.LENGTH_LONG).show();
        } else {
            String date1 = extras.getString("Date");
            String time1 = extras.getString("Time");
            date.setText("Date is:"+date1);
            time.setText("Time is:"+time1);
        }

    }
}