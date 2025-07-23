package com.example.doandominhtrung_2120110322;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

public class ProductDetailFragment extends Fragment {

    private Product product;
    private int quantity = 1;

    public static ProductDetailFragment newInstance(Product product) {
        ProductDetailFragment fragment = new ProductDetailFragment();
        Bundle args = new Bundle();
        args.putString("name", product.getName());
        args.putString("detail", product.getDetail());
        args.putString("imageUrl", product.getImageUrl());
        args.putString("price", product.getPrice());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            product = new Product(
                    args.getString("name"),
                    args.getString("detail"),
                    args.getString("imageUrl"),
                    args.getString("price")
            );
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);

        ImageView productImage = view.findViewById(R.id.productImage);
        TextView productName = view.findViewById(R.id.productName);
        TextView productDescription = view.findViewById(R.id.productDescription);
        TextView productPrice = view.findViewById(R.id.productPrice);
        TextView tvQuantity = view.findViewById(R.id.tvQuantity);
        Button btnIncrease = view.findViewById(R.id.btnIncrease);
        Button btnDecrease = view.findViewById(R.id.btnDecrease);
//        Button buyButton = view.findViewById(R.id.buyButton);
        ImageButton btnAddToCart = view.findViewById(R.id.btnAddToCart);

        if (product != null) {
            productName.setText(product.getName());
            productDescription.setText(product.getDetail());
            productPrice.setText(product.getPrice());
            Glide.with(requireContext())
                    .load(product.getImageUrl())
                    .placeholder(R.drawable.placeholder)
                    .into(productImage);
        }

        tvQuantity.setText(String.valueOf(quantity));

        btnIncrease.setOnClickListener(v -> {
            quantity++;
            tvQuantity.setText(String.valueOf(quantity));
        });

        btnDecrease.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                tvQuantity.setText(String.valueOf(quantity));
            }
        });

        btnAddToCart.setOnClickListener(v -> {
            CartManager.getInstance().addToCart(product, quantity);
            Toast.makeText(getContext(), "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();

            // Chuyển sang giỏ hàng
            Fragment cartFragment = new CartFragment();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, cartFragment)
                    .addToBackStack(null) // Quan trọng: để quay lại được
                    .commit();
        });

//        buyButton.setOnClickListener(v -> {
//            Toast.makeText(getContext(), "Chức năng mua ngay chưa được xử lý!", Toast.LENGTH_SHORT).show();
//        });

        // Thiết lập Toolbar với tiêu đề và nút quay lại
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        if (toolbar != null) {
            ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle("Chi tiết sản phẩm");
            setHasOptionsMenu(true);
        }

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            requireActivity().getSupportFragmentManager().popBackStack();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
