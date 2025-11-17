package com.example.cnit355_lab10m1_junsu_yoon;

import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private DBHelper dbHelper;

    private EditText etId, etName, etPhone, etEmail;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);

        etId    = findViewById(R.id.etId);
        etName  = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        tvResult = findViewById(R.id.tvResult);

        Button btnInsert = findViewById(R.id.btnInsert);
        Button btnUpdate = findViewById(R.id.btnUpdate);
        Button btnDelete = findViewById(R.id.btnDelete);
        Button btnView   = findViewById(R.id.btnView);

        btnInsert.setOnClickListener(v -> doInsert());
        btnUpdate.setOnClickListener(v -> doUpdate());
        btnDelete.setOnClickListener(v -> doDelete());
        btnView.setOnClickListener(v -> showAll());
    }

    private void doInsert() {
        String name  = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Name is required", Toast.LENGTH_SHORT).show();
            return;
        }

        long rowId = dbHelper.insertContact(name, phone, email);
        if (rowId == -1) {
            Toast.makeText(this, "Insert failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Insert success. New id = " + rowId, Toast.LENGTH_SHORT).show();
            clearInputsExceptId();
            showAll();
        }
    }

    private void doUpdate() {
        String idStr  = etId.getText().toString().trim();
        String name   = etName.getText().toString().trim();
        String phone  = etPhone.getText().toString().trim();
        String email  = etEmail.getText().toString().trim();

        if (TextUtils.isEmpty(idStr)) {
            Toast.makeText(this, "ID is required for update", Toast.LENGTH_SHORT).show();
            return;
        }

        long id;
        try {
            id = Long.parseLong(idStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid ID", Toast.LENGTH_SHORT).show();
            return;
        }

        int count = dbHelper.updateContact(id, name, phone, email);
        Toast.makeText(this, "Updated rows: " + count, Toast.LENGTH_SHORT).show();
        showAll();
    }

    private void doDelete() {
        String idStr = etId.getText().toString().trim();
        if (TextUtils.isEmpty(idStr)) {
            Toast.makeText(this, "ID is required for delete", Toast.LENGTH_SHORT).show();
            return;
        }

        long id;
        try {
            id = Long.parseLong(idStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid ID", Toast.LENGTH_SHORT).show();
            return;
        }

        int count = dbHelper.deleteContact(id);
        Toast.makeText(this, "Deleted rows: " + count, Toast.LENGTH_SHORT).show();
        showAll();
    }

    private void showAll() {
        Cursor c = dbHelper.getAllContacts();
        if (c == null) {
            tvResult.setText("No data (cursor null)");
            return;
        }

        StringBuilder sb = new StringBuilder();
        if (c.moveToFirst()) {
            do {
                long id = c.getLong(c.getColumnIndexOrThrow(DBHelper.COL_ID));
                String name  = c.getString(c.getColumnIndexOrThrow(DBHelper.COL_NAME));
                String phone = c.getString(c.getColumnIndexOrThrow(DBHelper.COL_PHONE));
                String email = c.getString(c.getColumnIndexOrThrow(DBHelper.COL_EMAIL));

                sb.append("ID: ").append(id)
                        .append("  Name: ").append(name)
                        .append("  Phone: ").append(phone)
                        .append("  Email: ").append(email)
                        .append("\n");
            } while (c.moveToNext());
        } else {
            sb.append("(no rows)");
        }
        c.close();

        tvResult.setText(sb.toString());
    }

    private void clearInputsExceptId() {
        etName.setText("");
        etPhone.setText("");
        etEmail.setText("");
    }
}
