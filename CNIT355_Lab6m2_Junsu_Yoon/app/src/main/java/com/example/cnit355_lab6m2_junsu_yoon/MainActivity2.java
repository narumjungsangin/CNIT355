package com.example.cnit355_lab6m2_junsu_yoon;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);  // 레이아웃 파일명과 매칭

        Button back = findViewById(R.id.btnBack);
        back.setOnClickListener(v -> finish());
    }
}
