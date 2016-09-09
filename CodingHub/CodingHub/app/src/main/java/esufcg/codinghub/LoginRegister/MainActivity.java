package esufcg.codinghub.LoginRegister;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import esufcg.codinghub.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent mainIntent = new Intent(MainActivity.this, LoginActivity.class);
        MainActivity.this.startActivity(mainIntent);
    }
}
