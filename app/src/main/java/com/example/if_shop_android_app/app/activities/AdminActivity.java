package com.example.if_shop_android_app.app.activities;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.if_shop_android_app.R;
import com.example.if_shop_android_app.app.models.User;
import com.example.if_shop_android_app.databinding.ActivityAdminBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class AdminActivity extends AppCompatActivity {

    ActivityAdminBinding binding;

    TextView txtAdminPage;

    EditText edTxtEmailForDelete;

    Button btnDeleteUser, btnLogOutAdmin;

    DatabaseReference reference;

    FirebaseAuth mAuth;

    private ArrayList<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        txtAdminPage = findViewById(R.id.txtAdminPage);
        txtAdminPage.setPaintFlags(txtAdminPage.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        edTxtEmailForDelete = findViewById(R.id.edTxtEmailForDelete);
        btnDeleteUser = findViewById(R.id.btnDeleteUser);
        btnLogOutAdmin = findViewById(R.id.btnLogOutAdmin);

        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("Users");

        users = new ArrayList<>();

        binding.btnDeleteUser.setOnClickListener(view -> {

            String emailForDeletion = binding.edTxtEmailForDelete.getText().toString();

            if (emailForDeletion.isEmpty()){
                edTxtEmailForDelete.setError(getResources().getString(R.string.email_empty));
                edTxtEmailForDelete.requestFocus();
            } else if (!isEmailValid(emailForDeletion)) {
                edTxtEmailForDelete.setError(getResources().getString(R.string.email_not_valid));
                edTxtEmailForDelete.requestFocus();
            } else {
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            User user = dataSnapshot.getValue(User.class);
                            users.add(user);
                        }

                        for (User user: users) {
                            if (user.getEmail().equals(emailForDeletion)) {
                                deleteUser(emailForDeletion.replace(".", ""));
                                break;
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
            }
        });

        btnLogOutAdmin.setOnClickListener(view -> {
            mAuth.signOut();
            Toast.makeText(AdminActivity.this,
                    R.string.log_out_main,
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void deleteUser(String emailForDeletion) {

        reference.child(emailForDeletion).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Toast.makeText(AdminActivity.this, R.string.delete_success, Toast.LENGTH_SHORT).show();
                binding.edTxtEmailForDelete.setText("");
            } else {
                Toast.makeText(AdminActivity.this, R.string.delete_fail, Toast.LENGTH_SHORT).show();
            }
        });
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