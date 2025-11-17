package com.example.cnit355_lab9m2_junsu_yoon;

import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int REQ_CONTACTS = 2001;
    private EditText etName, etNumber;
    private TextView tvResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.etName);
        etNumber = findViewById(R.id.etNumber);
        tvResults = findViewById(R.id.tvResults);
        Button btnAdd = findViewById(R.id.btnAdd);

        checkAndRequestPermissions();

        btnAdd.setOnClickListener(v -> {
            if (!hasPermissions()) {
                checkAndRequestPermissions();
                return;
            }

            String name = etName.getText().toString().trim();
            String number = etNumber.getText().toString().trim();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(number)) {
                Toast.makeText(this, "Enter name and number", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean success = insertContact(name, number);
            if (success) {
                Toast.makeText(this, "New contact added!", Toast.LENGTH_SHORT).show();
                etName.setText("");
                etNumber.setText("");
                displayContacts();
            } else {
                Toast.makeText(this, "Failed to add contact", Toast.LENGTH_SHORT).show();
            }
        });

        displayContacts(); // 앱 실행 시 기존 연락처 표시
    }

    /** 연락처 추가 **/
    private boolean insertContact(String name, String number) {
        try {
            ArrayList<ContentProviderOperation> ops = new ArrayList<>();

            // 새 RawContact
            ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                    .build());

            // 이름
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)
                    .build());

            // 번호
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, number)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                    .build());

            getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            return true;
        } catch (RemoteException | OperationApplicationException | SecurityException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** 연락처 읽기 **/
    private void displayContacts() {
        if (!hasPermissions()) {
            tvResults.setText("(Permission required)");
            return;
        }

        ContentResolver cr = getContentResolver();
        Cursor c = cr.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                },
                null, null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        );

        if (c == null) {
            tvResults.setText("(No data)");
            return;
        }

        StringBuilder sb = new StringBuilder();
        if (c.moveToFirst()) {
            do {
                String name = c.getString(c.getColumnIndexOrThrow(
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phone = c.getString(c.getColumnIndexOrThrow(
                        ContactsContract.CommonDataKinds.Phone.NUMBER));
                sb.append("Name: ").append(name)
                        .append("  Numbers: ").append(phone).append("\n");
            } while (c.moveToNext());
        } else {
            sb.append("(No contacts)");
        }
        c.close();

        tvResults.setText(sb.toString());
    }

    /** 권한 체크 **/
    private boolean hasPermissions() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS)
                        == PackageManager.PERMISSION_GRANTED;
    }

    private void checkAndRequestPermissions() {
        if (!hasPermissions()) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS},
                    REQ_CONTACTS
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_CONTACTS) {
            if (hasPermissions()) displayContacts();
            else Toast.makeText(this, "Contacts permission denied", Toast.LENGTH_SHORT).show();
        }
    }
}
