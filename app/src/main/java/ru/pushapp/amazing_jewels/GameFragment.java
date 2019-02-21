package ru.pushapp.amazing_jewels;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class GameFragment extends Fragment implements GameView.OnCustomListener{

    GameView gameView;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.game_fragment, container, false);

        gameView = view.findViewById(R.id.game_view);
        gameView.setCustomListener(this);

        sharedPreferences = getContext().getSharedPreferences("local", Context.MODE_MULTI_PROCESS);
        editor = sharedPreferences.edit();

        return view;
    }

    @Override
    public void saveReward(int coins) {
        int userBalance = coins + sharedPreferences.getInt("money", 0);

        editor.putInt("money", userBalance);
        editor.apply();
    }

    @Override
    public void removeLife() {
        int externalLife = sharedPreferences.getInt("life", 0);

        editor.putInt("life", --externalLife);
        editor.apply();
    }
}
