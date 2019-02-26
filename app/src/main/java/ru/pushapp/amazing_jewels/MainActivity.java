package ru.pushapp.amazing_jewels;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    ImageView bottomBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomBtn = findViewById(R.id.bottom_btn_bg);
    }

    @Override
    protected void onResume() {
        SharedPreferences sharedPreferences = getSharedPreferences("local", Context.MODE_MULTI_PROCESS);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        int visibility = sharedPreferences.getInt("visibility", 0);
        bottomBtn.setVisibility(visibility);

        super.onResume();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        int visibility = sharedPreferences.getInt("visibility", 0);

        bottomBtn.setVisibility(visibility);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
