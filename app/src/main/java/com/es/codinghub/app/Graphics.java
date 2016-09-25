package com.es.codinghub.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.EntryXIndexComparator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Graphics extends Fragment {

    @BindView(R.id.refreshButton) Button refreshButton;

    private LineChart lineChart;
    private BarChart barChart;
    private RequestQueue queue;
    private String baseUrl;
    private Long userid;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_home,container,false);

        ButterKnife.bind(this, v);

        lineChart = (LineChart) v.findViewById(R.id.lineChart);
        barChart = (BarChart) v.findViewById(R.id.barChart);

        lineChart.setNoDataText("Carregando dados...");
        barChart.setNoDataText("Carregando dados...");

        SharedPreferences authPref = getActivity().getSharedPreferences(
                getString(R.string.authentication_file), Context.MODE_PRIVATE);

        userid = authPref.getLong("userid", -1);
        queue = Volley.newRequestQueue(getActivity());
        baseUrl = getString(R.string.api_url);

        refresh();

        return v;
    }

    private void simpleLineChart(int[] quantity_questions) {

        ArrayList<Entry> entries = new ArrayList<>();
        for (int i = 0; i < 12; i++)
            entries.add(new Entry(quantity_questions[i], i));

        Collections.sort(entries, new EntryXIndexComparator());

        LineDataSet dataSet = new LineDataSet(entries, "Número de questões");
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

        String c = "#ed1f24";

        String[] months = {"Jan", "Fev", "Mar", "Abr", "Maio", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez"};
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


    private void SimpleBarChart(Map<String, Integer> verdicts) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        int i = 0;
        for (String verdict : verdicts.keySet()) {
            entries.add(new BarEntry(verdicts.get(verdict), i));
            i++;
        }

        BarDataSet dataSet = new BarDataSet(entries, "veredictos das questões");

        ArrayList<String> labels = new ArrayList<>();
        for (String verdict : verdicts.keySet()) {
            labels.add(verdict);
        }

        BarData data = new BarData(labels, dataSet);
        barChart.setData(data);
        barChart.getXAxis().setLabelsToSkip(0);
    }

    @OnClick(R.id.refreshButton) void refresh() {

        refreshButton.setEnabled(false);

        String url = baseUrl + "/user/" + userid + "/submissions";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url,

            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    try {
                        int[] buckets = new int[12];
                        Calendar today = GregorianCalendar.getInstance();

                        // temporary
                        Map<String, Integer> verdicts =  new HashMap<String, Integer>();
                        String[] ver = {"ACCEPTED", "COMPILATION_ERROR", "MEMORY_LIMIT", "OTHER", "PRESENTATION_ERROR", "RUNTIME_ERROR", "TIME_LIMIT", "WRONG_ANSWER"};


                        for (int i=0; i<response.length(); ++i) {

                            JSONObject submission = response.getJSONObject(i);

                            Calendar cal = GregorianCalendar.getInstance();
                            cal.setTimeInMillis(submission.getLong("timestamp") * 1000L);

                            if (cal.get(Calendar.YEAR) == today.get(Calendar.YEAR))
                                buckets[cal.get(Calendar.MONTH)] += 1;

                            // temporary

                            for (String s : ver) verdicts.put(s, 0);

                            for (String result : ver) {
                                Integer value = verdicts.get(result);
                                verdicts.put(result, value + 1);
                            }
                        }

                        simpleLineChart(buckets);
                        SimpleBarChart(verdicts);

                        refreshButton.setEnabled(true);
                    }

                    catch (JSONException e) {
                        onFail();
                    }
                }
            },

            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    onFail();
                }
            }
        );

        request.setRetryPolicy(new DefaultRetryPolicy(
                120000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    public void onFail() {
        Toast.makeText(getActivity().getBaseContext(), getString(R.string.no_connection),
                Toast.LENGTH_LONG).show();

        SharedPreferences authPref = getActivity().getSharedPreferences(
                getString(R.string.authentication_file), Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = authPref.edit();
        editor.putLong("userid", -1);
        editor.commit();

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
