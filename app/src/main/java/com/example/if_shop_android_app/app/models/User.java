package com.example.if_shop_android_app.app.models;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private String email, name, country, dateCreated, age;

    public User() {}

    public User(String email, String name, String country, String dateCreated, String age) {
        this.email = email;
        this.name = name;
        this.country = country;
        this.dateCreated = dateCreated;
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    protected User(Parcel in) {
        email = in.readString();
        name = in.readString();
        country = in.readString();
        dateCreated = in.readString();
        age = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(email);
        parcel.writeString(name);
        parcel.writeString(country);
        parcel.writeString(dateCreated);
        parcel.writeString(age);
    }
}