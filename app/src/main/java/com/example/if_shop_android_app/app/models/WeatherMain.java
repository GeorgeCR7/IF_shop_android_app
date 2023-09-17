package com.example.if_shop_android_app.app.models;

public class WeatherMain {

    private double temp;

    public WeatherMain() {
    }

    public WeatherMain(double temp) {
        this.temp = temp;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }
}
