package com.skudemo;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @创建者 zhangfan1
 * @创建时间 2017/7/3 9:54
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述
 */

class FlexDataAdapter extends RecyclerView.Adapter {
    private final Context context;
    private Long[][] data;
    private ArrayList<View> cacheView = new ArrayList<>();
    private ArrayList<Long> resultData;
    private Long defaultCheck;
    private boolean isDefaultCheck = false;
    HashMap<FlexboxLayout, Long> checkedProduct = new HashMap<>();
    private  HashMap<FlexboxLayout,TextView> defaultCheckTv= new HashMap<>();

    public FlexDataAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item, null);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemHolder itemHolder = (ItemHolder) holder;
        itemHolder.setData(cacheView.get(position));
    }

    @Override
    public int getItemCount() {
        return data != null && data.length > 0 ? data.length : 0;
    }

    public void setData(Long[][] data) {
        this.data = data;
        cacheView.clear();
        cacheView();
    }



    private void cacheView() {
        for (int i = 0; i < data.length; i++) {
            FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.MATCH_PARENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);
            final FlexboxLayout layout = new FlexboxLayout(context);
            layout.setLayoutParams(params);
            layout.setFlexDirection(FlexDirection.ROW);
            layout.setFlexWrap(FlexWrap.WRAP);
            layout.setDividerDrawable(ContextCompat.getDrawable(context, R.drawable.item_divider));
            layout.setShowDivider(FlexboxLayout.SHOW_DIVIDER_MIDDLE);
            for (int j = 0; j < data[i].length; j++) {
                final TextView view = (TextView) LayoutInflater.from(context).inflate(R.layout.tv_item, null);
                view.setTag(data[i][j]);
                view.setText(String.valueOf(data[i][j]));
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!v.isSelected()) {
                            TextView checkedTv = defaultCheckTv.get(layout);
                            if (checkedTv != null)//如果有已选中的，先取消选择
                                checkedTv.setSelected(false);
                            defaultCheckTv.put(layout, (TextView) v);
                            v.setSelected(true);
                            checkedProduct.put(layout, (Long) v.getTag());
                            refreshSKU(layout, true, (Long) v.getTag());
                        } else {//取消选择TAG
                            checkedProduct.remove(layout);
                            v.setSelected(false);
                            refreshSKU(layout, false, (Long) v.getTag());
                        }
                    }
                });
                layout.addView(view);

            }
            if (layout.getFlexItemCount() > 0) {
                cacheView.add(layout);
            }
        }
        for (int i = 0; i < cacheView.size(); i++) {
            FlexboxLayout flowLayout = (FlexboxLayout) cacheView.get(i);
            for (int v = 0; v < flowLayout.getFlexItemCount(); v++) {

                TextView tafText = (TextView) flowLayout.getFlexItemAt(v);
                tafText.setEnabled(false);
                tafText.setClickable(false);
                tafText.setSelected(false);

                Long tag = (Long) tafText.getTag();
                for (Long result : resultData) {
                    if (result % tag == 0) {
                        tafText.setEnabled(true);
                        tafText.setClickable(true);
                    }
                    if (isDefaultCheck) {
                        if (defaultCheck != null && result.equals(defaultCheck)) {
                            defaultCheckTv.put(flowLayout, tafText);
                            tafText.setSelected(true);
                        }
                    }
                }
            }
        }
    }

    public void setResultData(ArrayList<Long> resultData) {
        this.resultData = resultData;
    }

    public void setDefaultCheck(Long defaultCheck) {
        isDefaultCheck = true;
        this.defaultCheck = defaultCheck;
        setDefaultSKU(defaultCheck);
    }

    public void refreshSKU(FlexboxLayout parent, boolean checked, Long tag) {
        ArrayList<Long> list = new ArrayList<>();
        for (Map.Entry<FlexboxLayout, Long> entry : checkedProduct.entrySet()) {
            list.add(entry.getValue());
        }
        checkSKU(list, parent, checked, tag);
    }


    public void setDefaultSKU(Long sku) {
        for (int i = 0; i < cacheView.size(); i++) {
            FlexboxLayout flowLayout = (FlexboxLayout) cacheView.get(i);
            for (int v = 0; v < flowLayout.getFlexItemCount(); v++) {
                TextView tafText =
                        (TextView) flowLayout.getFlexItemAt(v);
                tafText.setSelected(false);
                tafText.setEnabled(false);
                tafText.setClickable(false);
                Long tag = (Long) tafText.getTag();
                Log.d("Value", tag.toString());
                if (sku % tag == 0) {
                    tafText.setEnabled(true);
                    tafText.setClickable(true);
                    tafText.setSelected(true);
                    defaultCheckTv.put(flowLayout, tafText);
                    checkedProduct.put(flowLayout, tag);
                }
            }
        }
    }


    /**
     * @param list
     * @param parent
     * @param isCheck
     * @param checktag
     */
    public void checkSKU(ArrayList<Long> list, FlexboxLayout parent, boolean isCheck, Long checktag) {
        Long checkedResult = 1L;
        for (Long checked : list) {
            checkedResult *= checked;
        }
        for (int i = 0; i < cacheView.size(); i++) {
            FlexboxLayout flowLayout = (FlexboxLayout) cacheView.get(i);
            if (!isCheck) {//取消选择时候不做过滤
                Long id = checkedProduct.get(flowLayout);
                if (id != null && id != 0) {//有选中行
                    long r = checkedResult / id;//除去单前选中行后的积
                    for (int v = 0; v < flowLayout.getFlexItemCount(); v++) {
                        TextView tafText = (TextView) flowLayout.getFlexItemAt(v);
                        Long tag = (Long) tafText.getTag();
                        if (!tafText.isSelected()) {
                            for (Long cancheck : resultData) {
                                if (cancheck % (tag * r) == 0) {
                                    tafText.setEnabled(true);
                                    tafText.setClickable(true);
                                    tafText.setSelected(false);
                                    break;
                                }
                            }
                        }

                    }
                } else {//当前行没有任何按钮被选中
                    for (int v = 0; v < flowLayout.getFlexItemCount(); v++) {
                        TextView tafText = (TextView) flowLayout.getFlexItemAt(v);
                        Long tag = (Long) tafText.getTag();
                        for (Long cancheck : resultData) {
                            if (cancheck % (tag * checkedResult) == 0) {
                                tafText.setEnabled(true);
                                tafText.setClickable(true);
                                tafText.setSelected(false);
                                break;
                            }
                        }
                    }
                }

            } else {//选中按钮
                for (int v = 0; v < flowLayout.getFlexItemCount(); v++) {
                    TextView tafText = (TextView) flowLayout.getFlexItemAt(v);
                    if (parent != flowLayout) {//选中时，当前行的其他Item不做更新
                        Long id = checkedProduct.get(flowLayout);
                        if(id==null||id==0)
                            id=1L;
                        Long tag = (Long) tafText.getTag();
                        long selectResult = tag *(checkedResult/id);//选中的按钮和可以选择的按钮的积，是否在集合里面
                        for (Long cancheck : resultData) {
                            if (cancheck % selectResult == 0) {
                                tafText.setEnabled(true);
                                tafText.setClickable(true);
                                tafText.setSelected(false);
                                if (id.equals(tag)) {
                                    tafText.setSelected(true);
                                    defaultCheckTv.put(flowLayout, tafText);
                                }
                                break;
                            } else {
                                tafText.setSelected(false);
                                tafText.setEnabled(false);
                                tafText.setClickable(false);
                                }
                        }
                    }
                }
            }
        }
    }
}
