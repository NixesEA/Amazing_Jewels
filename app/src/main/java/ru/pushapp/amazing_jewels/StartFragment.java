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
import android.widget.Toast;

import androidx.navigation.Navigation;

public class StartFragment extends Fragment implements View.OnClickListener{

    ImageButton play;
    ImageButton leaders;
    ImageButton settings;
    ImageButton shop;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.start_fragment, container, false);

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

//                SharedPreferences sharedPreferences = getContext().getSharedPreferences("local", Context.MODE_MULTI_PROCESS);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putInt("money", 10000);
//                editor.putInt("life", 2);
//                editor.apply();

                Navigation.findNavController(view).navigate(R.id.action_startFragment_to_pauseFragment);
                Toast.makeText(getContext(), "way to game", Toast.LENGTH_SHORT).show();
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
