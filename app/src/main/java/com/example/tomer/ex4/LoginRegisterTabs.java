package com.example.tomer.ex4;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Timer;
import java.util.TimerTask;


public class LoginRegisterTabs extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private CoordinatorLayout ml;
    private int[] tabIcons = {
            R.drawable.key_variant,
            R.drawable.plus,
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register_tabs);

        ml = (CoordinatorLayout) findViewById(R.id.main_layout);


        Timer timer = new Timer();
        MyTimer mt = new MyTimer();
        timer.schedule(mt, 50, 50);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
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


    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new SignInFragment(), "Sign In");
        adapter.addFrag(new SignUpFragment(), "Sing Up");
        viewPager.setAdapter(adapter);
    }


}
