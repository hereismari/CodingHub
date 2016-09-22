package com.es.codinghub.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

public class SignupActivity extends Activity {

    @BindView(R.id.emailEditText) EditText emailInput;
    @BindView(R.id.passwordEditText) EditText passwordInput;
    @BindView(R.id.passwordAgainEditText) EditText passwordAgainInput;
    @BindView(R.id.signupButton) Button confirmButton;

    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.signup);
        ButterKnife.bind(this);

        progressDialog = new ProgressDialog(SignupActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.signing_up));

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressDialog.dismiss();
    }

    @OnClick(R.id.signupButton) void signup() {
        if(validate() == false)
            return;

        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://10.0.2.2:8080/user";

        queue.add(new StringRequest(Request.Method.POST, url,

            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    onSuccess();
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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();

                params.put("email", email);
                params.put("password", password);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                return headers;
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void onSuccess() {
        onBackPressed();
    }

    public void onFail() {
        Toast.makeText(getBaseContext(), getString(R.string.signup_failed),
                Toast.LENGTH_LONG).show();

        passwordInput.setText(null);
        passwordAgainInput.setText(null);
        progressDialog.hide();
    }

    public boolean validate() {
        boolean valid = false;

        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        String passwordAgain = passwordAgainInput.getText().toString();

        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() == false)
        	emailInput.setError(getString(R.string.invalid_email));

        else if (password.length() < 6)
        	passwordInput.setError(getString(R.string.short_password));

        else if (password.equals(passwordAgain) == false) {
        	passwordInput.setText(null);
        	passwordAgainInput.setText(null);
        	passwordAgainInput.setError(getString(R.string.passwords_differ));
        }

        else {
        	emailInput.setError(null);
        	passwordInput.setError(null);
        	valid = true;
        }

        return valid;
    }

}
