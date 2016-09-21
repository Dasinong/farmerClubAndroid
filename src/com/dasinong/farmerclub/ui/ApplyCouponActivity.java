package com.dasinong.farmerclub.ui;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.entity.AllCouponEntity.Store;
import com.dasinong.farmerclub.entity.BaseEntity;
import com.dasinong.farmerclub.entity.ClaimCouponEntity;
import com.dasinong.farmerclub.net.NetRequest.RequestListener;
import com.dasinong.farmerclub.net.RequestService;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper.Field;
import com.dasinong.farmerclub.ui.view.TopbarView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class ApplyCouponActivity extends BaseActivity {
    private EditText et_name;
    private EditText et_crop;
    private EditText et_size;
    private RadioGroup rg_experience;
    private Button btn_submit;
    private String name;
    private String crop;
    private String size;
    private int checkedId;
    private String experience;
    private String campaignId;
    private String[] experiences = {"第一年的新手", "2-3年有些经验", "3-5年的老手", "5-10年的专家", "10年以上的资深专家"};
    private TopbarView topBar;
    private String lat;
    private String lon;
    private boolean isInsurance;
    private LinearLayout ll_amount;
    private EditText et_jd;
    private String comment;
    private Spinner spinner_crop;
    private String[] crops = {"香蕉", "芒果", "其他"};
    private TextView tv_warning;
    private EditText et_kr;
    private String jdCount;
    private String krCount;
    private RelativeLayout rl_select_crop;
    private LinearLayout ll_experience;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_coupon);

        int id = getIntent().getIntExtra("campaignId", -1);
        isInsurance = getIntent().getBooleanExtra("isInsurance", false);
        lat = SharedPreferencesHelper.getString(this, Field.CURRENT_LAT, "");
        lon = SharedPreferencesHelper.getString(this, Field.CURRENT_LON, "");
        if (id != -1) {
            campaignId = String.valueOf(id);
        }

        initView();
        initEvent();
        initTopBar();
    }

    private void initTopBar() {
        if ("15".equals(campaignId)) {
            topBar.setCenterText("申请表");
        } else {
            topBar.setCenterText("申领表");
        }
        topBar.setLeftView(true, true);
    }

    private void initView() {
        topBar = (TopbarView) findViewById(R.id.topbar);
        tv_warning = (TextView) findViewById(R.id.tv_warning);
        et_name = (EditText) findViewById(R.id.et_name);
        et_crop = (EditText) findViewById(R.id.et_crop);
        rl_select_crop = (RelativeLayout) findViewById(R.id.rl_select_crop);
        spinner_crop = (Spinner) findViewById(R.id.spinner_crop);
        et_size = (EditText) findViewById(R.id.et_size);
        ll_amount = (LinearLayout) findViewById(R.id.ll_amount);
        et_jd = (EditText) findViewById(R.id.et_jd);
        et_kr = (EditText) findViewById(R.id.et_kr);
        ll_experience = (LinearLayout) findViewById(R.id.ll_experience);
        rg_experience = (RadioGroup) findViewById(R.id.rg_experience);
        btn_submit = (Button) findViewById(R.id.btn_submit);

        if ("15".equals(campaignId)) {
            ll_amount.setVisibility(View.VISIBLE);
        } else {
            ll_amount.setVisibility(View.GONE);
        }

        if ("15".equals(campaignId)) {
            et_crop.setVisibility(View.GONE);
            rl_select_crop.setVisibility(View.VISIBLE);
            tv_warning.setVisibility(View.VISIBLE);
            ll_experience.setVisibility(View.GONE);
        } else {
            et_crop.setVisibility(View.VISIBLE);
            rl_select_crop.setVisibility(View.GONE);
            tv_warning.setVisibility(View.GONE);
            ll_experience.setVisibility(View.VISIBLE);
        }

        if (spinner_crop.getVisibility() == View.VISIBLE) {
            spinner_crop.setAdapter(new ArrayAdapter<String>(this, R.layout.textview, crops));
        }
    }

    private void initEvent() {
        btn_submit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startLoadingDialog();
                if (checkNull()) {
//					String name, String company, String crop, String area, String yield,String experience, String productUseHistory, String contactNumber
                    if (TextUtils.isEmpty(jdCount)) {
                        jdCount = "0";
                    }
                    if (TextUtils.isEmpty(krCount)) {
                        krCount = "0";
                    }
                    comment = "凯润:" + krCount + ";" + "健达:" + jdCount;
                    RequestService.getInstance().requestCoupon(ApplyCouponActivity.this, name, "", crop, size, "0", comment, experience, "", "", "", "", BaseEntity.class, new RequestListener() {
                        @Override
                        public void onSuccess(int requestCode, BaseEntity resultData) {
                            if (resultData.isOk()) {
                                dismissLoadingDialog();
                                claimCoupon();
                            }
                            dismissLoadingDialog();
                        }

                        @Override
                        public void onFailed(int requestCode, Exception error, String msg) {
                            dismissLoadingDialog();
                        }

                    });
                } else {
                    dismissLoadingDialog();
                }
            }
        });
    }

    private void claimCoupon() {
        startLoadingDialog();
        RequestService.getInstance().claimCoupon(this, campaignId, comment, lat, lon, ClaimCouponEntity.class, new RequestListener() {

            @Override
            public void onSuccess(int requestCode, BaseEntity resultData) {
                if (resultData.isOk()) {
                    showToast("领取成功");
                    ClaimCouponEntity entity = (ClaimCouponEntity) resultData;
                    if (entity.data != null && entity.data.coupon != null && entity.data.coupon.campaign != null) {
                        Intent intent = new Intent(ApplyCouponActivity.this, CouponQRCodeActivity.class);
                        intent.putExtra("picUrl", entity.data.coupon.campaign.pictureUrls.get(0));
                        intent.putExtra("name", entity.data.coupon.campaign.name);

                        if (entity.data.coupon.campaign.id != 15) {
                            intent.putExtra("time", entity.data.coupon.claimedAt);
                        }
                        intent.putExtra("id", entity.data.coupon.id);
                        intent.putExtra("amount", entity.data.coupon.amount);
                        intent.putExtra("type", entity.data.coupon.type);
                        intent.putExtra("stores", (Serializable) entity.data.coupon.campaign.stores);
                        intent.putExtra("comment", entity.data.coupon.comment);
                        startActivity(intent);
                        finish();
                    }

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
            }
        });
    }

    protected boolean checkNull() {
        name = et_name.getText().toString().trim();
        if (et_crop.getVisibility() == View.VISIBLE) {
            crop = et_crop.getText().toString().trim();
        } else {
            crop = crops[spinner_crop.getSelectedItemPosition()];
        }
        size = et_size.getText().toString().trim();
        jdCount = et_jd.getText().toString().trim();
        krCount = et_kr.getText().toString().trim();
        checkedId = rg_experience.getCheckedRadioButtonId();


        if (TextUtils.isEmpty(name)) {
            showToast("请填写姓名");
            return false;
        }
        if (TextUtils.isEmpty(crop)) {
            showToast("请填写作物");
            return false;
        }
        if (TextUtils.isEmpty(size)) {
            showToast("请填写面积");
            return false;
        }
        if ("15".equals(campaignId)) {
            if (TextUtils.isEmpty(krCount) && TextUtils.isEmpty(jdCount)) {
                showToast("请填写数量");
                return false;
            } else {
                if (TextUtils.isEmpty(krCount)) {
                    if (Float.parseFloat(jdCount) < 1) {
                        showToast("购买数量太少无优惠卷");
                        return false;
                    }
                } else if (TextUtils.isEmpty(jdCount)) {
                    if (Float.parseFloat(krCount) < 3) {
                        showToast("购买数量太少无优惠卷");
                        return false;
                    }
                } else if (Float.parseFloat(krCount) < 3 && Float.parseFloat(jdCount) < 1) {
                    showToast("购买数量太少无优惠卷");
                    return false;
                }
            }
        }
        if (!"15".equals(campaignId)) {
            if (checkedId == -1) {
                showToast("请选择种植经验");
                return false;
            }
            experience = experiences[(checkedId - 1) % 5];
        } else if ("15".equals(campaignId)) {
            experience = "未知";
        }
        return true;
    }
}
