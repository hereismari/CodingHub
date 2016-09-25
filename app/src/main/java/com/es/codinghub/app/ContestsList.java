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
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ContestsList extends Fragment {

    private RequestQueue queue;
    private String baseUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.contests_list, container, false);

        queue = Volley.newRequestQueue(getActivity());
        baseUrl = getString(R.string.api_url);

        String url = baseUrl + "/contests";

        queue.add(new JsonArrayRequest(Request.Method.GET, url,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            ArrayList<ContestView> list = new ArrayList<>();
                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM HH:mm");

                            for (int i=0; i<response.length(); ++i) {

                                JSONObject contest = response.getJSONObject(i);
                                Date date = new Date(contest.getLong("timestamp") * 1000L);

                                int image = -1;
                                if (contest.getString("judge").equals("Codeforces"))
                                    image = R.drawable.ic_codeforces;
                                else if (contest.getString("judge").equals("UVa"))
                                    image = R.drawable.ic_uva;

                                list.add(new ContestView(
                                    image,
                                    contest.getString("name"),
                                    contest.getString("judge"),
                                    formatter.format(date)
                                ));
                            }

                            onSuccess(v, list);
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
                })
        );

        return v;
    }

    public void onSuccess(View v, ArrayList<ContestView> list) {
        ContestAdapter adapter = new ContestAdapter(getActivity(), list);
        ListView listView = (ListView) v.findViewById(R.id.cont_list);
        listView.setAdapter(adapter);
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
