package com.example.programmingasignment1;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.LinkedHashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText etInput;
    private Spinner spFrom;
    private TableLayout tableResults;

    private String currentMode = "length"; // 기본 모드
    private final Map<String, Double> lengthUnits = new LinkedHashMap<>();
    private final Map<String, Double> weightUnits = new LinkedHashMap<>();
    private final Map<String, String> tempUnits = new LinkedHashMap<>();

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

        // 단위 데이터 초기화
        setupUnitMaps();

        // 입력값 변경 감지 → 즉시 결과 업데이트
        etInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateResults();
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        // 탭 클릭 리스너
        imgLength.setOnClickListener(v -> {
            currentMode = "length";
            highlightTab(imgLength);
            updateResults();
        });
        imgWeight.setOnClickListener(v -> {
            currentMode = "weight";
            highlightTab(imgWeight);
            updateResults();
        });
        imgTemperature.setOnClickListener(v -> {
            currentMode = "temperature";
            highlightTab(imgTemperature);
            updateResults();
        });
        imgHelp.setOnClickListener(v -> {
            currentMode = "help";
            highlightTab(imgHelp);
            showHelp();
        });

        // 초기 화면
        highlightTab(imgLength);
        updateResults();
    }

    /** 단위 값 세팅 */
    private void setupUnitMaps() {
        // 길이 (기준: meter)
        lengthUnits.put("m", 1.0);
        lengthUnits.put("cm", 0.01);
        lengthUnits.put("mm", 0.001);
        lengthUnits.put("km", 1000.0);
        lengthUnits.put("in", 0.0254);
        lengthUnits.put("ft", 0.3048);

        // 무게 (기준: gram)
        weightUnits.put("g", 1.0);
        weightUnits.put("kg", 1000.0);
        weightUnits.put("lb", 453.592);
        weightUnits.put("oz", 28.35);

        // 온도 (표시용)
        tempUnits.put("°C", "Celsius");
        tempUnits.put("°F", "Fahrenheit");
        tempUnits.put("K", "Kelvin");
    }

    /** 현재 모드에 맞는 변환 결과 업데이트 */
    private void updateResults() {
        tableResults.removeViews(1, Math.max(0, tableResults.getChildCount() - 1)); // 헤더 제외 모두 제거

        String inputText = etInput.getText().toString().trim();
        if (inputText.isEmpty()) return;

        double value;
        try {
            value = Double.parseDouble(inputText);
        } catch (NumberFormatException e) {
            return;
        }

        switch (currentMode) {
            case "length":
                for (Map.Entry<String, Double> unit : lengthUnits.entrySet()) {
                    double meters = value * lengthUnits.get("m"); // 기준 단위 변환
                    double converted = (value / lengthUnits.get("m")) * unit.getValue(); // 동일 기준
                    addTableRow(String.format("%.4f", converted), unit.getKey());
                }
                break;

            case "weight":
                for (Map.Entry<String, Double> unit : weightUnits.entrySet()) {
                    double grams = value * weightUnits.get("g");
                    double converted = (value / weightUnits.get("g")) * unit.getValue();
                    addTableRow(String.format("%.4f", converted), unit.getKey());
                }
                break;

            case "temperature":
                addTableRow(String.format("%.2f", value), "°C");
                addTableRow(String.format("%.2f", cToF(value)), "°F");
                addTableRow(String.format("%.2f", cToK(value)), "K");
                break;
        }
    }

    /** 표에 한 행 추가 */
    private void addTableRow(String val, String unit) {
        TableRow row = new TableRow(this);
        row.setPadding(6, 6, 6, 6);
        row.setBackgroundColor(Color.WHITE);

        TextView tvValue = new TextView(this);
        tvValue.setText(val);
        tvValue.setGravity(Gravity.CENTER);
        tvValue.setTextColor(Color.parseColor("#ff7a00"));
        tvValue.setTextSize(18);

        TextView tvUnit = new TextView(this);
        tvUnit.setText(unit);
        tvUnit.setGravity(Gravity.CENTER);
        tvUnit.setTextColor(Color.parseColor("#444444"));
        tvUnit.setTextSize(16);

        row.addView(tvValue);
        row.addView(tvUnit);
        tableResults.addView(row);
    }

    /** 선택된 탭 강조 */
    private void highlightTab(ImageView selected) {
        int normal = Color.parseColor("#5b4a3a");
        int selectedColor = Color.parseColor("#8b7765");

        findViewById(R.id.imageLength).setBackgroundColor(normal);
        findViewById(R.id.imageTemperature).setBackgroundColor(normal);
        findViewById(R.id.imageWeight).setBackgroundColor(normal);
        findViewById(R.id.imageHelp).setBackgroundColor(normal);

        selected.setBackgroundColor(selectedColor);
    }

    /** 도움말 탭 눌렀을 때 */
    private void showHelp() {
        tableResults.removeViews(1, Math.max(0, tableResults.getChildCount() - 1));
        addTableRow("Enter a number", "Input value");
        addTableRow("Click tab", "Change category");
        addTableRow("Results", "Shown below");
    }

    /** 온도 변환식 */
    private double cToF(double c) { return (c * 9 / 5) + 32; }
    private double cToK(double c) { return c + 273.15; }
}
