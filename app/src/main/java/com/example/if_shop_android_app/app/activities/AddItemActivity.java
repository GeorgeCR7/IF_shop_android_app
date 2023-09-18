package com.example.if_shop_android_app.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.if_shop_android_app.R;
import com.example.if_shop_android_app.app.local_DB.LocalDBHandler;
import com.example.if_shop_android_app.app.models.Product;
import com.example.if_shop_android_app.app.service.ProductCloudService;
import com.example.if_shop_android_app.app.service.SyncDBService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddItemActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button btnAddProduct, btnShowList, btnBackAddItem;

    EditText edTxtProduct;

    Spinner spnQuantity;

    int quantity;

    DatabaseReference reference;

    FirebaseAuth mAuth;

    ProductCloudService productCloudService;
    SyncDBService syncDBService;

    LocalDBHandler localDB;

    ArrayList<Product> allProducts, myProducts, localProducts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        btnAddProduct = findViewById(R.id.btnAddProduct);
        btnShowList = findViewById(R.id.btnShowList);
        btnBackAddItem = findViewById(R.id.btnBackAddItem);

        edTxtProduct = findViewById(R.id.edTxtProduct);

        spnQuantity = findViewById(R.id.spnQuantity);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.numbers,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnQuantity.setAdapter(adapter);
        spnQuantity.setOnItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();

        allProducts = new ArrayList<>();
        myProducts = new ArrayList<>();
        localProducts = new ArrayList<>();

        productCloudService = new ProductCloudService();
        syncDBService = new SyncDBService(productCloudService);
        localDB = new LocalDBHandler();

        reference = FirebaseDatabase.getInstance().getReference("Products");

        btnAddProduct.setOnClickListener(view -> {

            String productName = edTxtProduct.getText().toString();
            int id = generateRandomInt();

            Product product = new Product(id, productName, quantity, mAuth.getCurrentUser().getEmail());

            // Upload product object to local MySQL Database.
            localDB.doAction(product, "INSERT", mAuth.getCurrentUser().getEmail());

            // Upload product object to remote Firebase Database.
            productCloudService.uploadProduct(product, id, reference);
        });

        btnShowList.setOnClickListener(view -> {

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Product product = dataSnapshot.getValue(Product.class);
                        allProducts.add(product);
                    }

                    for (Product p : allProducts){
                        if (p.getAddedByUser().equals(mAuth.getCurrentUser().getEmail())){
                            myProducts.add(p);
                        }
                    }

                    // Get the products from the local MySQL Database.
                    downloadLocalProducts();
                    localProducts = localDB.getProducts();

                    // Trigger synchronization
                    //syncDBService.syncProducts(localProducts);

                    Intent intent = new Intent(AddItemActivity.this, ListActivity.class);
                    intent.putParcelableArrayListExtra("PRODUCTS_LIST", myProducts);
                    startActivity(intent);
                    finish();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
        });

        btnBackAddItem.setOnClickListener(view -> {
            Intent intent = new Intent(AddItemActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private int generateRandomInt(){

        int min = 10000000;
        int max = 99999999;

        return (int)Math.floor(Math.random() * (max - min + 1) + min);
    }

    private void downloadLocalProducts(){
        localDB.doAction(null, "SELECT", mAuth.getCurrentUser().getEmail());
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String q = adapterView.getItemAtPosition(i).toString();
        quantity = Integer.parseInt(q);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}
}