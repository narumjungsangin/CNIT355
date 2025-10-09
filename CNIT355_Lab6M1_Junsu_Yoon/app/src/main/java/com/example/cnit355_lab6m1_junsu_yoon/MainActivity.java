package com.example.cnit355_lab6m1_junsu_yoon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_Q1 = "q1";
    public static final String KEY_Q2 = "q2";
    public static final String KEY_A1 = "a1";
    public static final String KEY_A2 = "a2";

    private TextView tvQ1Label, tvQ2Label, tvA1Below, tvA2Below, tvNote;
    private EditText etA1InField, etA2InField;

    private static final String QUESTION_1 = "What is your name?";
    private static final String QUESTION_2 = "What is your favorite color?";

    private final ActivityResultLauncher<Intent> askLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();

                    String q1 = data.getStringExtra(KEY_Q1);
                    String q2 = data.getStringExtra(KEY_Q2);
                    if (q1 != null) tvQ1Label.setText(q1);
                    if (q2 != null) tvQ2Label.setText(q2);

                    String a1 = data.getStringExtra(KEY_A1);
                    String a2 = data.getStringExtra(KEY_A2);
                    Toast.makeText(this, "A1=[" + a1 + "] A2=[" + a2 + "]", Toast.LENGTH_SHORT).show();

                    // ⬇️ A1·A2를 둘 다 아래 TextView에 표시
                    tvA1Below.setText(a1 != null ? a1 : "");
                    tvA2Below.setText(a2 != null ? a2 : "");

                    // 입력칸은 시각적으로 비워두기(선택)
                    etA1InField.setText("");
                    etA2InField.setText("");

                    tvNote.setVisibility(View.VISIBLE);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvQ1Label   = findViewById(R.id.tvQ1Label);
        tvQ2Label   = findViewById(R.id.tvQ2Label);
        etA1InField = findViewById(R.id.etA1InField);
        etA2InField = findViewById(R.id.etA2InField);
        tvA1Below   = findViewById(R.id.tvA1Below);   // ✅ 새로 바인딩
        tvA2Below   = findViewById(R.id.tvA2Below);
        tvNote      = findViewById(R.id.tvNote);
        Button btnAsk = findViewById(R.id.btnAsk);

        tvQ1Label.setText(QUESTION_1);
        tvQ2Label.setText(QUESTION_2);
        tvNote.setVisibility(View.GONE);

        btnAsk.setOnClickListener(v -> {
            Intent i = new Intent(this, SecondActivity.class);
            i.putExtra(KEY_Q1, QUESTION_1);
            i.putExtra(KEY_Q2, QUESTION_2);
            askLauncher.launch(i);
        });
    }
}
