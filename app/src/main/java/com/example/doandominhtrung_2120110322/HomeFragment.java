package com.example.doandominhtrung_2120110322;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.*;
import com.android.volley.toolbox.*;

import org.json.*;

import java.net.URLEncoder;
import java.util.*;

public class HomeFragment extends Fragment implements ProductAdapter.OnProductClickListener {

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<Product> allProducts = new ArrayList<>();
    private List<Product> currentProducts = new ArrayList<>();
    private static final int ITEMS_PER_PAGE = 5;
    private int currentPage = 1;

    private static final String URL = "http://10.0.2.2/android_api/get_products.php";
    private TextView tvUsername;

    private EditText etSearch;
    private Button btnApple, btnSamsung, btnVivo, btnXiaomi;
    private Button btnDienThoai, btnDongHo;
    private int selectedBrand = -1;
    private int selectedCategory = -1;
    private LinearLayout paginationLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tvUsername = view.findViewById(R.id.tvUsername);
        etSearch = view.findViewById(R.id.etSearch);
        btnApple = view.findViewById(R.id.btnApple);
        btnSamsung = view.findViewById(R.id.btnSamsung);
        btnVivo = view.findViewById(R.id.btnVivo);
        btnXiaomi = view.findViewById(R.id.btnXiaomi);
        btnDienThoai = view.findViewById(R.id.btnDienThoai);
        btnDongHo = view.findViewById(R.id.btnDongHo);
        recyclerView = view.findViewById(R.id.recyclerProducts);
        paginationLayout = view.findViewById(R.id.paginationLayout);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ProductAdapter(getContext(), currentProducts, this);
        recyclerView.setAdapter(adapter);

        // Xử lý hiển thị username và menu popup
        SharedPreferences prefs = requireContext().getSharedPreferences("user_session", Context.MODE_PRIVATE);
        String username = prefs.getString("username", "Người dùng");
        tvUsername.setText("Xin chào, " + username);

        tvUsername.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(getContext(), tvUsername);
            popup.getMenuInflater().inflate(R.menu.user_menu, popup.getMenu());

