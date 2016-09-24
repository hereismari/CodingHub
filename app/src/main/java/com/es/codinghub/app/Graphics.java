package com.es.codinghub.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.EntryXIndexComparator;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.OnClick;

public class Graphics extends Fragment {

    private LineChart lineChart;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_home,container,false);

        lineChart = (LineChart) v.findViewById(R.id.chart);
        //multiLineChart();
        simpleLineChart();
        lineChart.setNoDataText("Ainda não temos dados ='(");

        return v;
    }

    private void simpleLineChart() {

        // AQUI
        int[] quantity_questions = {1,2,3,4,5,6,7,8,9,10,11,12};
        ArrayList<Entry> entries = new ArrayList<>();
        for (int i = 0; i < 12; i++)
            entries.add(new Entry(quantity_questions[i], i));

        Collections.sort(entries, new EntryXIndexComparator());

        LineDataSet dataSet = new LineDataSet(entries, "Número de questões");
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

        String c = "#ed1f24";

        String[] months = {"J", "F", "M", "A", "M", "J", "J", "A", "S", "O", "N", "D"};
        ArrayList<String> xValues = new ArrayList<>();
        for (int i = 0; i < 12; i++)
            xValues.add(months[i]);

        LineData data = new LineData(xValues, dataSet);
        data.setDrawValues(false);


        // styling
        lineChart.setData(data);
        lineChart.setDescription("Quantidade de questões feitas por mês.");
        lineChart.animateY(1000);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(false);
        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.getXAxis().setDrawGridLines(false);

        LineGraphToolTip toolTip = new LineGraphToolTip(getActivity(), R.layout.line_graph_tooltip);
        lineChart.setMarkerView(toolTip);
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


    //atualizar o grafico
    @OnClick(R.id.refreshButton) void refresh() {
        //Log.d("CodingHub","aqqq");
        lineChart.notifyDataSetChanged();
        lineChart.invalidate();
    }
}
