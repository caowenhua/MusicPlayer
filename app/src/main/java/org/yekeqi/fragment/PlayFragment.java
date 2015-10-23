package org.yekeqi.fragment;

import android.animation.Animator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.yekeqi.R;
import org.yekeqi.api.Params;
import org.yekeqi.service.MusicService;
import org.yekeqi.util.SpUtil;
import org.yekeqi.widget.MarqueeTextView;

import java.text.SimpleDateFormat;

/**
 * Created by yekeqi on 2015/9/23.
 */
public class PlayFragment extends BaseFragment implements View.OnClickListener{

    private ImageView img_needle;
    private ImageView img_disk;
    private SeekBar seekbar;
    private TextView tv_time;
    private MarqueeTextView tv_name;
    private ImageButton imgbtn_play;
    private ImageButton imgbtn_pre;
    private ImageButton imgbtn_next;
    private ImageButton imgbtn_loop;
//    private RotateAnimation animation;

    private boolean isAnimated;

    @Override
    protected String setTag() {
        return "PlayFragment";
    }

    @Override
    protected void findView() {
        img_disk = findViewByID(R.id.img_disk);
        img_needle = findViewByID(R.id.img_needle);
        seekbar = findViewByID(R.id.seekbar);
        tv_time = findViewByID(R.id.tv_time);
        tv_name = findViewByID(R.id.tv_name);
        imgbtn_next = findViewByID(R.id.imgbtn_next);
        imgbtn_pre = findViewByID(R.id.imgbtn_pre);
        imgbtn_play = findViewByID(R.id.imgbtn_play);
        imgbtn_loop = findViewByID(R.id.imgbtn_loop);
    }

    @Override
    protected int setLayout() {
        return R.layout.fragment_play;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
//        seekbar.set

        isAnimated = false;
        img_needle.setPivotX(0.0f);
        img_needle.setPivotY(0.0f);
        img_needle.setRotation(-90f);
//        animation = new RotateAnimation(0f, 360f);
//        animation.setDuration(5000);
//        animation.setRepeatMode(Animation.INFINITE);
//        img_disk.setAnimation(animation);
        seekbar.setMax(0);
        seekbar.setProgress(0);
        imgbtn_next.setOnClickListener(this);
        imgbtn_play.setOnClickListener(this);
        imgbtn_pre.setOnClickListener(this);
        imgbtn_loop.setOnClickListener(this);
        tv_name.setSelected(true);

        setupLoopButton(false);

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Intent intent = new Intent(getActivity(), MusicService.class);
                intent.putExtra(Params.OP, Params.OP_SEEK_TO);
                intent.putExtra(Params.POSITION, seekBar.getProgress());
                getActivity().startService(intent);
            }
        });
    }

    private void setupLoopButton(boolean showToast) {
        if(SpUtil.getIsLoop(getActivity())){
            imgbtn_loop.setImageResource(R.drawable.selector_btn_loop);
            if(showToast)
                showToast("循环播放");
        }
        else{
            imgbtn_loop.setImageResource(R.drawable.selector_btn_random);
            if(showToast)
                showToast("随机播放");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter durationFilter = new IntentFilter("seekBarUpdateBroadCast");
        getActivity().registerReceiver(durationChangeReceiver, durationFilter);
        IntentFilter playingFilter = new IntentFilter("playingChanged");
        getActivity().registerReceiver(playingChangeReceiver, playingFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(durationChangeReceiver);
        getActivity().unregisterReceiver(playingChangeReceiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgbtn_next:
                Intent intent = new Intent(getActivity(), MusicService.class);
                intent.putExtra(Params.OP, Params.OP_RIGHT_NEXT);
                getActivity().startService(intent);
                break;
            case R.id.imgbtn_pre:
                Intent intent2 = new Intent(getActivity(), MusicService.class);
                intent2.putExtra(Params.OP, Params.OP_LEFT_NEXT);
                getActivity().startService(intent2);
                break;
            case R.id.imgbtn_play:
                Intent intent3 = new Intent(getActivity(), MusicService.class);
                intent3.putExtra(Params.OP, Params.OP_PLAY);
                getActivity().startService(intent3);
                break;
            case R.id.imgbtn_loop:
                SpUtil.setIsLoop(getActivity(), !SpUtil.getIsLoop(getActivity()));
                setupLoopButton(true);
                break;
        }
    }

    private BroadcastReceiver durationChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent != null){
                int total = intent.getIntExtra(Params.TOTAL, 0);
                int duration = intent.getIntExtra(Params.DURATION, 0);
                String music = intent.getStringExtra(Params.PLAYING_NAME);
//                System.out.println(total + "--" + duration + "--" + music);
                tv_name.setText(music == null ? "" : music);
                seekbar.setMax(total);
                seekbar.setProgress(duration);
                if(total == 0 && duration == 0){
                    tv_time.setText("--:--  /  --:--");
                    imgbtn_play.setImageResource(R.drawable.selector_btn_play);
                    stopTuringAnimation();
                }
                else{
                    SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
                    tv_time.setText(formatter.format(duration) + "  /  " + formatter.format(total));
                    imgbtn_play.setImageResource(R.drawable.selector_btn_pause);
                    startTuringAnimation();
                }
            }
        }
    };

    private BroadcastReceiver playingChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent != null){
                if(intent.getBooleanExtra(Params.IS_PLAYING, false)){
                    imgbtn_play.setImageResource(R.drawable.selector_btn_pause);
                    startTuringAnimation();
                }
                else{
                    imgbtn_play.setImageResource(R.drawable.selector_btn_play);
                    stopTuringAnimation();
                }
            }
        }
    };

    private void startTuringAnimation(){
        if(!isAnimated){
            img_needle.animate().rotation(0f).setDuration(300).start();
            img_disk.animate().rotation(360.0f).setDuration(10000).setInterpolator(new LinearInterpolator())
                    .setListener(animatorListener).start();
            isAnimated = true;
        }
    }

    private void stopTuringAnimation(){
        if(isAnimated){
            img_needle.animate().rotation(-90f).setDuration(300).start();
            img_disk.animate().cancel();
            isAnimated = false;
        }
    }

    private Animator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if(img_disk.getRotation() == 360.0f){
                img_disk.setRotation(0.0f);
                img_disk.animate().rotation(360.0f).setDuration(10000).setInterpolator(new LinearInterpolator())
                        .setListener(animatorListener).start();
            }

        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };
}
