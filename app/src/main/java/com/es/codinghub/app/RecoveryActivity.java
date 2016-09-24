package com.es.codinghub.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecoveryActivity extends Activity {

    @BindView(R.id.emailEditText) EditText emailInput;
    @BindView(R.id.recoverButton) Button confirmButton;

    private ProgressDialog progressDialog;
    private RequestQueue queue;
    private String baseUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.recovery_activity);
        ButterKnife.bind(this);

        progressDialog = new ProgressDialog(RecoveryActivity.this);
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

    @OnClick(R.id.recoverButton) void recover() {

        progressDialog.setMessage(getString(R.string.recovering));
        progressDialog.show();

        String url = baseUrl + "/auth/recover";

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
                params.put("email", email);

                return params;
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
        Toast.makeText(getBaseContext(), getString(R.string.recovery_succeded),
                Toast.LENGTH_LONG).show();
        onBackPressed();
    }

    public void onFail() {
        Toast.makeText(getBaseContext(), getString(R.string.recovery_failed),
                Toast.LENGTH_LONG).show();
        progressDialog.hide();
    }
}
