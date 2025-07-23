package com.example.doandominhtrung_2120110322;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangePasswordFragment extends Fragment {

    private EditText etOldPass, etNewPass, etConfirmPass;
    private Button btnConfirm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        etOldPass = view.findViewById(R.id.etOldPassword);
        etNewPass = view.findViewById(R.id.etNewPassword);
        etConfirmPass = view.findViewById(R.id.etConfirmPassword);
        btnConfirm = view.findViewById(R.id.btnConfirmChange);

        SharedPreferences prefs = requireContext().getSharedPreferences("user_session", Context.MODE_PRIVATE);
        String username = prefs.getString("username", "");
        Log.d("DEBUG_USERNAME", "Username from SharedPreferences: " + username);

        btnConfirm.setOnClickListener(v -> {
            String oldPass = etOldPass.getText().toString().trim();
            String newPass = etNewPass.getText().toString().trim();
            String confirmPass = etConfirmPass.getText().toString().trim();

            if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPass.equals(confirmPass)) {
                Toast.makeText(getContext(), "Mật khẩu mới không khớp", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                JSONObject data = new JSONObject();
                data.put("username", username);
                data.put("old_password", oldPass);
                data.put("new_password", newPass);

                String url = "http://10.0.2.2/android_api/change_password.php";
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, data,
                        response -> {
                            try {
                                boolean success = response.getBoolean("success");
                                if (success) {
                                    Toast.makeText(getContext(), "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.clear();
                                    editor.apply();
                                    startActivity(new Intent(getContext(), MainActivity.class));
                                    requireActivity().finish();
                                } else {
                                    String message = response.optString("message", "Lỗi không xác định");
                                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                Toast.makeText(getContext(), "Lỗi phản hồi", Toast.LENGTH_SHORT).show();
                            }
                        },
                        error -> {
                            Toast.makeText(getContext(), "Lỗi kết nối máy chủ", Toast.LENGTH_SHORT).show();
                        }
                );
                Volley.newRequestQueue(requireContext()).add(request);
            } catch (JSONException e) {
                Toast.makeText(getContext(), "Lỗi tạo dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}

