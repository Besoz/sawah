package com.accorpa.sawah.Authorization;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.accorpa.sawah.BaseActivity;
import com.accorpa.sawah.Handlers.DataHandler;
import com.accorpa.sawah.Handlers.NavigationHandler;
import com.accorpa.sawah.R;
import com.accorpa.sawah.custom_views.CustomButton;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends BaseActivity implements ImageRequestListner{

    private final static int PICK_IMAGE_REQUEST = 100;

    private CircleImageView profileImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CustomButton editProfilePicButton = (CustomButton) findViewById(R.id.edit_profile_picture);
        editProfilePicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NavigationHandler.getInstance().startImagePickerForResult(EditProfileActivity.this,
                        PICK_IMAGE_REQUEST);
            }
        });

        profileImage = (CircleImageView) findViewById(R.id.profile_image);

//        Bitmap bitmap = DataHandler.getInstance(this).loadProfileImage();
//        if(bitmap != null){
//            profileImage.setImageBitmap(bitmap);
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null &&
                data.getData() != null) {

            try {
                DataHandler.getInstance(this).requestUpdateUserImage(this, this.getApplicationContext(),
                        data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_edit_profile;
    }

    @Override
    public void recieveBitmap(Bitmap bitmap) {
        profileImage.setImageBitmap(bitmap);
    }
}
