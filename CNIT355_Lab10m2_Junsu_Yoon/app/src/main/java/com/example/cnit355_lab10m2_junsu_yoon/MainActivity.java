package com.example.cnit355_lab10m2_junsu_yoon;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText etName, etPhone, etId;
    TextView tvResult;
    Button btnInsert, btnSearch, btnDelete;

    Uri CONTENT_URI = MyProvider.CONTENT_URI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etId = findViewById(R.id.etId);
        tvResult = findViewById(R.id.tvResult);

        btnInsert = findViewById(R.id.btnInsert);
        btnSearch = findViewById(R.id.btnSearch);
        btnDelete = findViewById(R.id.btnDelete);

        btnInsert.setOnClickListener(v -> doInsert());
        btnSearch.setOnClickListener(v -> doSearch());
        btnDelete.setOnClickListener(v -> doDelete());
    }

    private void doInsert() {
        ContentValues values = new ContentValues();
        values.put("name", etName.getText().toString());
        values.put("phone", etPhone.getText().toString());

        getContentResolver().insert(CONTENT_URI, values);
        Toast.makeText(this, "Inserted", Toast.LENGTH_SHORT).show();
    }

    private void doSearch() {
        Cursor c = getContentResolver().query(
                CONTENT_URI,
                null, null, null, "_id ASC"
        );

        StringBuilder sb = new StringBuilder();
        if (c.moveToFirst()) {
            do {
                sb.append("ID: ").append(c.getInt(0))
                        .append(" Name: ").append(c.getString(1))
                        .append(" Phone: ").append(c.getString(2))
                        .append("\n");
            } while (c.moveToNext());
        }
        c.close();
        tvResult.setText(sb.toString());
    }

    private void doDelete() {
        String id = etId.getText().toString();
        getContentResolver().delete(
                Uri.withAppendedPath(CONTENT_URI, id),
                null, null
        );
        Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
    }
}
