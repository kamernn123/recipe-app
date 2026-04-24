package com.example.recipeapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class SignupPage extends AppCompatActivity {
    EditText username, email, password, confirmPassword;
    Button goLogin, createAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_page);

        username = findViewById(R.id.editTextTextEmailAddress8);
        email = findViewById(R.id.editTextTextEmailAddress9);
        password = findViewById(R.id.editTextTextPassword6);
        confirmPassword = findViewById(R.id.editTextTextPassword7);
        createAccount = findViewById(R.id.button6);
        goLogin = findViewById(R.id.button7);

        setupPasswordToggle(password);
        setupPasswordToggle(confirmPassword);

        goLogin.setOnClickListener(v -> {
            Intent intent = new Intent(SignupPage.this, LoginPage.class);
            startActivity(intent);
        });

        createAccount.setOnClickListener(v -> {
            registerUser();
        });
    }

    private void setupPasswordToggle(EditText editText) {
        final boolean[] isVisible = {false};

        editText.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_UP) {
                if(editText.getCompoundDrawables()[2] != null && event.getRawX() >= (editText.getRight() - editText.getCompoundDrawables()[2].getBounds().width() - 20)) {
                    if(isVisible[0]) {
                        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        editText.setCompoundDrawablesWithIntrinsicBounds(
                                editText.getCompoundDrawables()[0],
                                null,
                                getDrawable(R.drawable.baseline_visibility_24),
                                null
                        );
                        isVisible[0] = false;
                    } else {
                        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        editText.setCompoundDrawablesWithIntrinsicBounds(
                                editText.getCompoundDrawables()[0],
                                null,
                                getDrawable(R.drawable.baseline_visibility_off_24),
                                null
                        );
                        isVisible[0] = true;
                    }
                    editText.setSelection(editText.length());
                    return true;
                }
            }
            return false;
        });
    }

    private void registerUser() {
        String user = username.getText().toString().trim();
        String mail = email.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String confirmPass = confirmPassword.getText().toString().trim();

        if(user.isEmpty() || mail.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {
            Toast.makeText(this, "Please Fill in all Fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            Toast.makeText(this, "Invaild Email Format", Toast.LENGTH_SHORT).show();
            return;
        }

        if(pass.length() < 6) {
            Toast.makeText(this, "Password must at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!pass.equals(confirmPass)) {
            Toast.makeText(this, "Passwords Do Not Match", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://10.0.2.2/recipeapp/signup.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            Toast.makeText(this, response, Toast.LENGTH_LONG).show();

            if(response.contains("success")) {
                Intent intent = new Intent(SignupPage.this, LoginPage.class);
                startActivity(intent);
                finish();
            }
        }, error -> {
            Toast.makeText(this, "Error! " + error.getMessage(), Toast.LENGTH_LONG).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", user);
                params.put("email", mail);
                params.put("password", pass);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}
