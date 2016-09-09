package esufcg.codinghub.LoginRegister;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import esufcg.codinghub.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        final EditText etEmail = (EditText) findViewById(R.id.etEmail);

        final Button btnSendPassword = (Button) findViewById(R.id.btnSendPassword);
    }
}
