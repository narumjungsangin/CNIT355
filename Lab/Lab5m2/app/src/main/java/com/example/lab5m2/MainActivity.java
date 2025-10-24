package com.example.lab5m2;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;

    int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView1 = (ImageView) findViewById(R.id.imageView1);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        imageView3 = (ImageView) findViewById(R.id.imageView3);
    }

    public void onClick(View view){
        if (index == 0){
            imageView1.setVisibility(View.VISIBLE);
            imageView2.setVisibility(View.GONE);
            imageView3.setVisibility(View.GONE);
        } else if (index == 1){
            imageView1.setVisibility(View.GONE);
            imageView2.setVisibility(View.VISIBLE);
            imageView3.setVisibility(View.GONE);
        } else if (index == 2){
            imageView1.setVisibility(View.GONE);
            imageView2.setVisibility(View.GONE);
            imageView3.setVisibility(View.VISIBLE);
        }

        index++;
        if (index > 2) {
            index = 0;
        }
    }
}
