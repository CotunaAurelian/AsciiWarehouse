package com.xteam.warehouse.ascii.discount.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * TextView that has a square size
 * Created by cotuna on 7/4/16.
 */

public class SquareTextView  extends TextView{
    public SquareTextView(Context context) {
        super(context);
    }

    public SquareTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //We want the textView to have the same height as the weight.
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
