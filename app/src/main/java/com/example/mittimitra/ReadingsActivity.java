package com.example.mittimitra;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class ReadingsActivity extends AppCompatActivity {

    private PieChart npkPie;
    private LineChart moistureLine;

    private LineData lineData;
    private int xIndex = 0; // X-axis counter for moisture data

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readings);

        npkPie = findViewById(R.id.npkPie);
        moistureLine = findViewById(R.id.moistureLine);

        setupNpkPie();
        setupMoistureLine();
    }

    /** Initialize the static NPK pie chart */
    private void setupNpkPie() {
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(132f, "Nitrogen"));
        entries.add(new PieEntry(46f, "Phosphorus"));
        entries.add(new PieEntry(128f, "Potassium"));

        PieDataSet dataSet = new PieDataSet(entries, "NPK Composition (ppm)");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(14f);

        PieData data = new PieData(dataSet);
        npkPie.setData(data);
        npkPie.getDescription().setEnabled(false);
        npkPie.setCenterText("Soil NPK");
        npkPie.animateY(1000);
    }

    /** Prepare real-time moisture line chart */
    private void setupMoistureLine() {
        LineDataSet dataSet = new LineDataSet(new ArrayList<>(), "Moisture %");
        dataSet.setLineWidth(2f);
        dataSet.setColor(ColorTemplate.getHoloBlue());
        dataSet.setCircleRadius(3f);
        dataSet.setCircleColor(ColorTemplate.getHoloBlue());
        dataSet.setValueTextSize(10f);

        lineData = new LineData(dataSet);
        moistureLine.setData(lineData);

        Description desc = new Description();
        desc.setText("Real-time Moisture");
        moistureLine.setDescription(desc);
        moistureLine.getAxisLeft().setAxisMinimum(0f);
        moistureLine.getAxisLeft().setAxisMaximum(100f);
        moistureLine.getAxisRight().setEnabled(false);
        moistureLine.getXAxis().setDrawGridLines(false);
        moistureLine.animateX(500);
    }

    /**
     * Call this whenever a new moisture reading is received,
     * for example from your BluetoothService in DashboardActivity.
     */
    public void addMoistureReading(int moisturePercent) {
        lineData.addEntry(new Entry(xIndex++, moisturePercent),
                0); // 0 = first data set
        lineData.notifyDataChanged();
        moistureLine.notifyDataSetChanged();
        moistureLine.moveViewToX(lineData.getEntryCount());
    }
}
