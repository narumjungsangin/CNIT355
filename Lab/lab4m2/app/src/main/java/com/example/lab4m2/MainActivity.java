package com.example.lab4m2;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Spinner spinner;
    TextView textView;
    EditText editText;
    String[] baseballTeams = {"Pirates", "Cubs", "Reds", "Cardinals", "Dodgers", "Twins"};
    String selected = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = findViewById(R.id.spinner);
        textView = findViewById(R.id.textView);
        editText = findViewById(R.id.editTextText);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, baseballTeams);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected = baseballTeams[position];
                textView.setText("Your selected team is: " + selected);
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals(selected) && !s.toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Same Text!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }
}
