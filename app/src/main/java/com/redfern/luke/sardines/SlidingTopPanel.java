package com.redfern.luke.sardines;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.logging.Handler;

/**
 * Created by luke on 18/03/16.
 */
public class SlidingTopPanel extends RelativeLayout {
    Context context;
    public SlidingTopPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    ImageButton dragButton;

    private static final int INVALID_POINTER_ID = -1;
    private int mActivePointerId = INVALID_POINTER_ID;
    float mLastTouchY;

    LinearLayout.LayoutParams params;
    int startHeight;
    int currentHeight;
    float dy;

    float minHeight;
    float maxHeight;
    float closeBottomHeight;
    float screenHeight;
    float bottomOffset;
    float bottomPanelMinHeight;
    float bottomPanelHeight;

    Animation animation;

    Resources res;

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        res = getResources();
        minHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, res.getDisplayMetrics());
        bottomPanelMinHeight= TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, res.getDisplayMetrics());
        closeBottomHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, res.getDisplayMetrics());
        calculateOffsets();

        params = (LinearLayout.LayoutParams) getLayoutParams();

        dragButton = (ImageButton) findViewById(R.id.drag_top_panel);
        dragButton.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int action = MotionEventCompat.getActionMasked(event);

                switch (action) {
                    case MotionEvent.ACTION_DOWN: {
                        if (animation!=null){
                            animation.cancel();
                        }
                        bottomPanelHeight = ((MainActivity) context).getBottomPanelHeight();
                        final int pointerIndex = MotionEventCompat.getActionIndex(event);
                        final float y = MotionEventCompat.getY(event, pointerIndex);
                        dy = 0;

                        // Remember where we started (for dragging)
                        mLastTouchY = y;
                        // Save the ID of this pointer (for dragging)
                        mActivePointerId = MotionEventCompat.getPointerId(event, 0);
                        // Layout Params
                        params = (LinearLayout.LayoutParams) getLayoutParams();
                        startHeight = params.height;
                        break;
                    }

                    case MotionEvent.ACTION_MOVE: {
                        invalidate();
                        // Find the index of the active pointer and fetch its position
                        final int pointerIndex =
                                MotionEventCompat.findPointerIndex(event, mActivePointerId);

                        final float y = MotionEventCompat.getY(event, pointerIndex);

                        // Calculate the distance moved
                        dy = y - mLastTouchY + dy;

                        //mPosY += dy;



                        // Remember this touch position for the next move event
                        mLastTouchY = y;
                        //Log.d("Touch", String.valueOf(y));
                        setPanelHeight(dy);


                        break;
                    }

                    case MotionEvent.ACTION_UP: {
                        mActivePointerId = INVALID_POINTER_ID;
                        android.os.Handler h = new android.os.Handler();
                        h.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                checkPosition();
                            }
                        },1);
                        break;
                    }
                }
                return false;
            }

        });
    }


    public void setScreenHeight(float screenHeight) {
        this.screenHeight = screenHeight;
        calculateOffsets();
    }


    private void calculateOffsets(){
        bottomOffset = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, res.getDisplayMetrics());
        screenHeight = res.getDisplayMetrics().heightPixels;
        maxHeight = screenHeight - bottomOffset;

    }

    public void setPanelHeight(float y){
        //params.height = (int) Math.max(params.height + y, minHeight);
        bottomPanelHeight = ((MainActivity) context).getBottomPanelHeight();
        //Log.d("bottomPanelHeight", String.valueOf(bottomPanelHeight));
        //if (bottomPanelHeight<=0)
           // y = Math.min(0, y);
        y = Math.min(y, bottomPanelHeight);
        //Log.d("y", String.valueOf(y));
        params.height = params.height+(int) y;
        //params.height = (int) (params.height + y);
        setLayoutParams(params);
    }

    public void animatePanelDelta(int deltaHeight){
        //Log.d("delta", String.valueOf(deltaHeight));
        Animation ani = new HeightAnim(deltaHeight);
        ani.setDuration(200);
        startAnimation(ani);
    }


    private void checkPosition(){
        if (animation!=null){
            animation.cancel();
        }
        params = (LinearLayout.LayoutParams) getLayoutParams();
        bottomPanelHeight = ((MainActivity) context).getBottomPanelHeight();
        if (bottomPanelHeight<bottomPanelMinHeight){
            int deltaHeight = (int)(((MainActivity) context).getBottomPanelHeight()-closeBottomHeight);
            animation = new HeightAnim(deltaHeight);
            animation.setDuration(200);
            startAnimation(animation);
        }
    }

    private class HeightAnim extends Animation {
        int deltaHeight;
        int startHeight;

        public HeightAnim(int deltaHeight) {
            params = (LinearLayout.LayoutParams) getLayoutParams();
            this.startHeight = params.height;
            this.deltaHeight = deltaHeight;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            getLayoutParams().height = startHeight+(int) (deltaHeight * interpolatedTime);
            requestLayout();
        }

        @Override
        public void initialize(int width, int height, int parentWidth,
                               int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }
}
