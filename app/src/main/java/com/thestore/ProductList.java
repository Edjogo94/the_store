package com.thestore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import db.Sqlite_OpenHelper;

public class ProductList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private Sqlite_OpenHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        recyclerView = findViewById(R.id.recyclerView);
        productList = new ArrayList<>();

        dbHelper = new Sqlite_OpenHelper(getApplicationContext(), getResources().getString(R.string.DATABASE_NAME), null, 1);
        productList = dbHelper.getProductList();

        productAdapter = new ProductAdapter(getApplicationContext(), productList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(productAdapter);
    }
}
