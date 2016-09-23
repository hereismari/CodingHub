package com.es.codinghub.app;

import android.os.Bundle;
import android.app.Activity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class HomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        LineChart lineChart = (LineChart) findViewById(R.id.chart);

        int[] number_questions = {1,2,3,4,5,6,7,8,9,10,11,12};
        ArrayList<Entry> entries = new ArrayList<>();
        for (int i = 0; i < 12; i++)
            entries.add(new Entry(number_questions[i], i));

        LineDataSet dataSet = new LineDataSet(entries, "Número de questões");

        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        ArrayList<String> xValues = new ArrayList<>();
        for (int i = 0; i < 12; i++)
            xValues.add(months[i]);

        LineData data = new LineData(xValues, dataSet);
        lineChart.setData(data);

        lineChart.setDescription("Quantidade de questões feitas por mês.");
        dataSet.setDrawFilled(true);
        dataSet.isDrawCubicEnabled();
        lineChart.animateY(1000);
    }
}
