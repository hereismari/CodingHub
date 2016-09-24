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

public class SettingsActivity extends Activity {

	@BindView(R.id.oldPasswordEditText) EditText oldPassword;
	@BindView(R.id.newPasswordEditText) EditText newPassword;

	private ProgressDialog progressDialog;
	private RequestQueue queue;

	private Long userid;
	private String email;
	private String baseUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		baseUrl = getString(R.string.api_url);

		setContentView(R.layout.settings_activity);
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

	@OnClick(R.id.changePasswordButton) void trocarSenha() {

		hideKeyboard();
		if (validate() == false)
			return;

		progressDialog.setMessage(getString(R.string.changing_password));
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

				String password = oldPassword.getText().toString();

				params.put("email", email);
				params.put("password", password);

				return params;
			}
		});
	}

	@OnClick(R.id.accountsButton) void editAccounts() {
		Intent intent = new Intent(this, AccountsActivity.class);
		startActivity(intent);
		finish();
	}

	public void clearAll() {
		oldPassword.setText(null);
		newPassword.setText(null);
		progressDialog.hide();
	}

	public void commit() {
		String url = baseUrl + "/user/" + userid + "/password";

		queue.add(new StringRequest(Request.Method.PUT, url,

				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						clearAll();
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
		{
			@Override
			public String getBodyContentType() {
				return "text/plain";
			}

			@Override
			public byte[] getBody() throws AuthFailureError {
				return newPassword.getText().toString().getBytes();
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

	public boolean validate() {
		boolean valid = false;

		String password = newPassword.getText().toString();

		if (password.length() < 6)
			newPassword.setError(getString(R.string.short_password));

		else {
			newPassword.setError(null);
			valid = true;
		}

		return valid;
	}
}
