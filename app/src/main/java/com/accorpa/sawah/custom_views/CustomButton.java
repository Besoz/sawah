package com.accorpa.sawah.custom_views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import com.accorpa.sawah.R;

/**
 * Created by Accorpa on 11/16/2015.
 */
public class CustomButton extends Button {
    public CustomButton(Context context) {
        super(context);
        applyUi();
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyUi();
        setFont(context, attrs);
    }

    private void applyUi()
    {
        super.setAllCaps(false);
    }
    private void setFont(Context context, AttributeSet attrs)
    {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.custom_attributes,
                0, 0);

        try {
            String fontFilePath = a.getString(R.styleable.custom_attributes_font_file_path);
            if (fontFilePath != null && !fontFilePath.equalsIgnoreCase(""))
            {
                fontFilePath = a.getString(R.styleable.custom_attributes_font_file_path);
                this.setTypeface(Typeface.createFromAsset(context.getAssets(),
                        fontFilePath));
            }
            else
            {
//                fontFilePath = getResources().getString(R.string.default_font);
            }
//            this.setTypeface(Typeface.createFromAsset(context.getAssets(),
//                    fontFilePath));

        } finally {
            a.recycle();
        }
    }

}
