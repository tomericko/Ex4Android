package com.example.tomer.ex4;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

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
    private EditText nameView;
    private EditText passwordView;
    private View loginFullView;
    private View loginInProgressView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_login,container, false);
        nameView = (EditText)rootView.findViewById(R.id.nameTxt);
        passwordView = (EditText)rootView.findViewById(R.id.passwordTxt);
        loginFullView = (View)rootView.findViewById(R.id.login_progress_bar);
        loginInProgressView = (View)rootView.findViewById(R.id.full_login);

        Button signInBtn = (Button)rootView.findViewById(R.id.loginBtn);
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

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


        String inputUsername = nameView.getText().toString();
        String inputPassword = passwordView.getText().toString();

        if(TextUtils.isEmpty(inputUsername)){
            nameView.setError(getString(R.string.error_field));
            focusView = nameView;
            cancel = true;
        }else if(TextUtils.isEmpty(inputPassword)){
            passwordView.setError(getString(R.string.error_field));
            focusView = passwordView;
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