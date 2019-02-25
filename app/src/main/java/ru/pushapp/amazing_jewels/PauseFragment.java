package ru.pushapp.amazing_jewels;

import android.app.Activity;
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

public class PauseFragment extends Fragment implements View.OnClickListener{

    ImageButton play;
    ImageButton leaders;

    ImageButton shop;
    TextView pause;

    OnResultListener callback;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pause_fragment, container, false);

        pause = view.findViewById(R.id.pause_txt);
        String msg = getArguments().getString("label");
        pause.setText(msg);

        play = view.findViewById(R.id.play_btn);
        play.setOnClickListener(this);

        leaders = view.findViewById(R.id.leaderboard_btn);
//        leaders.setOnClickListener(this);

        shop = view.findViewById(R.id.shop_btn);
//        shop.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.play_btn:{
                //TODO
                callback.playAgain();
//                getActivity().onBackPressed();
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

        }
    }

    public void setOnResultListener(Fragment fragment) {
        callback = (OnResultListener) fragment;
    }

    public interface OnResultListener {
        void playAgain();
    }
}
