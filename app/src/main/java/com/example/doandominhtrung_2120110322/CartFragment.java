package com.example.doandominhtrung_2120110322;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CartFragment extends Fragment {

    private RecyclerView cartRecyclerView;
    private TextView totalPriceTextView;
    private Button checkoutButton;

    private ArrayList<CartItem> cartItems;
    private CartAdapter cartAdapter;

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cartRecyclerView = view.findViewById(R.id.cartRecyclerView);
        totalPriceTextView = view.findViewById(R.id.totalPriceTextView);
        checkoutButton = view.findViewById(R.id.checkoutButton);

        cartItems = CartManager.getInstance().getCartItems();
        cartAdapter = new CartAdapter(getContext(), cartItems);
        cartAdapter.setOnItemRemovedListener(this::updateTotalPrice);

        cartRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cartRecyclerView.setAdapter(cartAdapter);

        updateTotalPrice();

        checkoutButton.setOnClickListener(v -> {
            if (cartItems.isEmpty()) {
                Toast.makeText(getContext(), "Giỏ hàng đang trống!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Thanh toán thành công!", Toast.LENGTH_SHORT).show();
                CartManager.getInstance().clearCart();
                cartAdapter.notifyDataSetChanged();
                updateTotalPrice();
            }
        });

        Toolbar toolbar = view.findViewById(R.id.toolbarCart);
        if (toolbar != null) {
            ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
            if (((AppCompatActivity) requireActivity()).getSupportActionBar() != null) {
                ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle("Giỏ hàng");
            }

            toolbar.setNavigationOnClickListener(v -> {
                requireActivity().getSupportFragmentManager().popBackStack();
            });
        }
    }

    private void updateTotalPrice() {
        cartItems = CartManager.getInstance().getCartItems();
        int total = 0;
        for (CartItem item : cartItems) {
            int price = Integer.parseInt(item.getProduct().getPrice().replaceAll("\\D+", ""));
            total += price * item.getQuantity();
        }

        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        totalPriceTextView.setText("Tổng tiền: " + formatter.format(total) + "đ");
        checkoutButton.setEnabled(!cartItems.isEmpty());
    }
}
