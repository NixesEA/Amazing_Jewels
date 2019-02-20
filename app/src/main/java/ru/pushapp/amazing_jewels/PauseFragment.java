package ru.pushapp.amazing_jewels;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.navigation.Navigation;

public class PauseFragment extends Fragment implements View.OnClickListener{

    ImageButton play;
    ImageButton leaders;
    ImageButton settings;
    ImageButton shop;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pause_fragment, container, false);

        play = view.findViewById(R.id.play_btn);
        play.setOnClickListener(this);

        leaders = view.findViewById(R.id.leaderboard_btn);
        leaders.setOnClickListener(this);

        shop = view.findViewById(R.id.shop_btn);
        shop.setOnClickListener(this);

        settings = view.findViewById(R.id.settings_btn);
        settings.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.play_btn:{
                getActivity().onBackPressed();
                //TODO
                break;
            }
            case R.id.leaderboard_btn:{
                Navigation.findNavController(view).navigate(R.id.action_startFragment_to_leaderBoardFragment);
                break;
            }
            case R.id.shop_btn:{
                Navigation.findNavController(view).navigate(R.id.action_startFragment_to_shopFragment);
                break;
            }
            case R.id.settings_btn:{
                Navigation.findNavController(view).navigate(R.id.action_startFragment_to_shopFragment);
                break;
            }

        }
    }
}
