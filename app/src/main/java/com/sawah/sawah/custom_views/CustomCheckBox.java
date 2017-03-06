package com.sawah.sawah.custom_views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

/**
 * Created by root on 05/02/17.
 */

public class CustomCheckBox extends ImageButton {

    private int checkedResID, unCheckedResID, currentResID, nextResID;

    public CustomCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setBackgroundResIDs(int checkedResID, int unCheckedResID){

        this.checkedResID = checkedResID;
        this.unCheckedResID = unCheckedResID;

        currentResID = unCheckedResID;
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

    public void setChecked(){
        this.setBackgroundResource(checkedResID);

        currentResID = checkedResID;
        nextResID = unCheckedResID;
    }

    public void setUnChecked(){
        this.setBackgroundResource(unCheckedResID);

        currentResID = unCheckedResID;
        nextResID = checkedResID;
    }


}
