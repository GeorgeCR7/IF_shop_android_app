package com.example.if_shop_android_app.app.service;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.if_shop_android_app.R;
import com.example.if_shop_android_app.app.models.Product;
import com.example.if_shop_android_app.app.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductCloudService {

    private ArrayList<Product> products;

    public ProductCloudService() {
        this.products = new ArrayList<>();
    }


    public void uploadProduct(Product product, int id, DatabaseReference reference) {
        reference.child("product_" + id).setValue(product);
    }

    public void downloadProducts(DatabaseReference reference) {

        ArrayList<Product> tempList = new ArrayList<>();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product p  = dataSnapshot.getValue(Product.class);
                    tempList.add(p);
                }
                Log.i("List size ondatachange: ", ""+tempList.size());
                setProducts(tempList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });


    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        Log.i("List size setProducts: ", ""+products.size());
        this.products = products;
    }
}
