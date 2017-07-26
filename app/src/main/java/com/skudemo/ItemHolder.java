package com.skudemo;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

/**
 * @创建者 zhangfan1
 * @创建时间 2017/7/3 9:59
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述
 */

class ItemHolder extends RecyclerView.ViewHolder {

    private  LinearLayout contentlayout;
    private View view;

    public ItemHolder(View itemView) {
        super(itemView);
        contentlayout = (LinearLayout) itemView.findViewById(R.id.item_content);
    }

    public void setData(View view) {
        this.view = view;
        contentlayout.removeAllViews();
        contentlayout.addView(view);
    }
}
