package org.yekeqi.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by yekeqi on 2015/9/27.
 */
public class DancingSurfaceView extends SurfaceView implements SurfaceHolder.Callback2 {

    public DancingSurfaceView(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    public DancingSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
    }

    public DancingSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceRedrawNeeded(SurfaceHolder holder) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private void drawDancing(){
        Canvas canvas = null;
        try {
            canvas = getHolder().lockCanvas();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            if(canvas != null){
                getHolder().unlockCanvasAndPost(canvas);
            }
        }
    }
}
