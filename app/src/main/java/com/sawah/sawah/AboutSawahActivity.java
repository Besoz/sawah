package com.sawah.sawah;

import android.os.Bundle;
import android.view.View;

import com.sawah.sawah.Handlers.SharingHandler;
import com.sawah.sawah.Handlers.URLHandler;
import com.sawah.sawah.Handlers.Utils;

public class AboutSawahActivity extends BaseActivity {

    SharingHandler sharingHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.getInstance().changeStatusBarColor(this);
        sharingHandler = SharingHandler.getInstance();

        findViewById(R.id.open_website_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharingHandler.openWebsite(AboutSawahActivity.this,
                        URLHandler.getInstance(AboutSawahActivity.this).getServerpath());
            }
        });

        findViewById(R.id.share_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharingHandler.share(AboutSawahActivity.this, getString(R.string.share_msg_a));
            }
        });

        findViewById(R.id.open_twitter_page_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharingHandler.openTwitterPage(AboutSawahActivity.this);
            }
        });
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_about_sawah;
    }

    @Override
    protected String getToolbarTitle() {
        return getString(R.string.about_sawah_app);
    }
}
