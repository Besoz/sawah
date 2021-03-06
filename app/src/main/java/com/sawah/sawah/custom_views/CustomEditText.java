package com.sawah.sawah.custom_views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

import com.sawah.sawah.R;

/**
 * Created by Accorpa on 11/16/2015.
 */
public class CustomEditText extends EditText {
    public CustomEditText(Context context) {
        super(context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont(context, attrs);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFont(context, attrs);
    }

    private void setUI(Context context)
    {
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
                this.setTypeface(Typeface.createFromAsset(context.getAssets(),
                        fontFilePath));
            }


        } finally {
            a.recycle();
        }
    }
}
