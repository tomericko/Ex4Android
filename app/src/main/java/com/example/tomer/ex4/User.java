package com.example.tomer.ex4;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class User implements Serializable {

    private String userName;
    private String password;
    private String realName;
    private String email;
    private String icon;

    public User() {
        userName = new String();
        password = new String();
    }

    public User(JSONObject js) throws JSONException {
        try {
            userName = js.getString("username");
            password = js.getString("password");
            email = js.getString("email");
            icon = js.getString("icon");
            realName = js.getString("realname");
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
    public User(String name, String password, String email, String icon, String realname) throws JSONException {
            this.userName = name;
            this.password = password;
            this.email = email;
            this.realName = realname;
            this.icon = icon;
    }


    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getRealName() {
        return realName;
    }

    public String getEmail() {
        return email;
    }

    public String getIcon() {
        return icon;
    }
}
