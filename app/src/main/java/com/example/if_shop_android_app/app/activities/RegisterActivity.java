package com.example.if_shop_android_app.app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.if_shop_android_app.R;
import com.example.if_shop_android_app.app.models.User;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    Button btnRegister, btnGoLogin;

    EditText edTxtRegisterEmail, edTxtRegisterPassword, edTxtRegisterPasswordAgain;

    // Firebase object for authentication.
    FirebaseAuth mAuth;

    // Firebase objects for reading database.
    private FirebaseDatabase rootNode;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnRegister = findViewById(R.id.btnRegister);
        btnGoLogin = findViewById(R.id.btnGoLogin);

        edTxtRegisterEmail = findViewById(R.id.edTxtRegisterEmail);
        edTxtRegisterPassword = findViewById(R.id.edTxtRegisterPassword);
        edTxtRegisterPasswordAgain = findViewById(R.id.edTxtRegisterPasswordAgain);

        rootNode = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(view -> {
            registerUser();
        });

        btnGoLogin.setOnClickListener(view -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

    }

    private void registerUser(){

        String email = edTxtRegisterEmail.getText().toString();
        String password = edTxtRegisterPassword.getText().toString();
        String passwordConfirm = edTxtRegisterPasswordAgain.getText().toString();

        reference = FirebaseDatabase.getInstance().getReference("Users");

        if (email.isEmpty()) {
            edTxtRegisterEmail.setError(getResources().getString(R.string.email_empty));
            edTxtRegisterEmail.requestFocus();
            return;
        } else if (!isEmailValid(email)) {
            edTxtRegisterEmail.setError(getResources().getString(R.string.email_not_valid));
            edTxtRegisterEmail.requestFocus();
            return;
        } else if (password.isEmpty()) {
            edTxtRegisterPassword.setError(getResources().getString(R.string.password_empty));
            edTxtRegisterPassword.requestFocus();
            return;
        } else if (password.length() < 6) {
            edTxtRegisterPassword.setError(getResources().getString(R.string.password_length_error));
            edTxtRegisterPassword.requestFocus();
            return;
        } else if (passwordConfirm.isEmpty()) {
            edTxtRegisterPasswordAgain.setError(getResources().getString(R.string.password_confirm));
            edTxtRegisterPasswordAgain.requestFocus();
            return;
        } else if (!password.equals(passwordConfirm)) {
            Toast.makeText(RegisterActivity.this,
                    R.string.passwords_not_match,
                    Toast.LENGTH_SHORT).show();
            return;
        } else {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this,
                                R.string.success_register,
                                Toast.LENGTH_SHORT).show();
                        // Create a new User object, at first, only with email.
                        User user = new User(email, "", "", setDateCreated(), "");
                        // Store the new User to the Firebase.
                        reference.child(email.replace(".","")).setValue(user);

                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this,
                                R.string.error_register + "" + task.getException(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
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

    private String setDateCreated() {

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

        String day, month;

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

        return day + month + cal.get(Calendar.YEAR);
    }
}