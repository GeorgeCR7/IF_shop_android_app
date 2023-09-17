package com.example.if_shop_android_app.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.if_shop_android_app.R;
import com.example.if_shop_android_app.app.models.Product;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<Product> {

    public ListAdapter(Context context, ArrayList<Product> products){
        super(context, R.layout.list_item, products);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Product product = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        TextView productName = convertView.findViewById(R.id.txtProductName);
        productName.setText(product.getName());

        TextView productQuantity = convertView.findViewById(R.id.txtProductQuantity);
        productQuantity.setText(String.valueOf(product.getQuantity()));

        return convertView;
    }
}
