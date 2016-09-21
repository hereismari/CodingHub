package com.es.codinghub.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignupActivity extends Activity {

    @BindView(R.id.emailEditText) EditText emailInput;
    @BindView(R.id.passwordEditText) EditText passwordInput;
    @BindView(R.id.passwordAgainEditText) EditText passwordAgainInput;
    @BindView(R.id.signupButton) Button confirmButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.signup);
        ButterKnife.bind(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @OnClick(R.id.signupButton) void signup() {
        if(validate() == false)
            return;

        ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.signing_up));
        progressDialog.show();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
    }

    public boolean validate() {
        boolean valid = false;

        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        String passwordAgain = passwordAgainInput.getText().toString();

        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() == false)
        	emailInput.setError(getString(R.string.invalid_email));

        else if (password.length() < 4)
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
