package com.accorpa.sawah.custom_views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Accorpa on 11/16/2015.
 */
public class CustomEditText extends EditText {
    public CustomEditText(Context context) {
        super(context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void setUI(Context context)
    {
    }
}
