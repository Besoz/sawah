package com.accorpa.sawah.custom_views;//package com.accorpa.sawah.custom_views;
//
//import android.content.Context;
//
///**
// * Created by root on 22/01/17.
// */
//
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.accorpa.sawah.R;


public class CustomProgressDialog extends ImageView {

    public CustomProgressDialog(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.setBackgroundResource(R.drawable.white_bird_progess_dialog);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        AnimationDrawable anim = (AnimationDrawable) this.getBackground();
        anim.start();
    }
}

////
////    public CustomProgressDialog(Context context, AttributeSet attrs, int defStyle) {
////        super(context, attrs, defStyle);
////        init();
////    }
////
////    public CustomProgressDialog(Context context, AttributeSet attrs) {
////        super(context, attrs);
////        init();
////    }
////
////    public CustomProgressDialog(Context context) {
////        super(context);
////        init();
////    }
////
////    private void init() {
////        setBackgroundResource(R.drawable.white_bird_progess_dialog);
////        final AnimationDrawable frameAnimation = (AnimationDrawable) getBackground();
////        post(new Runnable(){
////            public void run(){
////                frameAnimation.start();
////            }
////        });
////    }
////}
////
////    //    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//////    public CustomProgressDialog(Context context) {
//////        super(context);
//////
////////        this.setProgressDrawable(context.getDrawable(R.drawable.white_bird_progess_dialog));
//////
//////    }
////
//////    private ImageView imageview;
//////
//////    public CustomProgressDialog(Context context,int resourceIdOfImage) {
//////
//////        super(context, R.style.TransparentProgressDialog);
//////        WindowManager.LayoutParams windowmanger = getWindow().getAttributes();
//////        windowmanger .gravity = Gravity.CENTER_HORIZONTAL;
//////        getWindow().setAttributes(windowmanger );
//////        setTitle(null);
//////        setCancelable(false);
//////        setOnCancelListener(null);
//////        LinearLayout layout = new LinearLayout(context);
//////        layout.setOrientation(LinearLayout.VERTICAL);
//////        LinearLayout.LayoutParams params =  new LinearLayout.LayoutParams(80, 80);
//////
//////        imageview = new ImageView(context);
//////        imageview.setBackgroundResource(R.drawable.white_bird_progess_dialog);
//////        layout.addView(imageview, params);
//////        addContentView(layout, params);
//////    }
//////
//////    @Override
//////    public void show() {
//////        super.show();
//////
//////
//////    }
////
//////}
////
//import android.app.Dialog;
//import android.content.Context;
//import android.graphics.drawable.AnimationDrawable;
//import android.view.Gravity;
//import android.view.WindowManager;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//
//import com.accorpa.sawah.R;
//
//public class CustomProgressDialog extends Dialog {
//    private ImageView imageview;
//     AnimationDrawable frameAnimation;
//
//    public CustomProgressDialog(Context context,int resourceIdOfImage) {
//        super(context, R.style.TransparentProgressDialog);
//        WindowManager.LayoutParams windowmanger = getWindow().getAttributes();
//        windowmanger .gravity = Gravity.CENTER_HORIZONTAL;
//        getWindow().setAttributes(windowmanger );
//        setTitle(null);
//        setCancelable(false);
//        setOnCancelListener(null);
//        LinearLayout layout = new LinearLayout(context);
//        layout.setOrientation(LinearLayout.VERTICAL);
//        LinearLayout.LayoutParams params = new  LinearLayout.LayoutParams(80, 80);
//        imageview = new ImageView(context);
//        imageview.setBackgroundResource(R.drawable.white_bird_progess_dialog);
//        frameAnimation =
//                (AnimationDrawable) imageview.getBackground();
//        imageview.setBackgroundDrawable(frameAnimation);
//        layout.addView(imageview, params);
//        addContentView(layout, params);
//    }
//
//    @Override
//    public void show() {
//        super.show();
//
//        imageview.post(new Runnable() {
//                            @Override
//                            public void run() {
//
//
//                                frameAnimation.start();
//                            }
//        });
//
//    }
//
//}
//
////                /        AnimationDrawable frameAnimation = (AnimationDrawable)imageview.getBackground();
////        frameAnimation.start();


