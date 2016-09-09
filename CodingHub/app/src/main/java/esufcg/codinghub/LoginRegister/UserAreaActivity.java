package esufcg.codinghub.LoginRegister;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import esufcg.codinghub.R;

public class UserAreaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);

        final EditText etNickname = (EditText) findViewById(R.id.etNickname);

        final TextView tvWelcomMsg = (TextView) findViewById(R.id.tvWelcomeMsg);

    }
}
