package com.accorpa.sawah;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.accorpa.sawah.Handlers.Utils;

public class GeneralInstructionActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Utils.getInstance().changeStatusBarColor(this);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_general_instruction;
    }

    @Override
    protected String getToolbarTitle()
    {
        return getString(R.string.user_guide);
    }
}
