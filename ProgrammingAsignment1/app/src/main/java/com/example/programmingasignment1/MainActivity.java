package com.example.programmingasignment1;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText etInput;
    private Spinner spFrom;
    private TableLayout tableResults;

    private String currentMode = "length"; // length | weight | temperature | help

    // 길이(기준: m), 무게(기준: g)
    private final Map<String, Double> LENGTH_TO_M = new LinkedHashMap<>();
    private final Map<String, Double> WEIGHT_TO_G = new LinkedHashMap<>();
    // 온도는 기준 맵이 아니라 단위 목록만 사용
    private final String[] TEMP_UNITS = new String[]{"°C", "°F", "K"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etInput = findViewById(R.id.etInput);
        spFrom = findViewById(R.id.spFrom);
        tableResults = findViewById(R.id.tableResults);

        ImageView imgLength = findViewById(R.id.imageLength);
        ImageView imgTemperature = findViewById(R.id.imageTemperature);
        ImageView imgWeight = findViewById(R.id.imageWeight);
        ImageView imgHelp = findViewById(R.id.imageHelp);

        initUnitMaps();

        // 입력 변경 → 업데이트
        etInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            @Override public void onTextChanged(CharSequence s, int st, int b, int c) { updateResults(); }
            @Override public void afterTextChanged(Editable s) {}
        });

        // 스피너 변경 → 업데이트
        spFrom.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) { updateResults(); }
            @Override public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        // 탭 클릭 → 모드 전환
        imgLength.setOnClickListener(v -> setMode("length", imgLength));
        imgTemperature.setOnClickListener(v -> setMode("temperature", imgTemperature));
        imgWeight.setOnClickListener(v -> setMode("weight", imgWeight));
        imgHelp.setOnClickListener(v -> setMode("help", imgHelp));

        // 초기: Length
        setMode("length", imgLength);
    }

    /* ---------- 초기 데이터 ---------- */
    private void initUnitMaps() {
        // 길이 (1 unit = x m)
        LENGTH_TO_M.put("mm", 0.001);
        LENGTH_TO_M.put("cm", 0.01);
        LENGTH_TO_M.put("m", 1.0);
        LENGTH_TO_M.put("km", 1000.0);
        LENGTH_TO_M.put("inch", 0.0254);
        LENGTH_TO_M.put("ft", 0.3048);
        LENGTH_TO_M.put("yd", 0.9144);
        LENGTH_TO_M.put("mile", 1609.34);

        // 무게 (1 unit = x g)
        WEIGHT_TO_G.put("mg", 0.001);
        WEIGHT_TO_G.put("g", 1.0);
        WEIGHT_TO_G.put("kg", 1000.0);
        WEIGHT_TO_G.put("oz", 28.349523125);
        WEIGHT_TO_G.put("lb", 453.59237);
        WEIGHT_TO_G.put("ton", 1_000_000.0); // metric ton (kg*1000)
    }

    /* ---------- 모드 전환 ---------- */
    private void setMode(String mode, ImageView selectedTab) {
        currentMode = mode;
        highlightTab(selectedTab);

        // 스피너 항목 세팅 & 보이기/숨기기
        switch (currentMode) {
            case "length":
                spFrom.setVisibility(View.VISIBLE);
                setSpinnerItems(LENGTH_TO_M.keySet().toArray(new String[0]), "m");
                break;
            case "weight":
                spFrom.setVisibility(View.VISIBLE);
                setSpinnerItems(WEIGHT_TO_G.keySet().toArray(new String[0]), "g");
                break;
            case "temperature":
                spFrom.setVisibility(View.VISIBLE);
                setSpinnerItems(TEMP_UNITS, "°C");
                break;
            case "help":
                spFrom.setVisibility(View.GONE); // 도움말에선 스피너 숨김
                break;
        }

        updateResults();
    }

    private void setSpinnerItems(String[] items, String defaultUnit) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, items
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFrom.setAdapter(adapter);

        // 기본 선택
        int defaultIdx = 0;
        for (int i = 0; i < items.length; i++) {
            if (items[i].equals(defaultUnit)) { defaultIdx = i; break; }
        }
        spFrom.setSelection(defaultIdx);
    }

    /* ---------- 렌더링 ---------- */
    private void updateResults() {
        if (tableResults == null) return;

        // 헤더 제외 모두 삭제
        int count = tableResults.getChildCount();
        if (count > 1) tableResults.removeViews(1, count - 1);

        if ("help".equals(currentMode)) {
            addRow("Enter a number", "Input");
            addRow("Pick a unit", "Spinner");
            addRow("Tap tabs", "Length/Temp/Weight");
            return;
        }

        String text = etInput.getText().toString().trim();
        if (text.isEmpty()) return;

        double value;
        try { value = Double.parseDouble(text); }
        catch (NumberFormatException e) { return; }

        switch (currentMode) {
            case "length":
                renderLength(value);
                break;
            case "weight":
                renderWeight(value);
                break;
            case "temperature":
                renderTemperature(value);
                break;
        }
    }

    private void renderLength(double value) {
        String from = (String) spFrom.getSelectedItem();
        double fromFactor = LENGTH_TO_M.get(from); // 1 from = ? m
        double inMeters = value * fromFactor;

        for (Map.Entry<String, Double> e : LENGTH_TO_M.entrySet()) {
            String unit = e.getKey();
            double toFactor = e.getValue(); // 1 unit = ? m
            double converted = inMeters / toFactor;
            addRow(fmt(converted), unit);
        }
    }

    private void renderWeight(double value) {
        String from = (String) spFrom.getSelectedItem();
        double fromFactor = WEIGHT_TO_G.get(from); // 1 from = ? g
        double inGrams = value * fromFactor;

        for (Map.Entry<String, Double> e : WEIGHT_TO_G.entrySet()) {
            String unit = e.getKey();
            double toFactor = e.getValue(); // 1 unit = ? g
            double converted = inGrams / toFactor;
            addRow(fmt(converted), unit);
        }
    }

    private void renderTemperature(double value) {
        String from = (String) spFrom.getSelectedItem();

        // 입력값을 섭씨로 변환
        double celsius;
        switch (from) {
            case "°C": celsius = value; break;
            case "°F": celsius = (value - 32) * 5.0 / 9.0; break;
            case "K":  celsius = value - 273.15; break;
            default:   celsius = value; // 방어
        }

        addRow(fmt2(celsius), "°C");
        addRow(fmt2(celsius * 9.0 / 5.0 + 32), "°F");
        addRow(fmt2(celsius + 273.15), "K");
    }

    /* ---------- 유틸 ---------- */
    private String fmt(double v) {
        double abs = Math.abs(v);
        if (abs >= 1000 || abs < 0.001)
            return String.format(Locale.US, "%.4E", v);
        else
            return String.format(Locale.US, "%.4f", v);
    }
    private String fmt2(double v) {
        return String.format(Locale.US, "%.2f", v);
    }

    private void addRow(String value, String unit) {
        TableRow row = new TableRow(this);
        row.setPadding(6, 6, 6, 6);
        row.setBackgroundColor(Color.parseColor("#F5F5F5"));

        TextView tvVal = new TextView(this);
        tvVal.setText(value);
        tvVal.setTextColor(Color.parseColor("#1976D2"));
        tvVal.setTextSize(18);
        tvVal.setGravity(Gravity.CENTER);

        TextView tvUnit = new TextView(this);
        tvUnit.setText(unit);
        tvUnit.setTextColor(Color.parseColor("#424242"));
        tvUnit.setTextSize(16);
        tvUnit.setGravity(Gravity.CENTER);

        TableRow.LayoutParams lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
        tvVal.setLayoutParams(lp);
        tvUnit.setLayoutParams(lp);

        row.addView(tvVal);
        row.addView(tvUnit);
        tableResults.addView(row);
    }

    private void highlightTab(ImageView selected) {
        int normal = Color.parseColor("#E0E0E0");
        int selectedColor = Color.parseColor("#BDBDBD");

        findViewById(R.id.imageLength).setBackgroundColor(normal);
        findViewById(R.id.imageTemperature).setBackgroundColor(normal);
        findViewById(R.id.imageWeight).setBackgroundColor(normal);
        findViewById(R.id.imageHelp).setBackgroundColor(normal);

        if (selected != null) selected.setBackgroundColor(selectedColor);
    }
}
