package com.example.tomer.ex4;

import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Timer;

/**
 * Created by roi on 21/06/16.
 */
public class FirstFragment extends Fragment {
    private TextView messageTxt;
    private View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.first_fragment,container, false);
        messageTxt = (TextView) rootView.findViewById(R.id.logoTxt);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/INDIGO.TTF");
        messageTxt.setTypeface(font);
        Timer timer = new Timer();
        MyTimer mt = new MyTimer();
        timer.schedule(mt, 50, 50);
        //fragmentTxt = (TextView)rootView.findViewById(R.id.fragmentTxt);
        //GifView gifi = (GifView)rootView.findViewById(R.id.imageView);
        //gifi.loadGIFResource(getContext(), R.drawable.gifi);
        // Inflate the layout for this fragment
        return rootView;
    }
}
