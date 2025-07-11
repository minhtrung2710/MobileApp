package com.example.doandominhtrung_2120110322;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<CartItem> cartList;

    public CartAdapter(Context context, List<CartItem> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvName, tvPrice, tvQuantity;
        Button btnRemove;

        public CartViewHolder(View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            btnRemove = itemView.findViewById(R.id.btnRemove);
        }
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        CartItem item = cartList.get(position);
        Product product = item.getProduct();

        holder.imgProduct.setImageResource(product.getImageResId());
        holder.tvName.setText(product.getName());
        holder.tvPrice.setText(product.getPrice());
        holder.tvQuantity.setText("Số lượng: " + item.getQuantity());

        holder.btnRemove.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();

            if (currentPosition != RecyclerView.NO_POSITION && currentPosition < cartList.size()) {
                Product productToRemove = cartList.get(currentPosition).getProduct();

                // Xoá trong CartManager
                CartManager.getInstance().removeFromCart(productToRemove);

                // Cập nhật lại cartList từ CartManager
                cartList = new ArrayList<>(CartManager.getInstance().getCartItems());
                notifyDataSetChanged();

                // Cập nhật RecyclerView
                notifyDataSetChanged();

                if (onItemRemovedListener != null) {
                    onItemRemovedListener.onItemRemoved();
                }
            }
        });


    }

    public interface OnItemRemovedListener {
        void onItemRemoved();
    }

    private OnItemRemovedListener onItemRemovedListener;

    public void setOnItemRemovedListener(OnItemRemovedListener listener) {
        this.onItemRemovedListener = listener;
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }
}
