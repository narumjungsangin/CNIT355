package com.example.programmingasignment1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

//Author: Junsu Yoon, Oct 24, CNIT 355
//Backend for help page

public class activity_help extends AppCompatActivity {

    private TextView tvPhoneNumber;
    private Button btnCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        btnCall = findViewById(R.id.btnCall);

        // 전화 버튼 클릭 시 다이얼러 열기
        btnCall.setOnClickListener(v -> {
            String phone = tvPhoneNumber.getText().toString().replace(" ", "");
            Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
            startActivity(dialIntent);
        });

        // 상단 탭 아이콘 참조
        ImageView imgLength = findViewById(R.id.imageLength);
        ImageView imgTemperature = findViewById(R.id.imageTemperature);
        ImageView imgWeight = findViewById(R.id.imageWeight);
        ImageView imgHelp = findViewById(R.id.imageHelp);

        // Length 탭 → MainActivity로 이동 + length 모드로
        imgLength.setOnClickListener(v -> {
            Intent intent = new Intent(activity_help.this, MainActivity.class);
            intent.putExtra("mode", "length");
            startActivity(intent);
            finish();
        });

        // Temperature 탭 → MainActivity로 이동 + temperature 모드로
        imgTemperature.setOnClickListener(v -> {
            Intent intent = new Intent(activity_help.this, MainActivity.class);
            intent.putExtra("mode", "temperature");
            startActivity(intent);
            finish();
        });

        // Weight 탭 → MainActivity로 이동 + weight 모드로
        imgWeight.setOnClickListener(v -> {
            Intent intent = new Intent(activity_help.this, MainActivity.class);
            intent.putExtra("mode", "weight");
            startActivity(intent);
            finish();
        });

        // Help 탭은 자기 자신이라 아무 동작 안 함
        imgHelp.setOnClickListener(v -> {
            // do nothing (이미 현재 화면)
        });
    }
}
