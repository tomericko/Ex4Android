package com.example.tomer.ex4;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.Timer;
import java.util.TimerTask;

public class IntroductionScreen extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private LinearLayout ml;
    private int[] tabIcons = {
            R.drawable.circle_off,
            R.drawable.circle_on,
    };
    private Button nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction_screen);

        ml = (LinearLayout) findViewById(R.id.main_layout);

        Timer timer = new Timer();
        MyTimer mt = new MyTimer();
        timer.schedule(mt, 50, 50);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.transparent));
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        nextBtn = (Button) findViewById(R.id.next_skip_btn);
        nextBtn.setText("Next");
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int curItm = viewPager.getCurrentItem();
                int countItms = viewPager.getAdapter().getCount() - 1;
                if (curItm < countItms)
                {
                    curItm++;
                    viewPager.setCurrentItem(curItm);
                }
                else if (curItm == countItms)
                {
                    nextBtn.setText("You Skipped!!");
                }
            }
        });

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
        tabLayout.setOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {

                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        super.onTabSelected(tab);
                        tab.setIcon(tabIcons[1]);
                        int curItm = viewPager.getCurrentItem();
                        int countItms = viewPager.getAdapter().getCount() - 1;
                        if (curItm < countItms)
                        {
                            nextBtn.setText("Next");
                            setButtonStyle(nextBtn,false);
                            nextBtn.setTextColor(getResources().getColor(R.color.translucent_grey));
                        }
                        else if (curItm == countItms)
                        {
                            nextBtn.setText("Skip");
                            setButtonStyle(nextBtn,true);
                            nextBtn.setTextColor(getResources().getColor(R.color.white));
                        }
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        super.onTabUnselected(tab);
                        tab.setIcon(tabIcons[0]);
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        super.onTabReselected(tab);
                    }
                }
        );
        tabLayout.getTabAt(0).setIcon(tabIcons[1]);
        tabLayout.getTabAt(1).setIcon(tabIcons[0]);
        tabLayout.getTabAt(2).setIcon(tabIcons[0]);
        tabLayout.getTabAt(3).setIcon(tabIcons[0]);
        tabLayout.getTabAt(4).setIcon(tabIcons[0]);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new FirstFragment(), "");
        adapter.addFrag(new FirstFragment(), "");
        adapter.addFrag(new FirstFragment(), "");
        adapter.addFrag(new FirstFragment(), "");
        adapter.addFrag(new FirstFragment(), "");
        viewPager.setAdapter(adapter);
    }

    private void setButtonStyle(Button btn, boolean enable)
    {
        if (enable)
        {
            btn.setBackgroundResource(R.drawable.enablebtn);
        }
        else
        {
            btn.setBackgroundResource(R.drawable.disablebtn);
        }
    }


}
