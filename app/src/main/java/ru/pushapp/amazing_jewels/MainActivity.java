package ru.pushapp.amazing_jewels;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener, View.OnClickListener {

    TextView coinsTV;

    TextView externalLifeTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        coinsTV = findViewById(R.id.count_coins);
        externalLifeTV = findViewById(R.id.count_life);
    }

    @Override
    protected void onResume() {
        SharedPreferences sharedPreferences = getSharedPreferences("local", Context.MODE_MULTI_PROCESS);
        int userBalance = sharedPreferences.getInt("money", 0);
        int externalLife = sharedPreferences.getInt("life", 0);

        coinsTV.setText(String.valueOf(userBalance));
        coinsTV.setOnClickListener(this);
        externalLifeTV.setText(String.valueOf(externalLife));

        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        super.onResume();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        int userBalance = sharedPreferences.getInt("money", 0);
        int externalLife = sharedPreferences.getInt("life", 0);

        coinsTV.setText(String.valueOf(userBalance));
        externalLifeTV.setText(String.valueOf(externalLife));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences("local", Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("money", 10000);
        editor.putInt("life", 2);
        editor.apply();
    }
}
