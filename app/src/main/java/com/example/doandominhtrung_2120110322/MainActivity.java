package com.example.doandominhtrung_2120110322;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private EditText txtLoginEmail, txtLoginPassword;
    private Button btnLogin, btnGoToRegister;

    private final String loginUrl = "http://10.0.2.2/android_api/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtLoginEmail = findViewById(R.id.txtEmail);
        txtLoginPassword = findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoToRegister = findViewById(R.id.btnRegister);

        btnGoToRegister.setOnClickListener(v -> {
            Intent it = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(it);
            finish();
        });

        btnLogin.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        String email = txtLoginEmail.getText().toString().trim();
        String password = txtLoginPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            JSONObject user = new JSONObject();
            user.put("username", email);
            user.put("password", password);

            JsonObjectRequest jsonRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    loginUrl,
                    user,
                    response -> {
                        Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                        // LƯU TRẠNG THÁI ĐĂNG NHẬP
                        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("isLoggedIn", true);
                        editor.apply();

                        // CHUYỂN QUA HOME
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish(); // Kết thúc màn hình login, không cho quay lại
                    },
                    error -> {
                        error.printStackTrace();
                        Toast.makeText(this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_LONG).show();
                    }
            );

            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(jsonRequest);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi gửi yêu cầu đăng nhập", Toast.LENGTH_SHORT).show();
        }
    }
}
