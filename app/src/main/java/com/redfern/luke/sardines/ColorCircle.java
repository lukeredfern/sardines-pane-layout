package com.redfern.luke.sardines;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by luke on 19/03/16.
 */
public class ColorCircle extends View{

    int n = 2;
    boolean selected = false;
    Context context;

    public ColorCircle(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public ColorCircle(Context context) {
        super(context);
        this.context = context;
    }

    public ColorCircle(Context context, int n, boolean selected) {
        super(context);
        this.n = n;
        this.context = context;
        this.selected = selected;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float w = getWidth();
        float h = getHeight();
        float r = 0.85f*Math.min(w,h)/2;
        float sW = r/10;
        float cx = w/2;
        float cy = h/2;

        Paint innerPaint = new Paint();
        innerPaint.setColor(MaterialColor.getColor(n, 7));
        innerPaint.setStyle(Paint.Style.FILL);
        innerPaint.setAntiAlias(true);

        Paint outerPaint = new Paint();
        outerPaint.setColor(Color.WHITE);
        outerPaint.setStyle(Paint.Style.STROKE);
        outerPaint.setStrokeWidth(sW);
        outerPaint.setAntiAlias(true);

        canvas.drawCircle(cx, cy, r, innerPaint);
        canvas.drawCircle(cx, cy, r, outerPaint);

        if (selected) {
            Drawable d = ContextCompat.getDrawable(context, R.drawable.tick);
            d.setTint(Color.WHITE);
            int rs = (int) (0.35f * Math.min(w, h));
            d.setBounds((int) (cx - rs), (int) (cy - rs), (int) (cx + rs), (int) (cy + rs));
            d.draw(canvas);
        }

    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
        invalidate();
    }
}
