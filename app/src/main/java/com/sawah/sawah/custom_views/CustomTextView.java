package com.sawah.sawah.custom_views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.sawah.sawah.R;


/**
 * Created by Accorpa on 11/16/2015.
 */
public class CustomTextView extends TextView {
    private Boolean setFontTest = true;
    public CustomTextView(Context context) {
        super(context);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setFont(context, attrs);
    }

    public void setSetFontTest(Boolean setFontTest) {
        this.setFontTest = setFontTest;
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
                fontFilePath = getResources().getString(R.string.default_font);
                if(this.getTypeface().isBold())
                    fontFilePath = getResources().getString(R.string.default_font_bold);
                if(this.getTypeface().isItalic())
                    fontFilePath = getResources().getString(R.string.default_font_light);
                this.setTypeface(Typeface.createFromAsset(context.getAssets(),
                    fontFilePath));
            }


        } finally {
            a.recycle();
        }
    }
}
