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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ShopFragment extends Fragment implements View.OnClickListener,SharedPreferences.OnSharedPreferenceChangeListener {
    Button buy;
    ImageButton shopBtn;
    ImageButton backBtn;

    TextView countCoin;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shop_fragment, container, false);

        countCoin = view.findViewById(R.id.count_coin);

        backBtn = view.findViewById(R.id.arrow_back);
        backBtn.setOnClickListener(this);

        shopBtn = view.findViewById(R.id.add_btn);
        shopBtn.setOnClickListener(this);

        buy = view.findViewById(R.id.buy_life_btn);
        buy.setOnClickListener(this);

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
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        int userBalance = sharedPreferences.getInt("money", 0);

        countCoin.setText(String.valueOf(userBalance));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_btn:{
//                Navigation.findNavController(view).navigate(R.id.action_startFragment_to_shopFragment);
                break;
            }
            case R.id.buy_life_btn:{
                buyExternalLife();
                break;
            }
            case R.id.arrow_back:{
                getActivity().onBackPressed();
                break;
            }
        }
    }

    private void buyExternalLife() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("local", Context.MODE_MULTI_PROCESS);
        int userMoney = sharedPreferences.getInt("money", 0);
        int currentCountExternalLife = sharedPreferences.getInt("life", 0);

        if (userMoney >= 500){
            userMoney -= 500;
            currentCountExternalLife++;

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("money", userMoney);
            editor.putInt("life", currentCountExternalLife);
            editor.apply();

            Toast.makeText(getContext(), "Куплена 1 дополнительная жизнь", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getActivity(),"Нужно больше золота", Toast.LENGTH_LONG).show();
        }
    }
}
