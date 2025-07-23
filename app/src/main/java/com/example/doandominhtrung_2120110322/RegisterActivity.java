package com.example.doandominhtrung_2120110322;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class RegisterActivity extends AppCompatActivity {

    private EditText txtEmail, txtPassword, txtRePassword;
    private Button btnConfirmRegister, btnRegisterBack;

    // Địa chỉ PHP API đăng ký
    private final String registerUrl = "http://10.0.2.2/android_api/register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtEmail = findViewById(R.id.txtRegisterEmail);
        txtPassword = findViewById(R.id.txtRegisterPassword);
        txtRePassword = findViewById(R.id.txtRegisterRePassword);
        btnConfirmRegister = findViewById(R.id.btnConfirmRegister);
        btnRegisterBack = findViewById(R.id.btnRegisterBack);

        btnRegisterBack.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        });

        btnConfirmRegister.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String email = txtEmail.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();
        String rePassword = txtRePassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty() || rePassword.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(rePassword)) {
            Toast.makeText(this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d("REGISTER_DEBUG", "Email: " + email + ", Pass: " + password);

        try {
            JSONObject user = new JSONObject();
            user.put("username", email);  // Gửi username là email
            user.put("password", password);

            JsonObjectRequest jsonRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    registerUrl,
                    user,
                    response -> {
                        Log.d("REGISTER_RESPONSE", "Response: " + response.toString());
                        Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    },
                    error -> {
                        Log.e("REGISTER_ERROR", "Volley Error: " + error.toString());
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            String errorMsg = new String(error.networkResponse.data);
                            Log.e("REGISTER_ERROR_DETAIL", errorMsg);
                        }
                        Toast.makeText(this, "Lỗi đăng ký hoặc tài khoản đã tồn tại", Toast.LENGTH_LONG).show();
                    }
            );


            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(jsonRequest);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi gửi dữ liệu", Toast.LENGTH_SHORT).show();
        }
    }
}
