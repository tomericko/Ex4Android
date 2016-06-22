package com.example.tomer.ex4;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.util.AttributeSet;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;

public class GifView extends View {

    Movie movie;
    long moviestart;

    public GifView(Context context) throws IOException {
        super(context);
    }
    public GifView(Context context, AttributeSet attrs) throws IOException{
        super(context, attrs);
    }
    public GifView(Context context, AttributeSet attrs, int defStyle) throws IOException {
        super(context, attrs, defStyle);
    }

    public void loadGIFResource(Context context, int drawable)
    {
        //turn off hardware acceleration
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        InputStream is=context.getResources().openRawResource(drawable);
        movie = Movie.decodeStream(is);
    }

    public void loadGIFAsset(Context context, String filename)
    {
        InputStream is;
        try {
            is = context.getResources().getAssets().open(filename);
            movie = Movie.decodeStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (movie == null) {
            return;
        }

        long now=android.os.SystemClock.uptimeMillis();

        if (moviestart == 0) moviestart = now;

        int relTime;
        relTime = (int)((now - moviestart) % movie.duration());
        movie.setTime(relTime);
        movie.draw(canvas,10,10);
        this.invalidate();
    }
}
