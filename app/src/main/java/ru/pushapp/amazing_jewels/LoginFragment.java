package ru.pushapp.amazing_jewels;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.navigation.Navigation;

public class LoginFragment extends Fragment implements  View.OnClickListener{

    int State = 1;

    TextView label;
    TextView textOnBtn;

    TextInputEditText editName;
    TextInputEditText editEmail;
    TextInputEditText editPassword;

    TextInputLayout editEmailLayout;

    Button login;

    //todo Добавить галочки подтверждения для Name and Email

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);

        label = view.findViewById(R.id.text_login);
        textOnBtn = view.findViewById(R.id.btn_text);

        editName = view.findViewById(R.id.nickname_login);
        editEmail = view.findViewById(R.id.email_login);
        editPassword = view.findViewById(R.id.password_login);

        editEmailLayout = view.findViewById(R.id.email_layout);

        login = view.findViewById(R.id.image_btn_login);
        login.setOnClickListener(this);

        try {
            State = getArguments().getInt("login_state",1);
        } catch (NullPointerException ignored){}

        if (State == 2){
            label.setText("Registration");
            textOnBtn.setText("Registration");
            editEmailLayout.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt("login_state",1);
        if (State == 1){
            bundle.putInt("login_state",2);
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_loginFragment2,bundle);
        } else {
            bundle.putInt("login_state",1);
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_loginFragment2,bundle);
        }

    }
}
