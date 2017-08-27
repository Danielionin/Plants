package com.example.android.plants;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isfirstrun", true);

        //check if first run
        if(isFirstRun) {
            Toast.makeText(MainActivity.this, "First Run", Toast.LENGTH_LONG).show();
            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                    .putBoolean("isfirstrun", false).apply();

            //start login activity
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

        }
        setContentView(R.layout.activity_main);
    }
}
