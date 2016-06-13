package com.example.tomer.ex4;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SignUp extends AppCompatActivity {


    private EditText emailTxt;
    private EditText passwordTxt;
    private EditText nameTxt;
    private Spinner iconSpinner;
    private EditText realnameTxt;
    private View registerFullView;
    private View registerInProgressView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Button btn = (Button) findViewById(R.id.registerBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
            }
        });

    }

    //set the listeners for the diffrent buttons in this activity
    private void changeActivity(int btnResource, final Class<?> activityClass) {
        Button btn = (Button) findViewById(btnResource);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUp.this, activityClass);
                startActivity(i);
            }
        });
    }

    private void attemptRegister() {
        boolean cancel = false;
        View focusView = null;
        nameTxt = (EditText) findViewById(R.id.nameTxt);
        passwordTxt = (EditText) findViewById(R.id.passwordTxt);
        emailTxt = (EditText) findViewById(R.id.emailTxt);
        realnameTxt = (EditText) findViewById(R.id.realnameTxt);
        iconSpinner = (Spinner) findViewById(R.id.iconSpinner);
        registerFullView = (View) findViewById(R.id.main_layout);
        registerInProgressView = (View) findViewById(R.id.register_progress_bar);
        UserSignUpTask mAuthTask;

        String inputUsername = nameTxt.getText().toString();
        String inputPassword = passwordTxt.getText().toString();
        String inputRealname = realnameTxt.getText().toString();
        String inputEmail = emailTxt.getText().toString();
        String inputIcon = iconSpinner.getSelectedItem().toString();

        if (TextUtils.isEmpty(inputUsername)) {
            nameTxt.setError(getString(R.string.error_field));
            focusView = nameTxt;
            cancel = true;
        } else if (TextUtils.isEmpty(inputPassword)) {
            passwordTxt.setError(getString(R.string.error_field));
            focusView = passwordTxt;
            cancel = true;
        } else if (TextUtils.isEmpty(inputRealname)) {
            realnameTxt.setError(getString(R.string.error_field));
            focusView = realnameTxt;
            cancel = true;
        } else if (TextUtils.isEmpty(inputPassword)) {
            emailTxt.setError(getString(R.string.error_field));
            focusView = emailTxt;
            cancel = true;
        } else if (TextUtils.isEmpty(inputPassword)) {
            TextView errorText = (TextView)iconSpinner.getSelectedView();
            errorText.setError(getString(R.string.error_field));
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText(getString(R.string.error_field));
            focusView = iconSpinner;
            cancel = true;
        }


        if (cancel) {
            focusView.requestFocus();
        } else {
            View view = this.getCurrentFocus();
            //disable the keyboard
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            showProgress(true);

            mAuthTask = new UserSignUpTask(inputUsername, inputPassword,inputEmail,inputIcon,inputRealname);
            mAuthTask.execute();
        }


    }


    private void showProgress(boolean b) {
        if (b) {
            registerFullView.setVisibility(View.GONE);
            registerInProgressView.setVisibility(View.VISIBLE);
        } else {
            registerFullView.setVisibility(View.VISIBLE);
            registerInProgressView.setVisibility(View.GONE);
        }
    }


    class UserSignUpTask extends AsyncTask<Void, Void, User> {

        private final String name;
        private final String password;
        private final String email;
        private final String realname;
        private final String icon;

        UserSignUpTask(String name, String password, String email, String icon, String realname) {
            this.name = name;
            this.password = password;
            this.email = email;
            this.realname = realname;
            this.icon = icon;
        }

        @Override
        protected User doInBackground(Void... params) {
            Log.i("doInBackground: ", Thread.currentThread().getName());
            JSONObject parameters = new JSONObject();
            try {

                URL url = new URL("http://10.0.2.2:8080/MyFormServlet?username=" + this.name + "&password=" + this.password+
                                    "&email="+this.email+"&realname="+ this.realname + "&icon="+this.icon);
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
                    if(jsonRespones.getString("answer").equals("successful")){
                        return new User(this.name,this.password,this.email,this.icon,this.realname);
                    }
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
            if (user != null && user.getUserName() != null) {
                finish();
            } else {
                Toast.makeText(SignUp.this, "failed", Toast.LENGTH_LONG).show();
            }
        }
    }

}