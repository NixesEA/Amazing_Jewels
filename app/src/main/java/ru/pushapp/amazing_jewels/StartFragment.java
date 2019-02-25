package ru.pushapp.amazing_jewels;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.navigation.Navigation;

public class StartFragment extends Fragment implements View.OnClickListener, SharedPreferences.OnSharedPreferenceChangeListener {

    TextView countCoin;

    ImageButton play;
    ImageButton leaders;
    ImageButton shop;

    ImageButton shopAdd;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.start_fragment, container, false);

        countCoin = view.findViewById(R.id.count_coin);

        shopAdd = view.findViewById(R.id.add_btn);
        shopAdd.setOnClickListener(this);

        play = view.findViewById(R.id.play_btn);
        play.setOnClickListener(this);

        leaders = view.findViewById(R.id.leaderboard_btn);
        leaders.setOnClickListener(this);

        shop = view.findViewById(R.id.shop_btn);
        shop.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("local", Context.MODE_MULTI_PROCESS);
        int userBalance = sharedPreferences.getInt("money", 0);

        countCoin.setText(String.valueOf(userBalance));
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.play_btn: {
                Navigation.findNavController(view).navigate(R.id.action_startFragment_to_gameFragment);
                break;
            }
            case R.id.leaderboard_btn: {
                Navigation.findNavController(view).navigate(R.id.action_startFragment_to_leaderBoardFragment);
                break;
            }
            case R.id.shop_btn: {
                Navigation.findNavController(view).navigate(R.id.action_startFragment_to_shopFragment);
                break;
            }
            case R.id.add_btn: {
                Navigation.findNavController(view).navigate(R.id.action_startFragment_to_shopFragment);
                break;
            }

        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        int userBalance = sharedPreferences.getInt("money", 0);
        countCoin.setText(String.valueOf(userBalance));
    }
}
