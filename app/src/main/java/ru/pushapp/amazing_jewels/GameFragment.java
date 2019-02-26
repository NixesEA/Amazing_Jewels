package ru.pushapp.amazing_jewels;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

public class GameFragment extends Fragment implements GameView.OnCustomListener, PauseFragment.OnResultListener, View.OnClickListener,SharedPreferences.OnSharedPreferenceChangeListener{

    int SCORE = 0;
    int DEFAULT_LIFE_COUNT = 5;

    ImageButton pause_btn;
    TextView scoreCount;
    TextView lifeCount;

    GameView gameView;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    FrameLayout frame;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.game_fragment, container, false);

        pause_btn = view.findViewById(R.id.pause_btn);
        pause_btn.setOnClickListener(this);

        scoreCount = view.findViewById(R.id.game_score_tv);
        lifeCount = view.findViewById(R.id.count_life);

        gameView = view.findViewById(R.id.game_view);
        gameView.setCustomListener(this);

        sharedPreferences = getContext().getSharedPreferences("local", Context.MODE_MULTI_PROCESS);
        editor = sharedPreferences.edit();

        frame = view.findViewById(R.id.pause_frame);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        int externalLife = sharedPreferences.getInt("life", DEFAULT_LIFE_COUNT);

        scoreCount.setText(String.valueOf(0));
        lifeCount.setText(String.valueOf(externalLife));

        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void saveReward(int coins) {
        SCORE += coins;
        scoreCount.setText(String.valueOf(SCORE));

        int userBalance = coins + sharedPreferences.getInt("money", 0);

        editor.putInt("money", userBalance);
        editor.apply();
    }

    @Override
    public void removeLife() {
        int externalLife = sharedPreferences.getInt("life", DEFAULT_LIFE_COUNT);

        editor.putInt("life", --externalLife);
        editor.apply();

        if (externalLife <= 0){
            saveResult();
            startPauseFragment(1);
        }
    }

    @Override
    public void onPause() {
        saveResult();
        super.onPause();
    }

    private void saveResult() {
        ArrayList<LeaderUnit> list = getArrayList();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("local", Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", "User Name");

        LeaderUnit leaderUnit = new LeaderUnit(userName, SCORE);
        list.add(leaderUnit);
        saveArrayList(list);
    }

    /**
     * Save and get ArrayList in SharedPreference
     */
    public void saveArrayList(ArrayList<LeaderUnit> list) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("local", Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();

        Collections.sort(list);
        while (list.size() > 8){
            list.remove(8);
        }

        String json = gson.toJson(list);
        editor.putString("leaderBoard", json);
        editor.apply();
    }

    public ArrayList<LeaderUnit> getArrayList() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("local", Context.MODE_MULTI_PROCESS);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("leaderBoard", null);
        Type type = new TypeToken<ArrayList<LeaderUnit>>() {
        }.getType();

        ArrayList<LeaderUnit> leaderList = gson.fromJson(json, type);
        int listSize = 7;
        try {
            listSize = 7 - leaderList.size();
        } catch (NullPointerException ignored) {
            leaderList = new ArrayList<>();
        }

        while (listSize > 0) {
            leaderList.add(new LeaderUnit("User " + (8 - listSize), 1000 * listSize));
            listSize--;
        }
        saveArrayList(leaderList);

        return leaderList;
    }

    @Override
    public void playAgain() {
        SCORE = 0;
        editor.putInt("life", DEFAULT_LIFE_COUNT);
        editor.apply();

        gameView.generateGameSet();

        frame.setVisibility(View.GONE);
    }

    @Override
    public void resumeGame() {
        frame.setVisibility(View.GONE);
    }

    private void startPauseFragment(int state) {
        PauseFragment pauseFragment = new PauseFragment();
        pauseFragment.setOnResultListener(this);

        Bundle bundle = new Bundle();
        bundle.putInt("statePause", state);
        pauseFragment.setArguments(bundle);

        frame.setVisibility(View.VISIBLE);
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.pause_frame, pauseFragment);
        fragmentTransaction.commitNow();
    }

    @Override
    public void onClick(View view) {
        startPauseFragment(0);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        int externalLife = sharedPreferences.getInt("life", DEFAULT_LIFE_COUNT);

        lifeCount.setText(String.valueOf(externalLife));
    }
}
