package com.example.if_shop_android_app.app.activities;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.if_shop_android_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText recoveryEmail;

    TextView warningLabel, warningMessage;

    Button btnSendEmailRecover, btnBackForgotPass;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        recoveryEmail = findViewById(R.id.recoveryEmail);

        warningLabel = findViewById(R.id.warningLabel);
        warningLabel.setPaintFlags(warningLabel.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        warningLabel.setVisibility(View.INVISIBLE);

        warningMessage = findViewById(R.id.warningMessage);
        warningMessage.setVisibility(View.INVISIBLE);

        btnSendEmailRecover = findViewById(R.id.btnSendEmailRecover);
        btnBackForgotPass = findViewById(R.id.btnBackForgotPass);

        mAuth = FirebaseAuth.getInstance();

        btnSendEmailRecover.setOnClickListener(view -> sendRecoveryMail());

        btnBackForgotPass.setOnClickListener(view -> {
            Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void sendRecoveryMail() {

        String email = recoveryEmail.getText().toString();

        if (email.isEmpty()) {
            recoveryEmail.setError(getResources().getString(R.string.email_empty));
            recoveryEmail.requestFocus();
            return;
        } else if (!isEmailValid(email)) {
            recoveryEmail.setError(getResources().getString(R.string.email_not_valid));
            recoveryEmail.requestFocus();
            return;
        } else {
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ForgotPasswordActivity.this,
                                getResources().getString(R.string.check_inbox_recover),
                                Toast.LENGTH_SHORT).show();
                        recoveryEmail.getText().clear();
                        warningLabel.setVisibility(View.VISIBLE);
                        warningMessage.setVisibility(View.VISIBLE);
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
}