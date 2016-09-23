package com.es.codinghub.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends Activity {

	@BindView(R.id.emailEditText) EditText emailInput;
    @BindView(R.id.passwordEditText) EditText passwordInput;
    @BindView(R.id.loginButton) Button confirmButton;
    @BindView(R.id.signupTextView) TextView signupButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);
        ButterKnife.bind(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @OnClick(R.id.signupTextView) void signup() {
		Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
		startActivity(intent);
    }

    @OnClick(R.id.loginButton) void login() {

        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);

        /*
    	if(validate() == false)
			return;

		ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.authenticating));
        progressDialog.show();*/
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
    }

    public boolean validate() {
        boolean valid = false;

        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();

        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() == false)
        	emailInput.setError(getString(R.string.invalid_email));

        else if (password.length() < 4)
        	passwordInput.setError(getString(R.string.short_password));

        else {
        	emailInput.setError(null);
        	passwordInput.setError(null);
        	valid = true;
        }

        return valid;
    }

}
