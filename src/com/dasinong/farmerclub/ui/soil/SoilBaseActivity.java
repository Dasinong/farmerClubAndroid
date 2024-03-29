package com.dasinong.farmerclub.ui.soil;


import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.components.net.INetRequest;
import com.dasinong.farmerclub.components.net.NetError;
import com.dasinong.farmerclub.ui.BaseActivity;
import com.dasinong.farmerclub.ui.view.TopbarView;

public abstract class SoilBaseActivity extends BaseActivity implements INetRequest {


    protected TopbarView mTopBarView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soil_base);
        initTopBar();
        LinearLayout container = (LinearLayout) findViewById(R.id.soil_container);
        View root = View.inflate(this, getMainResourceId(), null);
        container.addView(root, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        initView();
        initEvent();
    }

    protected void initTopBar() {
        mTopBarView = (TopbarView) findViewById(R.id.top_bar);
        mTopBarView.setCenterText(getTitleText());
        mTopBarView.setLeftView(isBack(), isBack());



    }

    protected abstract int getMainResourceId();


    protected abstract void initView();

    protected abstract void initEvent();

    @Override
    public void onTaskFailedSuccess(int requestCode, NetError error) {

    }


//    @Override
//    public void onCache(int requestCode, Object response) {
//
//        onTaskSuccess(requestCode,response);
//    }

    public abstract int getTitleText();

    public boolean isBack() {

        return true;
    }

    public void setRightText(int resourceID) {

        mTopBarView.setRightText(resourceID);
        mTopBarView.setRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRightClick();
            }
        });
    }


    public  void onRightClick()
    {

    }







}
