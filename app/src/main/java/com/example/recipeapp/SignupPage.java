package com.example.recipeapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class SignupPage extends AppCompatActivity {
    EditText password, confirmPassword;
    Button goLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_page);

        password = findViewById(R.id.editTextTextPassword6);
        confirmPassword = findViewById(R.id.editTextTextPassword7);
        goLogin = findViewById(R.id.button7);

        setupPasswordToggle(password);
        setupPasswordToggle(confirmPassword);

        goLogin.setOnClickListener(v -> {
            Intent intent = new Intent(SignupPage.this, LoginPage.class);
            startActivity(intent);
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
}
