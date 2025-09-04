package com.example.lab1;

import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void onButton2Clicked(View view) {
        //display in short period of time
        Toast.makeText(getApplicationContext(), "Button Clicked", Toast.LENGTH_SHORT).show();
    }
    public void onButton3Clicked(View view)
    {
        //launch web browser
        Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.purdue.edu"));
        startActivity(mIntent);
    }
    public void onButton4Clicked(View view)
    {
        //launch dialer
        Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:765-123-4567"));
        startActivity(mIntent);
    }

    public void onButton5Clicked(View view)
    {
        //launch dialer
        Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:40.427890,-86.911198"));
        startActivity(mIntent);
    }
}

