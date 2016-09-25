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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Graphics extends Fragment {

    @BindView(R.id.refreshButton) Button refreshButton;

    private LineChart lineChart;
    private RequestQueue queue;
    private String baseUrl;
    private Long userid;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_home,container,false);

        ButterKnife.bind(this, v);

        lineChart = (LineChart) v.findViewById(R.id.chart);
        lineChart.setNoDataText("Carregando dados...");

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

                        for (int i=0; i<response.length(); ++i) {

                            JSONObject submission = response.getJSONObject(i);

                            Calendar cal = GregorianCalendar.getInstance();
                            cal.setTimeInMillis(submission.getLong("timestamp") * 1000L);

                            if (cal.get(Calendar.YEAR) == today.get(Calendar.YEAR))
                                buckets[cal.get(Calendar.MONTH)] += 1;
                        }

                        simpleLineChart(buckets);

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
