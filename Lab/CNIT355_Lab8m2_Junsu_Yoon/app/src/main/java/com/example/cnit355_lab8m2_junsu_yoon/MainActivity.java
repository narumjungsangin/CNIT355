package com.example.cnit355_lab8m2_junsu_yoon;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    private static final int REQ_EXTERNAL = 1001;

    private EditText etData;
    private Button btnWriteSDFile, btnReadSDFile, btnClear;

    // 노트: API<29 vs API≥29 경로
    private String getMySdPath() {
        if (Build.VERSION.SDK_INT >= 29) {
            File dir = getExternalFilesDir(null); // 앱 전용 외부저장소(권장)
            return (dir != null) ? dir.getAbsolutePath()
                    : Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
    }

    // 노트: 퍼미션 요청 예시(28↓)
    private boolean ensurePermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= 29) return true; // 10+는 앱 전용 폴더 쓰면 퍼미션 불필요
        boolean writeGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
        boolean readGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
        if (!writeGranted || !readGranted) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQ_EXTERNAL);
            return false;
        }
        return true;
    }

    private final String FILE_NAME = "mysdfile.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etData = findViewById(R.id.etData);
        btnWriteSDFile = findViewById(R.id.btnWriteSDFile);
        btnReadSDFile = findViewById(R.id.btnReadSDFile);
        btnClear = findViewById(R.id.btnClear);

        btnWriteSDFile.setOnClickListener(v -> {
            if (!ensurePermissionIfNeeded()) return;

            String mySdPath = getMySdPath();

            try {
                File dir = new File(mySdPath);
                if (!dir.exists()) dir.mkdirs();

                File myFile = new File(dir, FILE_NAME);
                OutputStreamWriter myOutWriter =
                        new OutputStreamWriter(new FileOutputStream(myFile));myOutWriter.append(etData.getText());
                myOutWriter.close();

                Toast.makeText(getBaseContext(),
                        "Done writing SD '" + FILE_NAME + "'",
                        Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Read
        btnReadSDFile.setOnClickListener(v -> {
            if (!ensurePermissionIfNeeded()) return;

            String mySdPath = getMySdPath();
            try {
                File myFile = new File(mySdPath, FILE_NAME);
                if (!myFile.exists()) {
                    Toast.makeText(getApplicationContext(),
                            "File not found: " + myFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                    return;
                }

                BufferedReader myReader = new BufferedReader(
                        new InputStreamReader(new FileInputStream(myFile))
                );
                String aDataRow;
                StringBuilder aBuffer = new StringBuilder();
                while ((aDataRow = myReader.readLine()) != null) {
                    aBuffer.append(aDataRow).append("\n");
                }
                myReader.close();

                etData.setText(aBuffer.toString());

                Toast.makeText(getApplicationContext(),
                        "Done reading SD '" + FILE_NAME + "'", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Clear
        btnClear.setOnClickListener(v -> etData.setText(""));
    }


}
