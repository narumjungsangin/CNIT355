package com.example.cnit355_lab6m1_junsu_yoon;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    private TextView tvRecvQ1, tvRecvQ2;
    private EditText etA1, etA2;

    private String q1, q2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        tvRecvQ1 = findViewById(R.id.tvRecvQ1);
        tvRecvQ2 = findViewById(R.id.tvRecvQ2);
        etA1     = findViewById(R.id.etA1);
        etA2     = findViewById(R.id.etA2);
        Button btnSend = findViewById(R.id.btnSendAnswers);

        // MainActivity에서 온 질문 표시
        Intent in = getIntent();
        q1 = in.getStringExtra(MainActivity.KEY_Q1);
        q2 = in.getStringExtra(MainActivity.KEY_Q2);
        tvRecvQ1.setText(q1 != null ? q1 : "");
        tvRecvQ2.setText(q2 != null ? q2 : "");

        // 답변 돌려주기
        btnSend.setOnClickListener(v -> {
            Intent back = new Intent();
            back.putExtra(MainActivity.KEY_Q1, q1);
            back.putExtra(MainActivity.KEY_Q2, q2);
            back.putExtra(MainActivity.KEY_A1, etA1.getText().toString());
            back.putExtra(MainActivity.KEY_A2, etA2.getText().toString());
            setResult(RESULT_OK, back);
            finish();
        });
    }
}
