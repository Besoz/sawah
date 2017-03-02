package com.accorpa.sawah.custom_views;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;

import com.accorpa.sawah.R;

/**
 * Created by root on 25/02/17.
 */

public class ExpandArrow extends ImageButton {

    private float deg = 0;
    public ExpandArrow(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
//    public ExpandArrow(Context context, AttributeSet attrs) {
//        super(context, attrs);
//
////        this.setBackgroundResource(R.drawable.expand_arrow_48);
//
//    }


    @Override
    public boolean callOnClick() {
        boolean superDone =  super.callOnClick();

//        deg += 180F;
//        deg = deg%360;
//
//        this.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());

        return superDone;
    }
}
