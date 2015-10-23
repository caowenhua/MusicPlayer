package org.yekeqi.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import org.yekeqi.R;

/**
 * Created by yekeqi on 2015/9/25.
 */
public class LoadingDialog extends Dialog {

    private TextView tv_msg;
//    private ImageView img_pb;
    private Drawable[] drawables;

    public LoadingDialog(Context context, String text) {
        super(context, R.style.myDialog);
        setContentView(R.layout.dialog_loading);

        tv_msg = (TextView) findViewById(R.id.tv_msg);
//        img_pb = (ImageView) findViewById(R.id.img_pb);

//        drawables = new Drawable[]{getContext().getResources().getDrawable(R.mipmap.fx_audio_mode_1),
//                getContext().getResources().getDrawable(R.mipmap.fx_audio_mode_2),
//                getContext().getResources().getDrawable(R.mipmap.fx_audio_mode_3),
//                getContext().getResources().getDrawable(R.mipmap.fx_audio_mode_4),
//                getContext().getResources().getDrawable(R.mipmap.fx_audio_mode_5),
//                getContext().getResources().getDrawable(R.mipmap.fx_audio_mode_6)};
//        img_pb.setAnimation();
        tv_msg.setText(text);
    }

    @Override
    public void show() {
        try{
            super.show();
        }
        catch (Exception e){

        }
    }

    @Override
    public void dismiss() {
        try{
            super.dismiss();
        }
        catch (Exception e){

        }
    }

    public void setText(final String text){
        if(isShowing()){
            Message msg = new Message();
            msg.what = 0;
            msg.obj = text;
            handler.sendMessage(msg);
//            getOwnerActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    tv_msg.setText(text);
//                }
//            });
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0){
                tv_msg.setText((String) msg.obj);
            }
        }
    };
}
