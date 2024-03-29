package com.dasinong.farmerclub.ui.soil;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.dasinong.farmerclub.DsnApplication;
import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.components.net.VolleyManager;
import com.dasinong.farmerclub.net.NetConfig;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper.Field;
import com.dasinong.farmerclub.ui.soil.adapter.CommonAdapter;
import com.dasinong.farmerclub.ui.soil.adapter.ViewHolder.ViewHolder;
import com.dasinong.farmerclub.ui.soil.domain.DataEntity;
import com.dasinong.farmerclub.ui.soil.domain.SoilAllEntity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SoilAllListActivity extends SoilBaseActivity implements AdapterView.OnItemClickListener {


    private ListView mListView;
    private static final int REQUEST_CODE_SOIL_LIST = 190;

    private static final String URL = NetConfig.BASE_URL + "getSoilReport";

    private String mTipText;

    private SimpleDateFormat sdf;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mTipText = getResources().getString(R.string.soil_report_title);

        sdf = new SimpleDateFormat("yyyy年MM月dd日");
        loadDataFromServer();

    }

    @Override
    protected int getMainResourceId() {
        return R.layout.activity_soil_all_list;
    }

    @Override
    protected void initView() {
        mListView = (ListView) findViewById(R.id.list_view);


    }

    @Override
    protected void initEvent() {
        mListView.setOnItemClickListener(this);
    }

    private void loadDataFromServer() {
        startLoadingDialog();
        Long fieldId = SharedPreferencesHelper.getLong(DsnApplication.getContext(), Field.FIELDID, 0);
        SoilAllEntity.Param param = new SoilAllEntity.Param(fieldId);
        
        VolleyManager.getInstance().addGetRequestWithNoCache(
                REQUEST_CODE_SOIL_LIST,
                URL,
                param,
                SoilAllEntity.class,
                this

        );


    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if(parent.getItemAtPosition(position)==null){
            return;
        }
        startActivity(SoilInformationActivity.createIntent(this, (DataEntity) parent.getItemAtPosition(position)));
    }

    @Override
    public void onTaskSuccess(int requestCode, Object response) {

        dismissLoadingDialog();
        SoilAllEntity entity = (SoilAllEntity) response;
        mListView.setAdapter(new CommonAdapter<DataEntity>(entity.data) {

            @Override
            protected int getResourceId() {
                return R.layout.soil_list_item;
            }

            @Override
            protected void updateView(DataEntity result, ViewHolder viewHolder,int position) {

                String text = sdf.format(result.testDate)+mTipText;
                viewHolder.setTextValue(R.id.soil_item_text,text);

            }
        });
    }


    @Override
    public int getTitleText() {
        return R.string.soil_report_title;
    }


}
