package com.example.if_shop_android_app.app.local_DB;

import com.example.if_shop_android_app.app.models.Product;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class LocalDBHandler {

    private ArrayList<Product> products;

    public LocalDBHandler() {}

    public void doAction(Product product, String actionType, String userEmail) {

        final String DB_URL = "jdbc:mysql://localhost:3306/ifshoplocaldb?useUnicode=yes&characterEncoding=UTF-8&useSSL=false&allowPublicKeyRetrieval=true";
        final String DB_USER = "";
        final String DB_PASS = "";

        new Thread(() -> {
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);

                if (actionType.equals("INSERT")){
                    String sqlQuery = "INSERT INTO products (id, productName, quantity, addedByUser) VALUES (?, ?, ?, ?)";
                    PreparedStatement preparedStatement = con.prepareStatement(sqlQuery);
                    preparedStatement.setInt(1, product.getId());
                    preparedStatement.setString(2, product.getName());
                    preparedStatement.setInt(3, product.getQuantity());
                    preparedStatement.setString(4, product.getAddedByUser());
                    preparedStatement.execute();
                    preparedStatement.close();
                } else if (actionType.equals("SELECT")){
                    String sqlQuery = "SELECT * FROM products WHERE addedByUser = ?";
                    PreparedStatement preparedStatement = con.prepareStatement(sqlQuery);
                    preparedStatement.setString(1, userEmail);
                    ResultSet rs = preparedStatement.executeQuery();

                    while (rs.next()) {
                        Product p = new Product();

                        p.setId(rs.getInt("id"));
                        p.setName(rs.getString("name"));
                        p.setQuantity(rs.getInt("quantity"));
                        p.setAddedByUser(rs.getString("addedByUser"));

                        products.add(p);

                        preparedStatement.close();
                        rs.close();
                    }
                }

                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }
}
