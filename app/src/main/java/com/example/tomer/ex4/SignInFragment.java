package com.example.tomer.ex4;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SignInFragment extends Fragment {


    private View rootView;
    private EditText userNameTxt;
    private EditText passwordTxt;
    private View loginFullView;
    private View loginInProgressView;
    private ImageButton usernameResetBtn;
    private ImageButton passwordResetBtn;
    private Button signInBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_login,container, false);
        loginFullView = (View)rootView.findViewById(R.id.login_progress_bar);
        loginInProgressView = (View)rootView.findViewById(R.id.full_login);

        userNameTxt = (EditText)rootView.findViewById(R.id.usernameTxt);
        passwordTxt = (EditText)rootView.findViewById(R.id.passwordTxt);
        usernameResetBtn = (ImageButton) rootView.findViewById(R.id.usernameResetBtn);
        passwordResetBtn = (ImageButton) rootView.findViewById(R.id.passwordResetBtn);
        usernameResetBtn.setVisibility(View.INVISIBLE);
        passwordResetBtn.setVisibility(View.INVISIBLE);

        setRemovableET(userNameTxt, usernameResetBtn);
        setRemovableET(passwordTxt, passwordResetBtn);

        signInBtn = (Button)rootView.findViewById(R.id.loginBtn);
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        setButtonEnabled(signInBtn, false);
        // Inflate the layout for this fragment
        return rootView;
    }

    //set the listeners for the diffrent buttons in this activity
    private void changeActivity(int btnResource, final Class<?> activityClass){
        Button btn = (Button)rootView.findViewById(btnResource);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), activityClass);
                startActivity(i);
            }
        });
    }

    private void attemptLogin(){
        boolean cancel = false;
        View focusView = null;


        String inputUsername = userNameTxt.getText().toString();
        String inputPassword = passwordTxt.getText().toString();

        if(TextUtils.isEmpty(inputUsername)){
            userNameTxt.setError(getString(R.string.error_field));
            focusView = userNameTxt;
            cancel = true;
        }else if(TextUtils.isEmpty(inputPassword)){
            passwordTxt.setError(getString(R.string.error_field));
            focusView = passwordTxt;
            cancel = true;
        }


        if(cancel){
            focusView.requestFocus();
        }else{
            View view = getActivity().getCurrentFocus();
            if(view != null){
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(),0);
            }

            showProgress(true);
        }


    }


    private void showProgress(boolean b){
        if(b){
            loginFullView.setVisibility(View.GONE);
            loginInProgressView.setVisibility(View.VISIBLE);
        }else{
            loginFullView.setVisibility(View.VISIBLE);
            loginInProgressView.setVisibility(View.GONE);
        }
    }

    private void setRemovableET(final EditText et, final ImageView resetIB) {

        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && et.getText().toString().length() > 0)
                    resetIB.setVisibility(View.VISIBLE);
                else
                    resetIB.setVisibility(View.INVISIBLE);
            }
        });

        resetIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et.setText("");
                resetIB.setVisibility(View.INVISIBLE);
            }
        });

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0){
                    resetIB.setVisibility(View.VISIBLE);
                }else{
                    resetIB.setVisibility(View.INVISIBLE);
                }
                if (userNameTxt.getText().toString().equals("") || passwordTxt.getText().toString().equals(""))
                {
                    setButtonEnabled(signInBtn, false);
                }
                else
                {
                    setButtonEnabled(signInBtn, true);
                }
            }
        });
    }

    private void setButtonEnabled(Button btn, boolean enable)
    {
        if (enable)
        {
            btn.setBackgroundResource(R.drawable.enablebtn);
        }
        else
        {
            btn.setBackgroundResource(R.drawable.disablebtn);
        }
        btn.setEnabled(enable);
    }


}

 class UserLoginTask extends AsyncTask<Void,Void,User>{

    private final String name;
    private final String password;

    UserLoginTask(String name, String password){
        this.name = name;
        this.password = password;
    }

    @Override
    protected User doInBackground(Void... params) {
        Log.i("doInBackground: " ,Thread.currentThread().getName());
        try{
            URL url = new URL("localhost:8080");
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            try{
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader streamReader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
                StringBuilder responesStr = new StringBuilder();

                String input;
                while((input = streamReader.readLine())!=null){
                    responesStr.append(input);
                }

                JSONObject jsonRespones = new JSONObject(responesStr.toString());

                return new User(jsonRespones);

            }catch (IOException e){

            }finally {
                urlConnection.disconnect();
            }
        }catch (Exception e){

        }
        return null;
    }
}