package com.example.cnit355_lab3m1_junsu_yoon;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editTextText);
        Button button = findViewById(R.id.button);

        editText.setText("Firstname Lastname");
        editText.setTextColor(Color.GRAY);

        button.setOnClickListener(v -> {
            editText.setTextColor(Color.BLACK);
        });
    }
}
