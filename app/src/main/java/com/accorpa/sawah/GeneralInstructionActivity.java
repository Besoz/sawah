package com.accorpa.sawah;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.accorpa.sawah.Handlers.DataHandler;
import com.accorpa.sawah.Handlers.Utils;
import com.accorpa.sawah.custom_views.CustomTextView;

public class GeneralInstructionActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Utils.getInstance().changeStatusBarColor(this);

        String advice = DataHandler.getInstance(this).getDefaultCity().getAdvices();
        String numbers = DataHandler.getInstance(this).getDefaultCity().getNumbers();

        CustomTextView ct = (CustomTextView) findViewById(R.id.general_text);
        System.out.println(advice);
        System.out.println(DataHandler.getInstance(this).getDefaultCity().getCityNameEN());
        ct.setText(advice);
        CustomTextView ct2 = (CustomTextView) findViewById(R.id.numbers_text);
        ct2.setText(numbers);
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
