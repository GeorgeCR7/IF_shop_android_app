package com.example.if_shop_android_app.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.if_shop_android_app.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin, btnGoRegister;

    EditText edTxtLoginEmail, edTxtLoginPassword;

    TextView txtForgotPassword;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);
        btnGoRegister = findViewById(R.id.btnGoRegister);

        edTxtLoginEmail = findViewById(R.id.edTxtLoginEmail);
        edTxtLoginPassword = findViewById(R.id.edTxtLoginPassword);

        txtForgotPassword = findViewById(R.id.txtForgotPassword);

        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(view -> {
            loginUser();
        });

        btnGoRegister.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });

        txtForgotPassword.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
            finish();

            /*Intent intent = new Intent(LoginActivity.this, WeatherActivity.class);
            startActivity(intent);
            finish();*/
        });
    }

    private void loginUser(){

        String email = edTxtLoginEmail.getText().toString();
        String password = edTxtLoginPassword.getText().toString();

        if (email.isEmpty()) {
            edTxtLoginEmail.setError(getResources().getString(R.string.email_empty));
            edTxtLoginEmail.requestFocus();
        } else if (!isEmailValid(email)) {
            edTxtLoginEmail.setError(getResources().getString(R.string.email_not_valid));
            edTxtLoginEmail.requestFocus();
        } else if (password.isEmpty()){
            edTxtLoginPassword.setError(getResources().getString(R.string.password_empty));
            edTxtLoginPassword.requestFocus();
        } else {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this,
                            R.string.log_in_success,
                            Toast.LENGTH_SHORT).show();
                    showProperActivity(mAuth);
                } else {
                    Toast.makeText(LoginActivity.this,
                            R.string.log_in_error + "" + task.getException(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void showProperActivity(FirebaseAuth mAuth) {

        if (mAuth.getCurrentUser().getEmail().equals("admin@ifshop.gr")) {
            Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private static boolean isEmailValid (String email) {

        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null) {
            return false;
        }
        return pat.matcher(email).matches();
    }
}