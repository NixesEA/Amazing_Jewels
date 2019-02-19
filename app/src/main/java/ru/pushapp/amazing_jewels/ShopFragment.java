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
import android.widget.Toast;

public class ShopFragment extends Fragment implements View.OnClickListener {
    Button buy;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shop_fragment, container, false);

        buy = view.findViewById(R.id.shop_btn);
        buy.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        buyExternalLife();
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
