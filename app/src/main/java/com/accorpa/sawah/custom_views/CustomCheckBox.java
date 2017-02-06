package com.accorpa.sawah.custom_views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

/**
 * Created by root on 05/02/17.
 */

public class CustomCheckBox extends ImageButton {

    private int currentResID, nextResID;

    public CustomCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setBackResIDs(int checkedResID, int unCheckResID){
        currentResID = unCheckResID;
        nextResID = checkedResID;

        this.setBackgroundResource(this.currentResID);

    }
    public void toggleState(){
        this.setBackgroundResource(nextResID);
//        shifting
        int temp = nextResID;
        nextResID = currentResID;
        currentResID = temp;
    }


}
