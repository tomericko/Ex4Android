package com.example.tomer.ex4;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {


    private EditText nameView;
    private EditText passwordView;
    private View loginFullView;
    private View loginInProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //changeActivity(R.id.loginBtn, LoginActivity.class);
        changeActivity(R.id.signupBtn, SignUp.class);

        Button btn = (Button) findViewById(R.id.loginBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

    }

    //set the listeners for the diffrent buttons in this activity
    private void changeActivity(int btnResource, final Class<?> activityClass) {
        Button btn = (Button) findViewById(btnResource);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, activityClass);
                startActivity(i);
            }
        });
    }

    private void attemptLogin() {
        boolean cancel = false;
        View focusView = null;
        nameView = (EditText) findViewById(R.id.nameTxt);
        passwordView = (EditText) findViewById(R.id.passwordTxt);
        loginFullView = (View) findViewById(R.id.full_login);
        loginInProgressView = (View) findViewById(R.id.login_progress_bar);
        UserLoginTask mAuthTask;
        String inputUsername = nameView.getText().toString();
        String inputPassword = passwordView.getText().toString();

        if (TextUtils.isEmpty(inputUsername)) {
            nameView.setError(getString(R.string.error_field));
            focusView = nameView;
            cancel = true;
        } else if (TextUtils.isEmpty(inputPassword)) {
            passwordView.setError(getString(R.string.error_field));
            focusView = passwordView;
            cancel = true;
        }


        if (cancel) {
            focusView.requestFocus();
        } else {
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            showProgress(true);

            mAuthTask = new UserLoginTask(inputUsername, inputPassword);
            mAuthTask.execute();
        }


    }


    private void showProgress(boolean b) {
        if (b) {
            loginFullView.setVisibility(View.GONE);
            loginInProgressView.setVisibility(View.VISIBLE);
        } else {
            loginFullView.setVisibility(View.VISIBLE);
            loginInProgressView.setVisibility(View.GONE);
        }
    }


    class UserLoginTask extends AsyncTask<Void, Void, User> {

        private final String name;
        private final String password;

        UserLoginTask(String name, String password) {
            this.name = name;
            this.password = password;
        }

        @Override
        protected User doInBackground(Void... params) {
            Log.i("doInBackground: ", Thread.currentThread().getName());
            JSONObject parameters = new JSONObject();
            try {

                URL url = new URL("http://10.0.2.2:8080/MyFormServlet?username="+this.name+"&password="+this.password);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                try {
                    //get the response from the server
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                    BufferedReader streamReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                    StringBuilder responesStr = new StringBuilder();

                    String input;
                    while ((input = streamReader.readLine()) != null) {
                        responesStr.append(input);
                    }

                    JSONObject jsonRespones = new JSONObject(responesStr.toString());

                    return new User(jsonRespones);

                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {

            }
            return null;

        }

        @Override
        protected void onPostExecute(final User user) {
            Log.i("doInBackground: ", Thread.currentThread().getName());
            showProgress(false);
            if(user!=null && user.getUserName()!=null){
                finish();
            }else{
                Toast.makeText(LoginActivity.this,"failed",Toast.LENGTH_LONG).show();
            }
        }

        protected void setParameters(HttpURLConnection connection,JSONObject params){
            try {
                OutputStream out = connection.getOutputStream();
                OutputStreamWriter writer = new OutputStreamWriter(out,"UTF-8");
                //connection.setRequestProperty("Content-Type","application/json");
                DataOutputStream data =  new DataOutputStream(out);
                //BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out,"UTF-8"));
                writer.write(params.toString());
                writer.flush();
                writer.close();
                out.close();
            }catch (IOException ioe){

            }
        }

    }

}