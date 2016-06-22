package com.example.tomer.ex4;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    int counter;
    ProgressBar progressBar;
    Random rand = new Random();
    RelativeLayout rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        rl = (RelativeLayout) findViewById(R.id.main_activity);

        Timer timer = new Timer();
        MyTimer mt = new MyTimer();
        timer.schedule(mt, 50, 50);
        HideActionBar();
        this.progressBar = (ProgressBar)findViewById(R.id.bar);
        startProgressBar();
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
                    mDrawable.getPaint().setShader(new LinearGradient( 0, 0, 0, rl.getHeight(), Color.HSVToColor(colors1),
                            Color.HSVToColor(colors2), Shader.TileMode.CLAMP));
                    rl.setBackgroundDrawable(mDrawable);

                }
            });
        }
    }

    private void HideActionBar(){
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


    }



    private void startProgressBar(){
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {


                while (counter < 100){
                    counter += rand.nextInt(25);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(counter);
                        }
                    });

                    try {
                        Thread.sleep(1000);
                    }catch (InterruptedException e){
                    }
                }//end of loop


                Intent intent = new Intent(SplashActivity.this, LoginRegisterTabs.class);
                startActivity(intent);
            }
        });
        t.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
