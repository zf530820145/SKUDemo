package com.skudemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       ArrayList<Long> canChecked=new ArrayList<>();
       Long[][] result=new Long[][]{{2L,3L,5L},{19L,23L},{43L,47L},{83L,89L},{113L,127L,137L}};

        Long[][] add=new Long[][]{{2L,19L,43L,89L,113L},{2L,19L,43L,89L,127L},{2L,23L,43L,89L,127L},{3L,23L,43L,89L,113L},{3L,19L,43L,89L,113L},{3L,19L,43L,83L,127L},{3L,19L,47L,89L,113L},{5L,23L,43L,89L,113L},{5L,23L,43L,89L,137L}};
        for (int i = 0; i < add.length; i++) {
            long sum=1;
            for (int j = 0; j < add[i].length; j++) {
                sum*=add[i][j];
            }
            canChecked.add(sum);
        }
        for (int i = 0; i < canChecked.size(); i++) {
            Log.d("Test",canChecked.get(i).toString());
        }



//        long[][] args=new long[][]{{2,3,5,7,11,13},{17,19,23,29,31,37,41},{43,47,53,59,61,67,71,73,79},{83,89,97,101,103,107,109},{113,127,131,137,139,149,151,157}};
        RecyclerView ry = (RecyclerView)findViewById(R.id.ry_main);
        ry.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        FlexDataAdapter dataAdapter = new FlexDataAdapter(this);
        ry.addItemDecoration(new ItemDivider(this,30));
        dataAdapter.setResultData(canChecked);
        dataAdapter.setData(result);
        dataAdapter.setDefaultCheck(canChecked.get(1));
        ry.setAdapter(dataAdapter);
    }
}
