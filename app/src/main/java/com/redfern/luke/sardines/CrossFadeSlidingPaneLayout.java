package com.redfern.luke.sardines;

/**
 * Created by luke on 16/03/16.
 */
        import android.content.Context;
        import android.os.Build;
        import android.support.v4.widget.SlidingPaneLayout;
        import android.util.AttributeSet;
        import android.view.MotionEvent;
        import android.view.View;
        import android.view.ViewGroup;

        import com.nineoldandroids.view.ViewHelper;

public class CrossFadeSlidingPaneLayout extends SlidingPaneLayout {
    private View partialViewLeft = null;
    private View fullViewLeft = null;

    private View partialViewRight = null;
    private View fullViewRight= null;

    public CrossFadeSlidingPaneLayout(Context context) {
        super(context);
    }

    public CrossFadeSlidingPaneLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CrossFadeSlidingPaneLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (getChildCount() < 1) {
            return;
        }

        View panelLeft = getChildAt(0);
        if (!(panelLeft instanceof ViewGroup)) {
            return;
        }

        ViewGroup viewGroupLeft = (ViewGroup) panelLeft;
        if (viewGroupLeft.getChildCount() != 2) {
            return;
        }
        fullViewLeft = viewGroupLeft.getChildAt(0);
        partialViewLeft = viewGroupLeft.getChildAt(1);

        View panelRight = getChildAt(1);
        if (!(panelRight instanceof ViewGroup)) {
            return;
        }

        ViewGroup viewGroupRight = (ViewGroup) panelRight;
        if (viewGroupRight.getChildCount() != 2) {
            return;
        }
        fullViewRight = viewGroupRight.getChildAt(0);
        partialViewRight = viewGroupRight.getChildAt(1);

        fullViewLeft.setAlpha(isOpen() ? 0 : 1);
        partialViewRight.setAlpha(isOpen() ? 0 : 1);

        super.setPanelSlideListener(crossFadeListener);
    }

    @Override
    public void setPanelSlideListener(final PanelSlideListener listener) {
        if (listener == null) {
            super.setPanelSlideListener(crossFadeListener);
            return;
        }

        super.setPanelSlideListener(new PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                crossFadeListener.onPanelSlide(panel, slideOffset);
                listener.onPanelSlide(panel, slideOffset);
            }

            @Override
            public void onPanelOpened(View panel) {
                listener.onPanelOpened(panel);
            }

            @Override
            public void onPanelClosed(View panel) {
                listener.onPanelClosed(panel);
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        /*if (partialViewLeft != null) {
            partialViewLeft.setVisibility(isOpen() ? View.GONE : VISIBLE);
        }
        if (partialViewRight != null) {
            partialViewRight.setVisibility(isOpen() ? View.GONE : VISIBLE);
        }*/
    }

    private SimplePanelSlideListener crossFadeListener = new SimplePanelSlideListener() {
        @Override
        public void onPanelSlide(View panel, float slideOffset) {
            super.onPanelSlide(panel, slideOffset);
            if (partialViewLeft == null || fullViewLeft == null) {
                return;
            }

            ViewHelper.setAlpha(partialViewLeft, 1 - slideOffset);
            ViewHelper.setAlpha(fullViewLeft, slideOffset);
            ViewHelper.setAlpha(fullViewRight, 1 - slideOffset);
            ViewHelper.setAlpha(partialViewRight, slideOffset);
        }
    };

    boolean swipeable = true;
    public void setSwipeable(boolean swipeable) {
        this.swipeable = swipeable;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        return (this.swipeable) ? super.onInterceptTouchEvent(arg0) : false;
    }
}