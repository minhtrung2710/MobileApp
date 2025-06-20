package com.example.doandominhtrung_2120110322;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btn = findViewById(R.id.btnLogin);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText objPhone =findViewById(R.id.txtPhone);
                EditText objPassword = findViewById(R.id.txtPassword);
                String txtPhone = objPhone.getText().toString();
                String txtPassword = objPassword.getText().toString();
                if (txtPhone.equals("0773656168") && txtPassword.equals("12345"))
                {
                    Intent it = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(it);
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Login fail", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(it);
            }
        });
    }
}