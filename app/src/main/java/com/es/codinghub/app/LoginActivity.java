package com.es.codinghub.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends Activity {

	@BindView(R.id.emailEditText) EditText emailInput;
    @BindView(R.id.passwordEditText) EditText passwordInput;
    @BindView(R.id.loginButton) Button confirmButton;
    @BindView(R.id.signupTextView) TextView signupButton;

    private ProgressDialog progressDialog;
    private RequestQueue queue;
    private String baseUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_activity);
        ButterKnife.bind(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        queue = Volley.newRequestQueue(this);
        baseUrl = getString(R.string.api_url);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressDialog.dismiss();
    }

    @OnClick(R.id.signupTextView) void signup() {
		Intent intent = new Intent(this, SignupActivity.class);
		startActivity(intent);
        finish();
    }

    @OnClick(R.id.recoveryTextView) void recovery() {
        Intent intent = new Intent(this, RecoveryActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.loginButton) void login() {

        progressDialog.setMessage(getString(R.string.authenticating));
        progressDialog.show();

        String url = baseUrl + "/auth/login";

        queue.add(new JsonObjectRequest(Request.Method.GET, url,

            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        long userid = response.getLong("userid");
                        onSuccess(userid);
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
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();

                params.put("email", email);
                params.put("password", password);

                return params;
            }
        });
    }

    public void onSuccess(Long userid) {
        SharedPreferences authPref = getSharedPreferences(
                getString(R.string.authentication_file), Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = authPref.edit();
        editor.putLong("userid", userid);
        editor.commit();

        Intent intent = new Intent(this, AccountsActivity.class);
        startActivity(intent);
        finish();
    }

    public void onFail() {
        Toast.makeText(getBaseContext(), getString(R.string.authentication_failed),
                Toast.LENGTH_LONG).show();

        passwordInput.setText(null);
        progressDialog.hide();
    }
}
