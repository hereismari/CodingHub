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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SubmissionsList extends Fragment {

    ArrayList<SubmissionView> list;
    private RequestQueue queue;
    private String baseUrl;
    private Long userid;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.submissions_list,container,false);

        SharedPreferences authPref = getActivity().getSharedPreferences(
                "com.es.codinghub.AUTHENTICATION", Context.MODE_PRIVATE);


        userid = authPref.getLong("userid", -1);
        queue = Volley.newRequestQueue(getActivity());
        baseUrl = getString(R.string.api_url);

        list = new ArrayList<SubmissionView>();

        refresh();

        // PARTE QUE JÁ ESTAVA

        SubmissionAdapter adapter = new SubmissionAdapter(getActivity(), list);

        ListView listView = (ListView) v.findViewById(R.id.subs_list);

        listView.setAdapter(adapter);

        return v;
    }

    private void refresh() {
        final String url = baseUrl + "/user/" + userid + "/submissions";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        System.out.println("aq");


                        try {

                            for (int i=0; i<response.length(); ++i) {
                                JSONObject submission = response.getJSONObject(i);

                                JSONObject problem = submission.getJSONObject("problem");

                                int image = -1;
                                if (problem.getString("judge").equals("CodeForces"))
                                    image = R.drawable.ic_codeforces;
                                else if (submission.getString("judge").equals("UVa"))
                                    image = R.drawable.ic_uva;

                                int verdict;
                                if (submission.getString("verdict").equals("ACCEPTED"))
                                    verdict = R.drawable.accepted;
                                else if ((submission.getString("verdict").equals("TIME_LIMIT")))
                                    verdict = R.drawable.time_limit_exceed;
                                else
                                    verdict = R.drawable.wrong_answer;

                                //list.add(new SubmissionView(image, problem.getString("name"), "seila mano", verdict));
                                break;
                            }

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