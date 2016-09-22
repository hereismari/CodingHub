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
import com.android.volley.toolbox.StringRequest;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);
        ButterKnife.bind(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.authenticating));

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
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

    @OnClick(R.id.loginButton) void login() {
    	if(validate() == false)
			return;

        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://10.0.2.2:8080/login";

        queue.add(new JsonObjectRequest(Request.Method.GET, url,

            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Long userid = response.getLong("userid");
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

        /* TODO ir para a tela principal
        Intent intent = new Intent(this, HubActivity.class);
        startActivity(intent); */

        finish();
    }

    public void onFail() {
        Toast.makeText(getBaseContext(), getString(R.string.authentication_failed),
                Toast.LENGTH_LONG).show();

        passwordInput.setText(null);
        progressDialog.hide();
    }

    public boolean validate() {
        boolean valid = false;

        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();

        if(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() == false)
        	emailInput.setError(getString(R.string.invalid_email));

        else if(password.length() < 6)
        	passwordInput.setError(getString(R.string.short_password));

        else {
        	emailInput.setError(null);
        	passwordInput.setError(null);
        	valid = true;
        }

        return valid;
    }

}
