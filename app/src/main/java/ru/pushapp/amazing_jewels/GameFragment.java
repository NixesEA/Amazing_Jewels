package ru.pushapp.amazing_jewels;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

public class GameFragment extends Fragment implements GameView.OnCustomListener, PauseFragment.OnResultListener{

    GameView gameView;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    FrameLayout frame;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.game_fragment, container, false);

        gameView = view.findViewById(R.id.game_view);
        gameView.setCustomListener(this);

        sharedPreferences = getContext().getSharedPreferences("local", Context.MODE_MULTI_PROCESS);
        editor = sharedPreferences.edit();

        frame = view.findViewById(R.id.pause_frame);

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

        if (externalLife <= 0){
            PauseFragment pauseFragment = new PauseFragment();
            pauseFragment.setOnResultListener(this);

            Bundle bundle = new Bundle();
            bundle.putString("label", "Play Again");
            pauseFragment.setArguments(bundle);

            frame.setVisibility(View.VISIBLE);
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.pause_frame, pauseFragment);
            fragmentTransaction.commitNow();
        }
    }

    @Override
    public void playAgain() {
        editor.putInt("life", 5);
        editor.apply();

        gameView.generateGameSet();

        frame.setVisibility(View.GONE);
        Toast.makeText(getContext(), "playAgain", Toast.LENGTH_SHORT).show();
    }

}
