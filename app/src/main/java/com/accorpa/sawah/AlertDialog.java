package com.accorpa.sawah;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.accorpa.sawah.Handlers.NavigationHandler;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;

public class AlertDialog extends AppCompatActivity {

    public static final int ADD_PLACE_DONE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_dialog);

        int dialogID = (int) getIntent().getSerializableExtra("DialogID");


        switch (dialogID) {

            case ADD_PLACE_DONE:

                new MaterialDialog.Builder(this)
                        .title(R.string.done_text)
                        .content(R.string.add_new_place_request_sent)
                        .positiveText(R.string.agree)
                        .autoDismiss(true)
                        .dismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                NavigationHandler.getInstance().startCategoriesListActivity(AlertDialog.this);
                                finish();
                            }
                        })
                        .titleGravity(GravityEnum.CENTER)
                        .contentGravity(GravityEnum.CENTER)
                        .show();

                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
                break;
            case 9:
                break;
            case 11:
                break;
            case 12:
                break;
            default:
                break;
        }
    }
}
