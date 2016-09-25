package com.es.codinghub.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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

public class DeleteActivity extends Activity {

	@BindView(R.id.passwordEditText) EditText passwordInput;

	private ProgressDialog progressDialog;
	private RequestQueue queue;

	private Long userid;
	private String email;
	private String baseUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		baseUrl = getString(R.string.api_url);

		setContentView(R.layout.delete_activity);
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

	@OnClick(R.id.confirmButton) void confirm() {

		hideKeyboard();

		progressDialog.setMessage(getString(R.string.deleting_user));
		progressDialog.show();

		String url = baseUrl + "/auth/login";

		queue.add(new JsonObjectRequest(Request.Method.GET, url,

			new Response.Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					commit();
				}
			},

			new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					Toast.makeText(getBaseContext(), getString(R.string.wrong_password),
							Toast.LENGTH_LONG).show();
					clearAll();
				}
			})
		{

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> params = new HashMap<>();

				String password = passwordInput.getText().toString();

				params.put("email", email);
				params.put("password", password);

				return params;
			}
		});
	}

	public void clearAll() {
		passwordInput.setText(null);
		progressDialog.hide();
	}

	public void commit() {
		String url = baseUrl + "/user/" + userid;

		queue.add(new StringRequest(Request.Method.DELETE, url,

			new Response.Listener<String>() {
				@Override
				public void onResponse(String response) {
					clearAll();

					SharedPreferences authPref = getSharedPreferences(
							getString(R.string.authentication_file), Context.MODE_PRIVATE);

					SharedPreferences.Editor editor = authPref.edit();
					editor.putLong("userid", -1);
					editor.commit();

					Intent intent = new Intent(DeleteActivity.this, LoginActivity.class);
					startActivity(intent);
					finish();
				}
			},

			new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					Toast.makeText(getBaseContext(), getString(R.string.operation_failed),
							Toast.LENGTH_LONG).show();
					clearAll();
				}
			})
		);
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
						email = response.getString("email");
					} catch (JSONException e) {}
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
}
