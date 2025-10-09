package com.example.cnit355_lab6m2_junsu_yoon;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText etId, etPw;
    private ImageView imgBanner;
    private android.view.View root;

    private enum LastAction { NONE, WEB, CALL, NEW }
    private LastAction last = LastAction.NONE;

    private final ActivityResultLauncher<Intent> newLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), r -> {
                clearInputs();
                root.setBackgroundColor(Color.parseColor("#6E6E6E")); // 회색
                imgBanner.setImageResource(R.drawable.banner4);
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        root      = findViewById(R.id.root);
        etId      = findViewById(R.id.etId);
        etPw      = findViewById(R.id.etPw);
        imgBanner = findViewById(R.id.imgBanner);

        Button btnWeb  = findViewById(R.id.btnWeb);
        Button btnCall = findViewById(R.id.btnCall);
        Button btnNew  = findViewById(R.id.btnNew);

        btnWeb.setOnClickListener(v -> {
            last = LastAction.WEB;
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.purdue.edu/")));
        });

        btnCall.setOnClickListener(v -> {
            last = LastAction.CALL;
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:7651234567")));
        });

        btnNew.setOnClickListener(v -> {
            last = LastAction.NEW;
            newLauncher.launch(new Intent(this, MainActivity2.class));

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (last == LastAction.WEB) {
            clearInputs();
            root.setBackgroundColor(Color.parseColor("#FF00FF"));
            imgBanner.setImageResource(R.drawable.banner2);
            last = LastAction.NONE;
        } else if (last == LastAction.CALL) {
            clearInputs();
            root.setBackgroundColor(Color.parseColor("#00E5FF")); // 시안
            imgBanner.setImageResource(R.drawable.banner3);
            last = LastAction.NONE;
        }
    }

    private void clearInputs() {
        etId.setText("");
        etPw.setText("");
    }
}
