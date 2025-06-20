package com.example.doandominhtrung_2120110322;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Quay lại màn hình chính
        Button btn = findViewById(R.id.btnBack);
        btn.setOnClickListener(v -> {
            Intent it = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(it);
        });

        // Hiển thị danh sách sản phẩm
        RecyclerView recyclerView = findViewById(R.id.recyclerProducts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Product> products = new ArrayList<>();
        products.add(new Product("iPhone 15", "Hàng mới 100%", R.drawable.iphone_16_pro_max, "Giá: 35.000.000đ"));
        products.add(new Product("Samsung S23", "Bảo hành 12 tháng", R.drawable.samsung_s24_ultra, "Giá: 22.000.000đ"));
        products.add(new Product("Xiaomi 13", "Hiệu năng mạnh mẽ", R.drawable.xiaomi_note_13_pro, "Giá: 12.000.000đ"));

        ProductAdapter adapter = new ProductAdapter(this, products);
        recyclerView.setAdapter(adapter);
    }
}
