package com.example.doandominhtrung_2120110322;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private RecyclerView cartRecyclerView;
    private TextView totalPriceTextView;
    private Button checkoutButton;

    private ArrayList<CartItem> cartItems;
    private CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        totalPriceTextView = findViewById(R.id.totalPriceTextView);
        checkoutButton = findViewById(R.id.checkoutButton);

        cartItems = CartManager.getInstance().getCartItems();
        cartAdapter = new CartAdapter(this, cartItems);
        cartAdapter.setOnItemRemovedListener(() -> updateTotalPrice());

        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartRecyclerView.setAdapter(cartAdapter);

        updateTotalPrice();

        checkoutButton.setOnClickListener(v -> {
            if (CartManager.getInstance().getCartItems().isEmpty()) {
                Toast.makeText(this, "Giỏ hàng đang trống!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Thanh toán thành công!", Toast.LENGTH_SHORT).show();
                CartManager.getInstance().clearCart();
                finish();
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbarCart);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // hiện nút back
            getSupportActionBar().setTitle("Giỏ hàng"); // đặt lại tiêu đề nếu muốn
        }
    }

    private void updateTotalPrice() {
        int total = 0;
        ArrayList<CartItem> items = CartManager.getInstance().getCartItems();
        for (CartItem cartItem : items) {
            int price = Integer.parseInt(cartItem.getProduct().getPrice().replaceAll("\\D+", ""));
            total += price * cartItem.getQuantity();
        }
        totalPriceTextView.setText(getString(R.string.total_price_format, total));

        // Vô hiệu hóa nút nếu giỏ hàng trống
        checkoutButton.setEnabled(!items.isEmpty());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // quay lại màn hình trước đó
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
