//package com.example.doandominhtrung_2120110322;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.inputmethod.EditorInfo;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.toolbox.JsonArrayRequest;
//import com.android.volley.toolbox.Volley;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.net.URLEncoder;
//import java.util.ArrayList;
//import java.util.List;
//
//public class HomeActivity extends AppCompatActivity {
//
//    private RecyclerView recyclerView;
//    private ProductAdapter adapter;
//    private List<Product> productList;
//    private static final String URL = "http://10.0.2.2/android_api/get_products.php";
//    private EditText etSearch;
//    private Button btnApple, btnSamsung, btnVivo, btnXiaomi;
//    private Button btnDienThoai, btnDongHo;
//    private int selectedBrand = -1;
//    private int selectedCategory = -1;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_home);
//
//        Button btn = findViewById(R.id.btnBack);
//        btn.setOnClickListener(v -> {
//            startActivity(new Intent(getApplicationContext(), MainActivity.class));
//        });
//
//        etSearch = findViewById(R.id.etSearch);
//
//        btnApple = findViewById(R.id.btnApple);
//        btnSamsung = findViewById(R.id.btnSamsung);
//        btnVivo = findViewById(R.id.btnVivo);
//        btnXiaomi = findViewById(R.id.btnXiaomi);
//
//        btnDienThoai = findViewById(R.id.btnDienThoai);
//        btnDongHo = findViewById(R.id.btnDongHo);
//
//        // BRAND buttons
//        btnApple.setOnClickListener(v -> {
//            selectedBrand = (selectedBrand == 1) ? -1 : 1;
//            updateButtonHighlights();
//            loadProducts();
//        });
//        btnSamsung.setOnClickListener(v -> {
//            selectedBrand = (selectedBrand == 2) ? -1 : 2;
//            updateButtonHighlights();
//            loadProducts();
//        });
//        btnVivo.setOnClickListener(v -> {
//            selectedBrand = (selectedBrand == 3) ? -1 : 3;
//            updateButtonHighlights();
//            loadProducts();
//        });
//        btnXiaomi.setOnClickListener(v -> {
//            selectedBrand = (selectedBrand == 4) ? -1 : 4;
//            updateButtonHighlights();
//            loadProducts();
//        });
//
//        // CATEGORY buttons
//        btnDienThoai.setOnClickListener(v -> {
//            selectedCategory = (selectedCategory == 1) ? -1 : 1;
//            updateButtonHighlights();
//            loadProducts();
//        });
//        btnDongHo.setOnClickListener(v -> {
//            selectedCategory = (selectedCategory == 2) ? -1 : 2;
//            updateButtonHighlights();
//            loadProducts();
//        });
//
//        recyclerView = findViewById(R.id.recyclerProducts);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        productList = new ArrayList<>();
//        adapter = new ProductAdapter(this, productList);
//        recyclerView.setAdapter(adapter);
//
//        // Tìm kiếm realtime
//        etSearch.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                loadProducts();
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {}
//        });
//
//        // Tìm kiếm khi nhấn Enter
//        etSearch.setOnEditorActionListener((TextView v, int actionId, android.view.KeyEvent event) -> {
//            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
//                loadProducts();
//                return true;
//            }
//            return false;
//        });
//
//        // Tải sản phẩm ban đầu
//        loadProducts();
//    }
//
//    private void loadProducts() {
//        try {
//            String urlWithParams = URL;
//            List<String> params = new ArrayList<>();
//
//            if (selectedBrand != -1) {
//                params.add("brand=" + selectedBrand);
//            }
//
//            if (selectedCategory != -1) {
//                params.add("category=" + selectedCategory);
//            }
//
//            String keyword = etSearch.getText().toString().trim();
//            if (!keyword.isEmpty()) {
//                String encodedKeyword = URLEncoder.encode(keyword, "UTF-8");
//                params.add("search=" + encodedKeyword);
//            }
//
//            if (!params.isEmpty()) {
//                urlWithParams += "?" + String.join("&", params);
//            }
//
//            Log.d("FILTER_URL", urlWithParams);
//
//            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, urlWithParams, null,
//                    response -> {
//                        try {
//                            productList.clear();
//                            for (int i = 0; i < response.length(); i++) {
//                                JSONObject obj = response.getJSONObject(i);
//
//                                String name = obj.getString("product_name");
//                                String description = obj.getString("detail");
//                                String image = obj.getString("image");
//                                double rawPrice = obj.getDouble("price");
//                                String price = formatCurrency(rawPrice);
//
//                                String imageUrl = "http://10.0.2.2/android_api/upload/" + image;
//
//                                productList.add(new Product(name, description, imageUrl, price));
//                            }
//                            adapter.notifyDataSetChanged();
//
//                            if (productList.isEmpty()) {
//                                Toast.makeText(this, "Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
//                            }
//                        } catch (Exception e) {
//                            Toast.makeText(this, "Lỗi dữ liệu", Toast.LENGTH_SHORT).show();
//                        }
//                    },
//                    error -> {
//                        Toast.makeText(this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
//                        Log.e("VOLLEY_ERROR", error.toString());
//                    }
//            );
//
//            RequestQueue queue = Volley.newRequestQueue(this);
//            queue.add(request);
//
//        } catch (Exception e) {
//            Toast.makeText(this, "Lỗi tạo URL", Toast.LENGTH_SHORT).show();
//            e.printStackTrace();
//        }
//    }
//
//    private void updateButtonHighlights() {
//        updateButtonBackground(btnApple, selectedBrand == 1);
//        updateButtonBackground(btnSamsung, selectedBrand == 2);
//        updateButtonBackground(btnVivo, selectedBrand == 3);
//        updateButtonBackground(btnXiaomi, selectedBrand == 4);
//
//        updateButtonBackground(btnDienThoai, selectedCategory == 1);
//        updateButtonBackground(btnDongHo, selectedCategory == 2);
//    }
//
//    private void updateButtonBackground(Button button, boolean isSelected) {
//        if (isSelected) {
//            button.setBackgroundResource(R.drawable.button_selected_bg);
//            button.setTextColor(getResources().getColor(android.R.color.black));
//        } else {
//            button.setBackgroundResource(R.drawable.category_button_bg);
//            button.setTextColor(getResources().getColor(android.R.color.white));
//        }
//    }
//
//    private String formatCurrency(double price) {
//        java.text.NumberFormat formatter = java.text.NumberFormat.getInstance(new java.util.Locale("vi", "VN"));
//        return "Giá: " + formatter.format(price) + "đ";
//    }
//}
