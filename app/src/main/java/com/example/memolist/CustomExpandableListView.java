package com.example.memolist;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ExpandableListView;

/**
 * Created by Kinred on 8/11/18.
 */
public class CustomExpandableListView extends ExpandableListView {

    private int maxHeightDp;

    public CustomExpandableListView(Context context) {
        super(context);
    }

    public CustomExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        final TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomExpandableListView, 0, 0);
        try {
            maxHeightDp = a.getInteger(R.styleable.CustomExpandableListView_maxHeightDp, 0);
        } finally {
            a.recycle();
        }
    }

    public CustomExpandableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int maxHeightPx = convertDpToPx(maxHeightDp);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeightPx, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setMaxHeightDp(int maxHeightDp) {
        this.maxHeightDp = maxHeightDp;
        invalidate();
    }

    private int convertDpToPx(int dp){
        return Math.round(dp*(getResources().getDisplayMetrics().xdpi/ DisplayMetrics.DENSITY_DEFAULT));

    }

}