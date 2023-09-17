package com.example.if_shop_android_app.app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.if_shop_android_app.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    Button btnRecover, btnBackForgotPass;

    EditText edTxtEmailRecovery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        btnRecover = findViewById(R.id.btnRecover);
        btnBackForgotPass = findViewById(R.id.btnBackForgotPass);

        edTxtEmailRecovery = findViewById(R.id.edTxtEmailRecovery);

        btnRecover.setOnClickListener(view -> {

        });

        btnBackForgotPass.setOnClickListener(view -> {
            Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}