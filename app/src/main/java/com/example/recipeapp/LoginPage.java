package com.example.recipeapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class LoginPage extends AppCompatActivity {
    EditText email, password;
    Button forgotPassword, signIn, goSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        email = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);
        forgotPassword = findViewById(R.id.button);
        signIn = findViewById(R.id.button2);
        goSignup = findViewById(R.id.button5);

        setupPasswordToggle(password);

        goSignup.setOnClickListener(v -> {
            Intent intent = new Intent(LoginPage.this,SignupPage.class);
            startActivity(intent);
        });

        forgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginPage.this, ForgotPage.class);
            startActivity(intent);
        });

        signIn.setOnClickListener(v -> {
            loginUser();
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

    private void loginUser() {
        String mail = email.getText().toString().trim();
        String pass = password.getText().toString().trim();

        if(mail.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Please Fill in All Fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            Toast.makeText(this, "Invalid Email Format", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://10.0.2.2/recipeapp/login.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            if(response.contains("success")) {
                Toast.makeText(this, response, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(LoginPage.this, HomePage.class);
                startActivity(intent);
                finish();
            } else if(response.equals("email_not_found")) {
                Toast.makeText(this, "Email Not Exists", Toast.LENGTH_SHORT).show();
            } else if(response.equals("incorrect_password")) {
                Toast.makeText(this, "Incorrect Password", Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            Toast.makeText(this, "Error! " + error.getMessage(), Toast.LENGTH_LONG).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", mail);
                params.put("password", pass);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}