//package com.example.doandominhtrung_2120110322;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.MenuItem;
//import android.widget.Button;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.TextView;
//import androidx.appcompat.widget.Toolbar;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//
//import com.bumptech.glide.Glide;
//
//public class ProductDetailActivity extends AppCompatActivity {
//
//    ImageView productImage;
//    TextView productName, productDescription, productPrice;
//    Button buyButton;
//    ImageButton btnAddToCart;
//
//    // Thêm số lượng
//    TextView tvQuantity;
//    Button btnIncrease, btnDecrease;
//    int quantity = 1;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_product_detail);
//
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setTitle("Chi tiết sản phẩm");
//        }
//
//        productImage = findViewById(R.id.productImage);
//        productName = findViewById(R.id.productName);
//        productDescription = findViewById(R.id.productDescription);
//        productPrice = findViewById(R.id.productPrice);
//        buyButton = findViewById(R.id.buyButton);
//        btnAddToCart = findViewById(R.id.btnAddToCart);
//
//        tvQuantity = findViewById(R.id.tvQuantity);
//        btnIncrease = findViewById(R.id.btnIncrease);
//        btnDecrease = findViewById(R.id.btnDecrease);
//
//        // Nhận dữ liệu từ Intent
//        Intent intent = getIntent();
//        String name = intent.getStringExtra("name");
//        String description = intent.getStringExtra("description");
//        String imageUrl = intent.getStringExtra("image_url");
//        Log.d("DEBUG_IMAGE_URL", "Image URL nhận được: " + imageUrl);
//        String price = intent.getStringExtra("price");
//
//
//        // Set dữ liệu lên view
//        productName.setText(name);
//        productDescription.setText(description);
//        productPrice.setText(price);
//
//        tvQuantity.setText(String.valueOf(quantity));
//
//        // Load ảnh từ URL bằng Glide
//        Glide.with(this)
//                .load(imageUrl)
//                .placeholder(R.drawable.placeholder)
//                .into(productImage);
//
//        // Xử lý tăng/giảm số lượng
//        btnIncrease.setOnClickListener(v -> {
//            quantity++;
//            tvQuantity.setText(String.valueOf(quantity));
//        });
//
//        btnDecrease.setOnClickListener(v -> {
//            if (quantity > 1) {
//                quantity--;
//                tvQuantity.setText(String.valueOf(quantity));
//            }
//        });
//
//        btnAddToCart.setOnClickListener(v -> {
//            // Tạo sản phẩm từ dữ liệu hiện tại
//            Product product = new Product(name, description, imageUrl, price);
//
//            // Thêm vào giỏ hàng
//            CartManager.getInstance().addToCart(product, quantity);
//
//            // Chuyển sang màn hình giỏ hàng (tùy chọn)
//            Intent cartIntent = new Intent(ProductDetailActivity.this, CartActivity.class);
//            startActivity(cartIntent);
//        });
//
//    }
//
//    // Xử lý sự kiện khi bấm nút quay lại
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            finish();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//}