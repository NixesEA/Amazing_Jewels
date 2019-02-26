package ru.pushapp.amazing_jewels.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import androidx.navigation.Navigation;
import ru.pushapp.amazing_jewels.model.LeaderUnit;
import ru.pushapp.amazing_jewels.R;
import ru.pushapp.amazing_jewels.model.RecyclerViewAdapter;

public class LeaderBoardFragment extends Fragment implements View.OnClickListener, SharedPreferences.OnSharedPreferenceChangeListener {

    TextView countCoin;

    ImageButton backBtn;
    ImageButton shopBtn;

    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.leader_bord_fragment, container, false);

        countCoin = view.findViewById(R.id.count_coin);

        backBtn = view.findViewById(R.id.arrow_back);
        backBtn.setOnClickListener(this);

        shopBtn = view.findViewById(R.id.add_btn);
        shopBtn.setOnClickListener(this);

        ArrayList<LeaderUnit> leaderList = getArrayList();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getContext(), leaderList);

        recyclerView = view.findViewById(R.id.leader_rv);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

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

    /**
     * Save and get ArrayList in SharedPreference
     */
    public void saveArrayList(ArrayList<LeaderUnit> list) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("local", Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.arrow_back: {
                getActivity().onBackPressed();
                break;
            }
            case R.id.add_btn: {
                Navigation.findNavController(view).navigate(R.id.action_leaderBoardFragment_to_shopFragment);
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
