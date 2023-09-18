package com.example.if_shop_android_app.app.service;

import com.example.if_shop_android_app.app.models.Product;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SyncDBService {

    private ProductCloudService cloudService;

    DatabaseReference reference;

    public SyncDBService(ProductCloudService cloudService){
        this.cloudService = cloudService;
    }

    public void syncProducts(ArrayList<Product> localProducts){

        reference = FirebaseDatabase.getInstance().getReference("Products");

        // Download the products list from the remote Firebase DB.
        cloudService.downloadCloudProducts(reference);
        ArrayList<Product> cloudProducts = cloudService.getProducts();

        ProductCloudService productCloudService= new ProductCloudService();

        for (Product localProduct : localProducts){
            for (Product cloudProduct : cloudProducts) {
                // Find the different product object and upload it.
                if (cloudProduct.getId() != localProduct.getId()) {
                    productCloudService.uploadProduct(localProduct, localProduct.getId(), reference);
                }
            }
        }
    }
}
