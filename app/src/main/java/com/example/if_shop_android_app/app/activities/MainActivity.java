package com.example.if_shop_android_app.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.if_shop_android_app.R;
import com.google.firebase.auth.FirebaseAuth;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView txtAppTitle, txtDate;

    Button btnMakeList, btnWeather;
    Button btnProfile, btnLogOut;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        txtAppTitle = findViewById(R.id.txtAppTitle);

        txtDate = findViewById(R.id.txtDate);
        txtDate.setText(setCurrentDate());

        btnMakeList = findViewById(R.id.btnMakeList);
        btnWeather = findViewById(R.id.btnWeather);
        btnProfile = findViewById(R.id.btnProfile);
        btnLogOut = findViewById(R.id.btnLogOut);


        btnMakeList.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
            startActivity(intent);
            finish();
        });

        btnWeather.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
            startActivity(intent);
            finish();
        });

        btnProfile.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
            finish();
        });

        btnLogOut.setOnClickListener(view -> {
            mAuth.signOut();
            Toast.makeText(MainActivity.this,
                    R.string.log_out_main,
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private String setCurrentDate(){

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

        String dayName = null, day, month;

        if(cal.get(Calendar.DAY_OF_MONTH) < 10) {
            day = "0" + cal.get(Calendar.DAY_OF_MONTH)+".";
        } else {
            day = ""+cal.get(Calendar.DAY_OF_MONTH)+".";
        }

        if((cal.get(Calendar.MONTH)+1) < 10) {
            month = "0" + (cal.get(Calendar.MONTH)+1)+".";
        } else {
            month = ""+(cal.get(Calendar.MONTH)+1)+".";
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDate today = LocalDate.now();
            DayOfWeek dayOfWeek = today.getDayOfWeek();
            dayName = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault());
        }

        if (dayName.equals("Sunday")) {
            dayName = getResources().getString(R.string.sunday);
        } else if (dayName.equals("Monday")) {
            dayName = getResources().getString(R.string.monday);
        } else if (dayName.equals("Tuesday")) {
            dayName = getResources().getString(R.string.tuesday);
        } else if (dayName.equals("Wednesday")) {
            dayName = getResources().getString(R.string.wednesday);
        } else if (dayName.equals("Thursday")) {
            dayName = getResources().getString(R.string.thursday);
        } else if (dayName.equals("Friday")) {
            dayName = getResources().getString(R.string.friday);
        } else if (dayName.equals("Saturday")) {
            dayName = getResources().getString(R.string.saturday);
        }

        return dayName + ", " +day + month + cal.get(Calendar.YEAR);
    }
}