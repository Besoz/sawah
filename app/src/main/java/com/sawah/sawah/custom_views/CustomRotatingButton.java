package com.sawah.sawah.custom_views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;

/**
 * Created by root on 05/02/17.
 */

public class CustomRotatingButton extends ImageButton {

    private int checkedResID, unCheckedResID, currentResID, nextResID;

    public CustomRotatingButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private int mCurrRotation = 0;

    @Override
    public void setOnClickListener(OnClickListener l) {
        super.setOnClickListener(l);



    }

    public void rotate() {

        mCurrRotation %= 360;
        float fromRotation = mCurrRotation;
        float toRotation = mCurrRotation += 180;

        final RotateAnimation rotateAnim = new RotateAnimation(
                fromRotation, toRotation, this.getWidth()/2, this.getHeight()/2);

        rotateAnim.setDuration(200); // Use 0 ms to rotate instantly
        rotateAnim.setFillAfter(true); // Must be true or the animation will reset

        this.startAnimation(rotateAnim);
    }
}
