package ru.pushapp.amazing_jewels;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.navigation.Navigation;

public class LoginFragment extends Fragment implements  View.OnClickListener, View.OnFocusChangeListener{

    //1 - Log In screen
    //2 - Registration screen
    int STATE = 1;

    TextView label;
    TextView textOnBtn;

    TextInputEditText editName;
    TextInputEditText editEmail;
    TextInputEditText editPassword;

    TextInputLayout editPasswordLayout;
    TextInputLayout editEmailLayout;

    Button login;
    Button btnContinue;

    //todo Добавить галочки подтверждения для Name and Email

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);

        label = view.findViewById(R.id.text_login);
        textOnBtn = view.findViewById(R.id.btn_text);

        editPasswordLayout = view.findViewById(R.id.password_layout);
        editEmailLayout = view.findViewById(R.id.email_layout);

        editName = view.findViewById(R.id.nickname_login);
        editEmail = view.findViewById(R.id.email_login);
        editPassword = view.findViewById(R.id.password_login);

        login = view.findViewById(R.id.image_btn_login);
        btnContinue = view.findViewById(R.id.continue_btn_login);

        login.setOnClickListener(this);
        btnContinue.setOnClickListener(this);

        try {
            STATE = getArguments().getInt("login_state",1);
            editName.setOnFocusChangeListener(this);
            editEmail.setOnFocusChangeListener(this);
        } catch (NullPointerException ignored){}

        if (STATE == 2){
            label.setText("Registration");
            textOnBtn.setText("Log In");
            editEmailLayout.setVisibility(View.VISIBLE);
        }

        return view;
    }

    public boolean checkLogIn(){
        String typingName = String.valueOf(editName.getText());
        String typingPassword = String.valueOf(editPassword.getText());

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("local", Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", "");
        String userPassword = sharedPreferences.getString("userPassword", "");

        if (typingName.equals("")){
            editName.setError("Name can't be empty");
        }
        if (typingPassword.equals("")) {
            editPassword.setError("Password can't be empty");
            return false;
        }

        return typingName.equals(userName) && typingPassword.equals(userPassword);
    }

    private boolean checkName(){
        if (!String.valueOf(editName.getText()).equals("")){
            editName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.check_sm, 0);
            return true;
        } else {
            editName.setError("Name can't be empty");
            return false;
        }
    }

    private boolean checkEmail(){
        if (isEmailValid(String.valueOf(editEmail.getText()))){
            editEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.check_sm, 0);
            return true;
        } else {
            editEmail.setError("Email not valid");
            return false;
        }
    }

    public boolean saveLogIn(){
        String typingName = String.valueOf(editName.getText());
        String typingEmail = String.valueOf(editEmail.getText());
        String typingPassword = String.valueOf(editPassword.getText());

        checkName();
        checkEmail();

        if (typingPassword.equals("")){
            editPassword.setError("Password can't be empty");
            return false;
        }

        if (isEmailValid(typingEmail)){
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("local", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("userName", typingName);
            editor.putString("userEmail", typingEmail);
            editor.putString("userPassword", typingPassword);
            editor.apply();

            return true;
        } else {
            editEmail.setError("Email not valid");
            return false;
        }
    }

    public boolean isEmailValid(String email)
    {
        String regExpr =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpr, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        return matcher.matches();
    }

    private void goToRegistrationScreen(View view){
        Bundle bundle = new Bundle();
        bundle.putInt("login_state",2);
        Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_loginFragment2,bundle);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.continue_btn_login: {
                if (STATE == 1){
                    if (checkLogIn()){
                        Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_startFragment);
                    } else {
                        Toast.makeText(getContext(), "Ошибка авторизации\nПроверьте введенные логин и пароль", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (saveLogIn()){
//                        return To Log In Screen
                        getActivity().onBackPressed();
                    }
                }
                break;
            }
            case R.id.image_btn_login: {
                if (STATE == 1){
                    goToRegistrationScreen(view);
                } else {
//                    return To Log In Screen;
                    getActivity().onBackPressed();
                }

                break;
            }
        }
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        switch (view.getId()){
            case R.id.nickname_login:{
                if (!b){
                    checkName();
                } else {
                    editName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
                break;
            }
            case R.id.email_login:{
                if (!b){
                    checkEmail();
                } else {
                    editEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
                break;
            }
        }
    }
}
