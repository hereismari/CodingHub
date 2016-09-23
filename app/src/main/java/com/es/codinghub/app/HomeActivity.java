package com.es.codinghub.app;

import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

import butterknife.OnClick;

public class HomeActivity extends Activity {
    private LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        lineChart = (LineChart) findViewById(R.id.chart);
        //multiLineChart();
        simpleLineChart();
        lineChart.setNoDataText("Ainda não temos dados ='(");
    }

    private void simpleLineChart() {
        int[] quantity_questions = {1,2,3,4,5,6,7,8,9,10,11,12};
        ArrayList<Entry> entries = new ArrayList<>();
        for (int i = 0; i < 12; i++)
            entries.add(new Entry(quantity_questions[i], i));

        LineDataSet dataSet = new LineDataSet(entries, "Número de questões");

        String c = "#ed1f24";

        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSet.setDrawCubic(false);
        dataSet.setDrawCircles(false);
        dataSet.setCircleColor(Color.parseColor(c));
        dataSet.setCircleSize(8f);
        dataSet.setCircleColorHole(Color.BLACK);
        dataSet.setDrawCircleHole(false);
        dataSet.setLineWidth(1f);
        dataSet.setColor(Color.parseColor(c));
        dataSet.setDrawHorizontalHighlightIndicator(false);
        dataSet.setDrawVerticalHighlightIndicator(false);

        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        ArrayList<String> xValues = new ArrayList<>();
        for (int i = 0; i < 12; i++)
            xValues.add(months[i]);

        LineData data = new LineData(xValues, dataSet);
        data.setDrawValues(false);

        lineChart.setData(data);
        lineChart.setDescription("Quantidade de questões feitas por mês.");
        lineChart.animateY(1000);
        //lineChart.setBackgroundColor(Color.BLUE);
    }

    private void multiLineChart() {
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        ArrayList<String> xValues = new ArrayList<>();
        for (int i = 0; i < months.length; i++)
            xValues.add(months[i]);

        int[] quantity_questionsUVA = {1,2,3,4,5,6,7,8,9,10,11,12};
        int[] quantity_questionsCodeForces = {3,2,3,4,10,6,7,21,9,10,1,5};

        String[] judges = {"UVA", "CodeForces"};
        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        for (int i = 0; i < judges.length; i++) {
            ArrayList<Entry> yValues = new ArrayList<Entry>();

            for (int j = 0; j < months.length; j++) {
                if (judges[i].equals("UVA")) {
                    yValues.add(new Entry(quantity_questionsUVA[j], j));
                } else {
                    yValues.add(new Entry(quantity_questionsCodeForces[j], j));
                }
            }


            String judge = "";
            String col = "#004bf6";
            judge = judges[i];
            if (judges[i].equals("UVA")) {
                col = "#ed1f24";
            } else {
                col = "#ffba00";
            }

            LineDataSet set = new LineDataSet(yValues, judge);

            dataSets.add(set);
        }

        LineData data = new LineData(xValues);
        data.setDrawValues(false);
        data.setHighlightEnabled(false);

        lineChart.setData(data);
    }

    @OnClick(R.id.refreshButton) void refresh() {
        Log.d("CodingHub","aqqq");
        lineChart.notifyDataSetChanged();
        lineChart.invalidate();
    }
}
