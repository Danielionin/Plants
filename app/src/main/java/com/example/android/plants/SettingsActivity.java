package com.example.android.plants;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;

public class SettingsActivity extends AppCompatActivity {

    // private class for disabling notification
    private class DeleteTokenTask extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                FirebaseInstanceId.getInstance().deleteInstanceId();
            } catch (IOException e) {
                Log.d("TEST", "Exception deleting token", e);
            }
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Switch notification_switch;
        Button signoutButton;
        notification_switch = (Switch) findViewById(R.id.notificationSwitch);
        signoutButton = (Button) findViewById(R.id.sign_out_button);

        SharedPreferences sharedPrefs = getSharedPreferences("switch_status", MODE_PRIVATE);
        notification_switch.setChecked(sharedPrefs.getBoolean("switch_status", true));

        signoutButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        Intent i = new Intent(SettingsActivity.this, MainActivity.class);
                        startActivity(i);
                    }
                }
        );

        notification_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPreferences.Editor editor = getSharedPreferences("switch_status", MODE_PRIVATE).edit();
                    editor.putBoolean("switch_status", true);
                    editor.apply();
                    FirebaseInstanceId.getInstance().getToken();
                }
                else {
                    SharedPreferences.Editor editor = getSharedPreferences("switch_status", MODE_PRIVATE).edit();
                    editor.putBoolean("switch_status", false);
                    editor.apply();
                    new DeleteTokenTask().execute();
                }
            }
        });



    }
}