            popup.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if (id == R.id.menu_change_password) {
                    requireActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new ChangePasswordFragment())
                            .addToBackStack(null)
                            .commit();
                    return true;
                } else if (id == R.id.menu_logout) {
                    new android.app.AlertDialog.Builder(requireContext())
                            .setTitle("Xác nhận đăng xuất")
                            .setMessage("Bạn có chắc chắn muốn đăng xuất?")
                            .setPositiveButton("Đăng xuất", (dialog, which) -> {
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.clear();
                                editor.apply();
                                startActivity(new Intent(getContext(), MainActivity.class));
                                requireActivity().finish();
                            })
                            .setNegativeButton("Không", (dialog, which) -> dialog.dismiss())
                            .show();
                    return true;
                }

                return false;
            });

            popup.show();
        });

        setupListeners();
        loadProducts();

        return view;
    }

    private void setupListeners() {
        btnApple.setOnClickListener(v -> {
            selectedBrand = (selectedBrand == 1) ? -1 : 1;
            updateButtonHighlights();
            loadProducts();
        });

        btnSamsung.setOnClickListener(v -> {
            selectedBrand = (selectedBrand == 2) ? -1 : 2;
            updateButtonHighlights();
            loadProducts();
        });

        btnVivo.setOnClickListener(v -> {
            selectedBrand = (selectedBrand == 3) ? -1 : 3;
            updateButtonHighlights();
            loadProducts();
        });

        btnXiaomi.setOnClickListener(v -> {
            selectedBrand = (selectedBrand == 4) ? -1 : 4;
            updateButtonHighlights();
            loadProducts();
        });

        btnDienThoai.setOnClickListener(v -> {
            selectedCategory = (selectedCategory == 1) ? -1 : 1;
            updateButtonHighlights();
            loadProducts();
        });

        btnDongHo.setOnClickListener(v -> {
            selectedCategory = (selectedCategory == 2) ? -1 : 2;
            updateButtonHighlights();
            loadProducts();
        });

        etSearch.setOnEditorActionListener((TextView v, int actionId, KeyEvent event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                loadProducts();
                return true;
            }
            return false;
        });
    }

    private void loadProducts() {
        try {
            String urlWithParams = URL;
            List<String> params = new ArrayList<>();

            if (selectedBrand != -1) params.add("brand=" + selectedBrand);
            if (selectedCategory != -1) params.add("category=" + selectedCategory);

            String keyword = etSearch.getText().toString().trim();
            if (!TextUtils.isEmpty(keyword)) {
                String encodedKeyword = URLEncoder.encode(keyword, "UTF-8");
                params.add("search=" + encodedKeyword);
            }

            if (!params.isEmpty()) {
                urlWithParams += "?" + String.join("&", params);
            }

            Log.d("FILTER_URL", urlWithParams);

            StringRequest request = new StringRequest(Request.Method.GET, urlWithParams,
                    response -> {
                        try {
                            JSONArray array = new JSONArray(response);
                            allProducts.clear();
                            Log.d("PRODUCT_COUNT", "Tổng sản phẩm nhận được: " + array.length());

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject obj = array.getJSONObject(i);
                                String name = obj.getString("product_name");
                                String description = obj.getString("detail");
                                String image = obj.getString("image");
                                double rawPrice = obj.getDouble("price");
                                String price = formatCurrency(rawPrice);
                                String imageUrl = "http://10.0.2.2/android_api/upload/" + image;
                                allProducts.add(new Product(name, description, imageUrl, price));
                            }

                            currentPage = 1;
                            loadPage(currentPage);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Lỗi dữ liệu JSON", Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> {
                        Toast.makeText(getContext(), "Lỗi mạng", Toast.LENGTH_SHORT).show();
                        Log.e("VOLLEY_ERROR", error.toString());
                    });

            RequestQueue queue = Volley.newRequestQueue(getContext());
            queue.add(request);

        } catch (Exception e) {
            Toast.makeText(getContext(), "Lỗi tạo URL", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void loadPage(int page) {
        currentProducts.clear();

        int start = (page - 1) * ITEMS_PER_PAGE;
        int end = Math.min(start + ITEMS_PER_PAGE, allProducts.size());

        if (start < allProducts.size()) {
            currentProducts.addAll(allProducts.subList(start, end));
        }

        Log.d("PAGE_DEBUG", "Trang " + page + ": Hiển thị từ " + start + " đến " + (end - 1));

        adapter.notifyDataSetChanged();
        currentPage = page;
        addPaginationButtons((int) Math.ceil(allProducts.size() * 1.0 / ITEMS_PER_PAGE));
    }

    private void addPaginationButtons(int totalPages) {
        paginationLayout.removeAllViews();

        if (totalPages <= 1) return;

        Button prevButton = new Button(getContext());
        prevButton.setText("«");
        stylePageButton(prevButton);
        prevButton.setOnClickListener(v -> {
            if (currentPage > 1) loadPage(currentPage - 1);
        });
        paginationLayout.addView(prevButton);

        for (int i = 1; i <= totalPages; i++) {
            Button pageButton = new Button(getContext());
            pageButton.setText(String.valueOf(i));
            stylePageButton(pageButton);
            if (i == currentPage) pageButton.setEnabled(false);
            int finalI = i;
            pageButton.setOnClickListener(v -> loadPage(finalI));
            paginationLayout.addView(pageButton);
        }

        Button nextButton = new Button(getContext());
        nextButton.setText("»");
        stylePageButton(nextButton);
        nextButton.setOnClickListener(v -> {
            if (currentPage < totalPages) loadPage(currentPage + 1);
        });
        paginationLayout.addView(nextButton);
    }

    private void stylePageButton(Button btn) {
        btn.setTextSize(12);
        btn.setPadding(12, 6, 12, 6);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(6, 0, 6, 0);
        btn.setLayoutParams(params);
    }

    private void updateButtonHighlights() {
        updateButtonBackground(btnApple, selectedBrand == 1);
        updateButtonBackground(btnSamsung, selectedBrand == 2);
        updateButtonBackground(btnVivo, selectedBrand == 3);
        updateButtonBackground(btnXiaomi, selectedBrand == 4);
        updateButtonBackground(btnDienThoai, selectedCategory == 1);
        updateButtonBackground(btnDongHo, selectedCategory == 2);
    }

    private void updateButtonBackground(Button button, boolean isSelected) {
        if (isSelected) {
            button.setBackgroundResource(R.drawable.button_selected_bg);
            button.setTextColor(getResources().getColor(android.R.color.black));
        } else {
            button.setBackgroundResource(R.drawable.category_button_bg);
            button.setTextColor(getResources().getColor(android.R.color.white));
        }
    }

    private String formatCurrency(double price) {
        java.text.NumberFormat formatter = java.text.NumberFormat.getInstance(new java.util.Locale("vi", "VN"));
        return "Giá: " + formatter.format(price) + "đ";
    }

    @Override
    public void onProductClick(Product product) {
        ProductDetailFragment detailFragment = ProductDetailFragment.newInstance(product);
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, detailFragment)
                .addToBackStack(null)
                .commit();
    }
}
