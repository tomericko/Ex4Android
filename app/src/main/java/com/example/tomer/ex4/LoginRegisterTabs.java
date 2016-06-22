package com.example.tomer.ex4;


import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;


public class LoginRegisterTabs extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView logoTxt;
    private LinearLayout ml;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register_tabs);

        ml = (LinearLayout) findViewById(R.id.main_layout);

        logoTxt = (TextView) findViewById(R.id.logoTxt);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/INDIGO.TTF");
        logoTxt.setTypeface(font);
        Timer timer = new Timer();
        MyTimer mt = new MyTimer();
        timer.schedule(mt, 50, 50);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    class MyTimer extends TimerTask {

        float[] colors1 = {250, 40, 100};
        float[] colors2 = {120, 40, 100};

        public void run() {
            runOnUiThread(new Runnable() {

                public void run() {
                    colors1[0] = (colors1[0] + 1) % 360;
                    colors2[0] = (colors2[0] + 1) % 360;
                    ShapeDrawable mDrawable = new ShapeDrawable(new RectShape());
                    mDrawable.getPaint().setShader(new LinearGradient(0, 0, 0, ml.getHeight(), Color.HSVToColor(colors1),
                            Color.HSVToColor(colors2), Shader.TileMode.CLAMP));
                    ml.setBackgroundDrawable(mDrawable);

                }
            });
        }
    }



    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new SignInFragment(), "Login");
        adapter.addFrag(new SignUpFragment(), "Register");
        viewPager.setAdapter(adapter);
    }


}
