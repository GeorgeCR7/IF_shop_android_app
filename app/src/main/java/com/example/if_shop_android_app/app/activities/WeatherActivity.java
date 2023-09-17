package com.example.if_shop_android_app.app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.if_shop_android_app.R;
import com.example.if_shop_android_app.app.LoadingDialog;
import com.example.if_shop_android_app.app.models.WeatherResponse;
import com.example.if_shop_android_app.app.networking.RetrofitClient;
import com.example.if_shop_android_app.app.networking.WeatherService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherActivity extends AppCompatActivity implements LocationListener {

    Button btnShowWeather, btnBackWeather;

    TextView txtTempLabel, txtTempValue;

    private WeatherService weatherService;

    // Location manager object.
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        LoadingDialog loadingDialog = new LoadingDialog(WeatherActivity.this);

        btnShowWeather = findViewById(R.id.btnShowWeather);
        btnBackWeather = findViewById(R.id.btnBackWeather);

        txtTempLabel = findViewById(R.id.txtTempLabel);
        txtTempLabel.setVisibility(View.INVISIBLE);

        txtTempValue = findViewById(R.id.txtTempValue);

        weatherService = RetrofitClient.getClient().create(WeatherService.class);

        //Runtime permissions for get access to Location.
        if (ContextCompat.checkSelfPermission(WeatherActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(WeatherActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }

        btnShowWeather.setOnClickListener(view -> {
            loadingDialog.startLoadingDialog();
            Handler handler = new Handler();
            handler.postDelayed(loadingDialog::dismissDialog, 4600);
            getMyLocation();
        });

        btnBackWeather.setOnClickListener(view -> {
            Intent intent = new Intent(WeatherActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @SuppressLint("MissingPermission")
    private void getMyLocation(){
        try{
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, WeatherActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        getWeatherData(location.getLatitude(), location.getLongitude());
    }

    private void getWeatherData(double myLat, double myLong) {

        Call<WeatherResponse> call = weatherService.getWeather(myLat, myLong,
                getResources().getString(R.string.apiKey));

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful()) {
                    WeatherResponse weatherResponse = response.body();
                    if (weatherResponse != null) {
                        createTempUI(Math.ceil(weatherResponse.getMain().getTemp() - 273.15));
                    }
                } else {
                    // Handle API call failure
                    Toast.makeText(WeatherActivity.this,
                            R.string.weather_fail,
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                // Handle network failure or other errors
                Toast.makeText(WeatherActivity.this,
                        R.string.weather_fail,
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void createTempUI(double temp){

        txtTempLabel.setVisibility(View.VISIBLE);
        txtTempValue.setText(temp + " °C");

        if (temp <= 40 && temp >= 30){
            txtTempValue.setTextColor(Color.RED);
        } else if (temp < 30 && temp >= 20) {
            txtTempValue.setTextColor(Color.GREEN);
        } else {
            txtTempValue.setTextColor(Color.BLUE);
        }
    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}