package com.example.if_shop_android_app.app.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.if_shop_android_app.app.ListAdapter;
import com.example.if_shop_android_app.app.models.Product;
import com.example.if_shop_android_app.databinding.ActivityListBinding;

import java.util.ArrayList;


public class ListActivity extends AppCompatActivity {

    private ArrayList<Product> products;

    ActivityListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        products = new ArrayList<>();
        products = getIntent().getParcelableArrayListExtra("PRODUCTS_LIST");

        ListAdapter listAdapter = new ListAdapter(ListActivity.this, products);
        binding.listViewSights.setAdapter(listAdapter);
        binding.listViewSights.setClickable(false);
    }
}