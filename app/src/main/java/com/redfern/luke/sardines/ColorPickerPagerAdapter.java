package com.redfern.luke.sardines;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by luke on 19/03/16.
 */
public class ColorPickerPagerAdapter extends PagerAdapter {

    private Context mContext;
    private int nColors;
    private int nPages;
    private int nColorPage;
    private int nColor;
    private int widthPager;
    private int heightPager;
    LinearLayout.LayoutParams params;
    int padding;
    ArrayList<ColorCircle> circles;
    private int CLICK_ACTION_THRESHHOLD = 5;

    private float startX;
    private float startY;
    private float vHeight;
    private int clickn;

    public ColorPickerPagerAdapter(Context context,int widthPager, int heightPager,int nColors, int nColor) {
        mContext = context;
        this.widthPager = widthPager;
        this.nColors = nColors;
        this.heightPager = heightPager;
        this.nColor = nColor;

        Resources r = mContext.getResources();
        padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, r.getDisplayMetrics());
        params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1);

        CLICK_ACTION_THRESHHOLD = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, r.getDisplayMetrics());

        nColorPage = (int) Math.floor((widthPager-2*padding)/heightPager);
        nPages = (int) Math.ceil(((float) nColors) / ((float) nColorPage));
        circles = new ArrayList<>(nColors);

    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LinearLayout layout = new LinearLayout(mContext);
        //layout.setBackgroundColor(MaterialColor.getColor((int)(Math.random()*16),4));
        layout.setPadding(padding,0,padding,0);
        for (int i = 0; i<nColorPage;i++) {
            final int n = position*nColorPage+i;
            if (n<nColors) {
                circles.add(new ColorCircle(mContext, n, n == nColor));
                circles.get(n).setLayoutParams(params);
                circles.get(n).setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                startX = v.getLeft()+event.getX();
                                startY = v.getTop()+event.getY();
                                vHeight = v.getBottom();
                                clickn = n;
                                Log.d("start", String.valueOf(startX));
                                break;
                        }
                        v.getParent().requestDisallowInterceptTouchEvent(true); //specific to my project
                        return false; //specific to my project
                    }
                });
                layout.addView(circles.get(n));
            } else {
                View dummyView = new View(mContext);
                dummyView.setLayoutParams(params);
                layout.addView(dummyView);
            }
        }
        collection.addView(layout);
        return layout;
    }

    public void setEndPoint(float endX, float endY){
        Log.d("end", String.valueOf(endX));
        if (isAClick(startX, endX, startY, endY)) {
            ((MainActivity) mContext).changeActiveItemColor(clickn,(int)endX,(int)(endY));
            circles.get(nColor).setSelected(false);
            circles.get(clickn).setSelected(true);
            nColor = clickn;
        }
    }

    private boolean isAClick(float startX, float endX, float startY, float endY) {
        float differenceX = Math.abs(startX - endX);
        float differenceY = Math.abs(startY - endY);
        if (differenceX > CLICK_ACTION_THRESHHOLD/* =5 */ ) {
            return false;
        }
        return true;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return nPages;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    public void setWidthPager(int width) {
        this.widthPager = width;
    }
}
