package com.es.codinghub.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AccountsActivity extends Activity {

    @BindView(R.id.onlineJudgeSpinner) Spinner onlineJudge;
    @BindView(R.id.userEditText) EditText onlineJudgeUser;
    @BindView(R.id.accountsListView) ListView accounts;
    @BindView(R.id.registerButton) Button register;

    private ProgressDialog progressDialog;
    private AccountsAdapter adapter;
    private RequestQueue queue;

    private Long userid;
    private String baseUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        baseUrl = getString(R.string.api_url);

        setContentView(R.layout.accounts_activity);
        ButterKnife.bind(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);

        SharedPreferences authPref = getSharedPreferences(
                getString(R.string.authentication_file), Context.MODE_PRIVATE);

        userid = authPref.getLong("userid", -1);
        queue = Volley.newRequestQueue(this);

        loadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.registerButton) void register() {

        hideKeyboard();

        progressDialog.setMessage(getString(R.string.registering_account));
        progressDialog.show();

        String url = baseUrl + "/user/" + userid + "/account";

        queue.add(new StringRequest(Request.Method.POST, url,

            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    onlineJudgeUser.setText(null);
                    loadData();
                }
            },

            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getBaseContext(), getString(R.string.operation_failed),
                            Toast.LENGTH_LONG).show();
                    progressDialog.hide();
                }
            })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                String judge = onlineJudge.getSelectedItem().toString();
                String username = onlineJudgeUser.getText().toString();

                params.put("judge", judge);
                params.put("username", username);

                return params;
            }
        });
    }

    public void hideKeyboard() {
        View view = getCurrentFocus();
        if (view == null) return;

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void loadData() {

        String url = baseUrl + "/user/" + userid;

        queue.add(new JsonObjectRequest(Request.Method.GET, url,

            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray array = response.getJSONArray("accounts");
                        List<JSONObject> values = new ArrayList<>();

                        for (int i=0; i<array.length(); ++i)
                            values.add(array.getJSONObject(i));

                        adapter = new AccountsAdapter(AccountsActivity.this, values);
                        accounts.setAdapter(adapter);
                        progressDialog.hide();
                    }
                        catch (JSONException e) {}
                }
            },

            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getBaseContext(), getString(R.string.no_connection),
                            Toast.LENGTH_LONG).show();
                    onBackPressed();
                }
            })
        );
    }

    public class AccountsAdapter extends ArrayAdapter<JSONObject> {

        public AccountsAdapter(Context context, List<JSONObject> accounts) {
            super(context, 0, accounts);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {

            JSONObject data = getItem(position);
            Context context = getContext();

            ViewHolder holder;

            if (view != null)
                holder = (ViewHolder) view.getTag();

            else {
                view = LayoutInflater.from(context).inflate(R.layout.account, parent, false);
                holder = new ViewHolder(view, position);
                view.setTag(holder);
            }

            try {
                String judge = data.getString("judge");
                String username = data.getString("username");

                holder.judgeTextView.setText(judge);
                holder.userTextView.setText(username);
            }
                catch (JSONException e) {}
            return view;
        }

        public class ViewHolder {

            @BindView(R.id.judgeTextView) TextView judgeTextView;
            @BindView(R.id.userTextView) TextView userTextView;

            public int position;

            public ViewHolder(View view, int position) {
                ButterKnife.bind(this, view);
                this.position = position;
            }

            @OnClick(R.id.deleteButton) void delete() {

                try {
                    final JSONObject account = getItem(position);
                    Long accountid = null;

                    accountid = account.getLong("id");

                    progressDialog.setMessage(getString(R.string.deleting_account));
                    progressDialog.show();

                    String url = baseUrl + "/user/" + userid + "/account/" + accountid;

                    queue.add(new StringRequest(Request.Method.DELETE, url,

                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                adapter.remove(account);
                                progressDialog.hide();
                            }
                        },

                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                onFail();
                            }
                        })
                    );
                }

                catch (JSONException e) {
                    onFail();
                }
            }

            public void onFail() {
                Toast.makeText(getBaseContext(), getString(R.string.operation_failed),
                        Toast.LENGTH_LONG).show();
                progressDialog.hide();
            }
        }
    }
}
