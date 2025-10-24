package com.example.lab4m1;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    int num = ((int) (Math.random() * 6)) + 1;
    ImageView robberImage;
    ImageView copImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        robberImage = findViewById(R.id.imageView);
        copImage    = findViewById(R.id.imageView2);
        robberImage.setVisibility(View.GONE);
        copImage.setVisibility(View.GONE);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    public void ClickMethod(View view){
        int id = view.getId();
        copImage.setVisibility(View.GONE);
        robberImage.setVisibility(View.GONE);

        if (id == R.id.button1) {
            if (num == 1) {
                Toast.makeText(getApplicationContext(), "Robber is caught!", Toast.LENGTH_SHORT).show();
                robberImage.setVisibility(View.GONE);
                copImage.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(getApplicationContext(), "You missed the robber!", Toast.LENGTH_SHORT).show();
                robberImage.setVisibility(View.VISIBLE);
                copImage.setVisibility(View.GONE);
            }

        } else if (id == R.id.button2) {
            if (num == 2) {
                Toast.makeText(getApplicationContext(), "Robber is caught!", Toast.LENGTH_SHORT).show();
                robberImage.setVisibility(View.GONE);
                copImage.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(getApplicationContext(), "You missed the robber!", Toast.LENGTH_SHORT).show();
                robberImage.setVisibility(View.VISIBLE);
                copImage.setVisibility(View.GONE);
            }

        } else if (id == R.id.button3) {
            if (num == 3) {
                Toast.makeText(getApplicationContext(), "Robber is caught!", Toast.LENGTH_SHORT).show();
                robberImage.setVisibility(View.GONE);
                copImage.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(getApplicationContext(), "You missed the robber!", Toast.LENGTH_SHORT).show();
                robberImage.setVisibility(View.VISIBLE);
                copImage.setVisibility(View.GONE);
            }

        } else if (id == R.id.button4) {
            if (num == 4) {
                Toast.makeText(getApplicationContext(), "Robber is caught!", Toast.LENGTH_SHORT).show();
                robberImage.setVisibility(View.GONE);
                copImage.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(getApplicationContext(), "You missed the robber!", Toast.LENGTH_SHORT).show();
                robberImage.setVisibility(View.VISIBLE);
                copImage.setVisibility(View.GONE);
            }

        } else if (id == R.id.button5) {
            if (num == 5) {
                Toast.makeText(getApplicationContext(), "Robber is caught!", Toast.LENGTH_SHORT).show();
                robberImage.setVisibility(View.GONE);
                copImage.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(getApplicationContext(), "You missed the robber!", Toast.LENGTH_SHORT).show();
                robberImage.setVisibility(View.VISIBLE);
                copImage.setVisibility(View.GONE);
            }

        } else if (id == R.id.button6) {
            if (num == 6) {
                Toast.makeText(getApplicationContext(), "Robber is caught!", Toast.LENGTH_SHORT).show();
                robberImage.setVisibility(View.GONE);
                copImage.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(getApplicationContext(), "You missed the robber!", Toast.LENGTH_SHORT).show();
                robberImage.setVisibility(View.VISIBLE);
                copImage.setVisibility(View.GONE);
            }
        }
    }
}