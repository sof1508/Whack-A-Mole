package com.example.whac_a_mole;

import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

public class MyView extends View{


    private GestureDetector gDet;

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }





    @Override
    public boolean onTouchEvent (MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
        }
        return true;
    }

    private void setupGesture() {
        gDet = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                //Notified when a double-tap occurs
                return true;
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                //Notified when an event within a double-tap gesture occurs
                return true;
            }
        });

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gDet.onTouchEvent(motionEvent);
            }
        });
    }

    private void validate() { this.invalidate(); }
}
