package com.dasinong.farmerclub.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.entity.BaseEntity;
import com.dasinong.farmerclub.entity.ClaimCouponEntity;
import com.dasinong.farmerclub.net.NetRequest;
import com.dasinong.farmerclub.net.RequestService;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper;
import com.dasinong.farmerclub.ui.view.TopbarView;

import java.io.Serializable;

import javax.sql.RowSetEvent;

/**
 * Created by Ming on 2016/9/21.
 */
public class ApplyTSCouponActivity extends BaseActivity {

    private EditText et_name;
    private EditText et_postcode;
    private EditText et_address;
    private Button btn_submit;
    private ProgressBar pb_loading;
    private String lon;
    private String lat;
    private TopbarView topBar;
    private boolean isInsurance;
    private String campaignId;
    private EditText et_phone;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_ts_coupon);

        int id = getIntent().getIntExtra("campaignId", -1);
        isInsurance = getIntent().getBooleanExtra("isInsurance", false);
        lat = SharedPreferencesHelper.getString(this, SharedPreferencesHelper.Field.CURRENT_LAT, "");
        lon = SharedPreferencesHelper.getString(this, SharedPreferencesHelper.Field.CURRENT_LON, "");
        if (id != -1) {
            campaignId = String.valueOf(id);
        }

        initView();
        initTopBar();
        setEvent();
    }

    private void initTopBar() {
        topBar.setCenterText("申领表");
        topBar.setLeftView(true,true);
    }

    private void initView() {
        topBar = (TopbarView) findViewById(R.id.topbar);

        et_name = (EditText) findViewById(R.id.et_name);
        et_postcode = (EditText) findViewById(R.id.et_postcode);
        et_address = (EditText) findViewById(R.id.et_address);
        et_phone = (EditText) findViewById(R.id.et_phone);

        btn_submit = (Button) findViewById(R.id.btn_submit);
        pb_loading = (ProgressBar) findViewById(R.id.pb_loading);

        et_name.setText(SharedPreferencesHelper.getString(this, SharedPreferencesHelper.Field.USER_NAME,""));

    }

    private void setEvent() {
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et_name.getText().toString().trim();
                String postcode = et_postcode.getText().toString().trim();
                String address = et_address.getText().toString().trim();
                String phone = et_phone.getText().toString().trim();

                if(TextUtils.isEmpty(name)){
                    showToast("姓名不能为空");
                    return;
                }

                if(TextUtils.isEmpty(postcode)){
                    showToast("邮编不能为空");
                    return;
                }

                if(TextUtils.isEmpty(address)){
                    showToast("地址不能为空");
                    return;
                }

                if(TextUtils.isEmpty(phone)){
                    showToast("联系电话不能为空");
                    return;
                }

                startLoadingDialog();
                btn_submit.setClickable(false);
                RequestService.getInstance().requestCoupon(ApplyTSCouponActivity.this, name,"" , "", "0", "0", "", "", "", phone, postcode,address, BaseEntity.class, new NetRequest.RequestListener() {
                    @Override
                    public void onSuccess(int requestCode, BaseEntity resultData) {
                        if (resultData.isOk()) {
                            dismissLoadingDialog();
                            claimCoupon();
                        }
                    }

                    @Override
                    public void onFailed(int requestCode, Exception error, String msg) {
                        btn_submit.setClickable(true);
                        dismissLoadingDialog();
                    }
                });

            }
        });
    }

    private void claimCoupon() {
        startLoadingDialog();
        btn_submit.setClickable(false);
        RequestService.getInstance().claimCoupon(this, campaignId, "", lat, lon, ClaimCouponEntity.class, new NetRequest.RequestListener() {

            @Override
            public void onSuccess(int requestCode, BaseEntity resultData) {
                if (resultData.isOk()) {
                    showToast("领取成功");
//                    ClaimCouponEntity entity = (ClaimCouponEntity) resultData;
//                    if (entity.data != null && entity.data.coupon != null && entity.data.coupon.campaign != null) {
//                        Intent intent = new Intent(ApplyTSCouponActivity.this, CouponQRCodeActivity.class);
//                        intent.putExtra("picUrl", entity.data.coupon.campaign.pictureUrls.get(0));
//                        intent.putExtra("name", entity.data.coupon.campaign.name);
//
//                        if (entity.data.coupon.campaign.id != 15) {
//                            intent.putExtra("time", entity.data.coupon.claimedAt);
//                        }
//                        intent.putExtra("id", entity.data.coupon.id);
//                        intent.putExtra("amount", entity.data.coupon.amount);
//                        intent.putExtra("type", entity.data.coupon.type);
//                        intent.putExtra("stores", (Serializable) entity.data.coupon.campaign.stores);
//                        intent.putExtra("comment", entity.data.coupon.comment);
//                        startActivity(intent);
//                    }

                    setResult(RESULT_OK);
                    finish();
                } else if ("2001".equals(resultData.getRespCode())) {
                    showToast("您已领取过了，请勿重复领取");
                } else if ("2002".equals(resultData.getRespCode())) {
                    showToast("对不起，活动已过期");
                } else if ("2003".equals(resultData.getRespCode())) {
                    showToast("对不起，该优惠券已经被抢光了");
                } else if ("2106".equals(resultData.getRespCode())) {
                    showToast(resultData.getMessage());
                }
                dismissLoadingDialog();
            }

            @Override
            public void onFailed(int requestCode, Exception error, String msg) {
                dismissLoadingDialog();
                btn_submit.setClickable(false);
            }
        });
    }


}
