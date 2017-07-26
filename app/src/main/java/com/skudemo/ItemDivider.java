package com.skudemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @创建者 zhangfan1
 * @创建时间 2016/6/17 9:02
 * @描述 RecycleView间距类, 思路上是先在条目的上层画一个颜色，然后让条目本身偏移，达到绘制自定义颜色的间距
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述
 */
public class ItemDivider extends RecyclerView.ItemDecoration {

    private Paint paint;
    private Context context;
    private int dp;
    private boolean hasLast = true;

    public ItemDivider(Context context, int dp) {
        this.context = context;
        this.dp = dp;
        if (paint == null) {
            paint = new Paint();
            if (context != null)
                paint.setColor(context.getResources().getColor(android.R.color.transparent));
        }
    }

    public ItemDivider(Context context, int dp, boolean hasLast) {
        this.context = context;
        this.dp = dp;
        this.hasLast = hasLast;
        if (paint == null) {
            paint = new Paint();
            if (context != null)
                paint.setColor(context.getResources().getColor(android.R.color.transparent));
        }
    }

    public ItemDivider(Context context, int dp, int color) {
        this.context = context;
        this.dp = dp;
        if (paint == null) {
            paint = new Paint();
            if (context != null)
                paint.setColor(color);
        }
    }

    public ItemDivider(Context context, int dp, int color, boolean hasLast) {
        this.context = context;
        this.dp = dp;
        this.hasLast = hasLast;
        if (paint == null) {
            paint = new Paint();
            if (context != null)
                paint.setColor(color);
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        final int childCount = hasLast ? parent.getChildCount() : parent.getChildCount() - 1;
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + dp;
            c.drawRect(left, top, right, bottom, paint);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(0, 0, 0,  dp);
    }
}
