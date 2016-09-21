package com.dasinong.farmerclub.ui;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;
import android.widget.TableLayout.LayoutParams;
import android.widget.TextView;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.entity.AllCouponEntity.CouponCampaign;
import com.dasinong.farmerclub.entity.AllCouponEntity.Store;
import com.dasinong.farmerclub.entity.BaseEntity;
import com.dasinong.farmerclub.entity.ClaimCouponEntity;
import com.dasinong.farmerclub.entity.CouponDetailEntity;
import com.dasinong.farmerclub.net.NetRequest.RequestListener;
import com.dasinong.farmerclub.net.NetConfig;
import com.dasinong.farmerclub.net.RequestService;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper.Field;
import com.dasinong.farmerclub.ui.view.TopbarView;
import com.dasinong.farmerclub.utils.GraphicUtils;
import com.lidroid.xutils.BitmapUtils;

public class CouponDetailActivity extends BaseActivity {
    private LinearLayout ll_pics;
    private LinearLayout ll_exchange_place;
    private TopbarView topBar;
    private Map<String, List<Store>> map;
    private TextView tv_title;
    private TextView tv_amount;
    private TextView tv_claim;
    private TextView tv_description;
    private TextView tv_redeem;
    private Button btn_apply;
    private int campaignId;
    private ImageView iv_top_image;

    private String province;
    private String city;
    private boolean isApplay;
    private boolean isDaren;
    private String lon;
    private String lat;
    private String type;
    private int id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_detail);

        isApplay = getIntent().getBooleanExtra("isApply", true);
        id = getIntent().getIntExtra("campaignId", -1);
        type = getIntent().getStringExtra("type");
        lon = SharedPreferencesHelper.getString(this, Field.CURRENT_LON, "");
        lat = SharedPreferencesHelper.getString(this, Field.CURRENT_LAT, "");
        isDaren = SharedPreferencesHelper.getBoolean(this, Field.ISDAREN, false);

        initView();
        queryData(String.valueOf(id));
        initEvent();
    }

    private void initView() {
        iv_top_image = (ImageView) findViewById(R.id.iv_top_image);

        ll_pics = (LinearLayout) findViewById(R.id.ll_pics);
        ll_exchange_place = (LinearLayout) findViewById(R.id.ll_exchange_place);
        topBar = (TopbarView) findViewById(R.id.topbar);

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_amount = (TextView) findViewById(R.id.tv_amount);
        tv_claim = (TextView) findViewById(R.id.tv_claim);
        tv_description = (TextView) findViewById(R.id.tv_description);
        tv_redeem = (TextView) findViewById(R.id.tv_redeem);
        btn_apply = (Button) findViewById(R.id.btn_apply);

        if ("INSURANCE".equals(type)) {
            findViewById(R.id.ll_redeem).setVisibility(View.GONE);
            findViewById(R.id.ll_store).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.tv_claim_text)).setText("活动时间");
            btn_apply.setText("立即申请");
        }

        if (id == 38 || id == 40) {
            findViewById(R.id.ll_store).setVisibility(View.GONE);
            findViewById(R.id.line).setVisibility(View.GONE);
        }

    }

    private void initEvent() {
        btn_apply.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if ("INSURANCE".equals(type)) {
                    Intent intent = new Intent(CouponDetailActivity.this, ApplyCouponActivity.class);
                    intent.putExtra("campaignId", campaignId);
                    intent.putExtra("isInsurance", true);
                    startActivity(intent);
                    return;
                }

                if (isDaren || campaignId == 14) {
                    claimCoupon();
                } else if (campaignId == 38 || campaignId == 40) {
                    Intent intent = new Intent(CouponDetailActivity.this,ApplyTSCouponActivity.class);
                    intent.putExtra("campaignId", campaignId);
                    intent.putExtra("isInsurance", false);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(CouponDetailActivity.this, ApplyCouponActivity.class);
                    intent.putExtra("campaignId", campaignId);
                    intent.putExtra("isInsurance", false);
                    startActivity(intent);
                }
            }
        });

        if (!isApplay) {
            btn_apply.setClickable(false);
            btn_apply.setBackgroundResource(R.color.color_999999);
            btn_apply.setText("您已参加过此活动");
        }
    }

    private void claimCoupon() {
        startLoadingDialog();
        RequestService.getInstance().claimCoupon(this, String.valueOf(campaignId), "", lat, lon, ClaimCouponEntity.class, new RequestListener() {

            @Override
            public void onSuccess(int requestCode, BaseEntity resultData) {
                if (resultData.isOk()) {
                    showToast("领取成功");
                    ClaimCouponEntity entity = (ClaimCouponEntity) resultData;
                    if (entity.data != null && entity.data.coupon != null && entity.data.coupon.campaign != null) {
                        Intent intent = new Intent(CouponDetailActivity.this, CouponQRCodeActivity.class);
                        intent.putExtra("picUrl", entity.data.coupon.campaign.pictureUrls.get(0));
                        intent.putExtra("name", entity.data.coupon.campaign.name);
                        if (entity.data.coupon.campaign.id != 15) {
                            intent.putExtra("time", entity.data.coupon.claimedAt);
                        }
                        intent.putExtra("id", entity.data.coupon.id);
                        intent.putExtra("campaignId",entity.data.coupon.campaignId);
                        intent.putExtra("stores", (Serializable) entity.data.coupon.campaign.stores);
                        startActivity(intent);
                        finish();
                    }

                } else if ("2001".equals(resultData.getRespCode())) {
                    showToast("您已领取过了，请勿重复领取");
                } else if ("2002".equals(resultData.getRespCode())) {
                    showToast("对不起，活动已过期");
                } else if ("2003".equals(resultData.getRespCode())) {
                    showToast("对不起，该优惠券已经被抢光了");
                }
                dismissLoadingDialog();
            }

            @Override
            public void onFailed(int requestCode, Exception error, String msg) {
                dismissLoadingDialog();
            }
        });
    }

    private void queryData(String id) {
        startLoadingDialog();

        RequestService.getInstance().couponCampaigns(this, id, province, city, lat, lon, CouponDetailEntity.class, new RequestListener() {

            @Override
            public void onSuccess(int requestCode, BaseEntity resultData) {
                if (resultData.isOk()) {
                    CouponDetailEntity entity = (CouponDetailEntity) resultData;
                    if (entity.data != null && entity.data.campaign != null) {
                        campaignId = entity.data.campaign.id;
                        formatData(entity.data.campaign);

                        initTopBar(entity.data.campaign.name);
                    }
                }
                dismissLoadingDialog();
            }

            @Override
            public void onFailed(int requestCode, Exception error, String msg) {
                dismissLoadingDialog();
            }
        });
    }

    private void formatData(CouponCampaign campaign) {
        map = new HashMap<String, List<Store>>();
        if (campaign.stores != null && !campaign.stores.isEmpty()) {
            for (int i = 0; i < campaign.stores.size(); i++) {
                if (!map.containsKey(campaign.stores.get(i).province)) {
                    List<Store> list = new ArrayList<Store>();
                    list.add(campaign.stores.get(i));
                    map.put(campaign.stores.get(i).province, list);
                } else {
                    map.get(campaign.stores.get(i).province).add(campaign.stores.get(i));
                }
            }
        }
        setData(campaign);
    }

    private void initTopBar(String title) {
        if ("INSURANCE".equals(type)) {
            TextView centerTextView = topBar.getCenterTextView();
            centerTextView.setMaxWidth(GraphicUtils.dip2px(this, 300));
        }
        topBar.setCenterText(title);
        topBar.setLeftView(true, true);
    }

    private void setData(CouponCampaign campaign) {
        BitmapUtils bitmapUtils = new BitmapUtils(this);

        bitmapUtils.display(iv_top_image, NetConfig.COUPON_IMAGE + campaign.pictureUrls.get(0));

        tv_title.setText(campaign.name);

        String claimTime = time2String(campaign.claimTimeStart, campaign.claimTimeEnd);

        tv_claim.setText(claimTime);

        tv_description.setText(campaign.description);

        String redeemTime = time2String(campaign.redeemTimeStart, campaign.redeemTimeEnd);

        tv_redeem.setText(redeemTime);

        if (campaign.amount != 0) {
            tv_amount.setText("¥" + campaign.amount + ".00");
        } else {
            tv_amount.setVisibility(View.GONE);
        }

        for (int i = 1; i < campaign.pictureUrls.size(); i++) {
            ImageView imageView = new ImageView(this);

            LinearLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, GraphicUtils.dip2px(this, 250));
            params.setMargins(0, 0, 0, GraphicUtils.dip2px(this, 5));
            imageView.setScaleType(ScaleType.CENTER_CROP);
            imageView.setLayoutParams(params);

            bitmapUtils.display(imageView, NetConfig.COUPON_IMAGE + campaign.pictureUrls.get(i));

            ll_pics.addView(imageView);
        }

        Set<String> keySet = map.keySet();
        for (String province : keySet) {
            TextView tv = new TextView(this);
            LinearLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, GraphicUtils.dip2px(this, 32.5f));
            tv.setText(province);
            tv.setPadding(GraphicUtils.dip2px(this, 15), 0, 0, 0);
            tv.setTextColor(getResources().getColor(R.color.color_666666));
            tv.setBackgroundResource(R.color.color_F5F5F5);
            tv.setGravity(Gravity.CENTER_VERTICAL);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

            tv.setLayoutParams(params);
            ll_exchange_place.addView(tv);

            for (Store store : map.get(province)) {
                View view = View.inflate(this, R.layout.item_exchange_place, null);
                TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
                TextView tv_address = (TextView) view.findViewById(R.id.tv_address);
                TextView tv_phone = (TextView) view.findViewById(R.id.tv_phone);

                tv_name.setText(store.name);
                tv_address.setText(store.location);
                tv_phone.setText(store.phone);

                LinearLayout.LayoutParams dividerParams = new LayoutParams(LayoutParams.MATCH_PARENT, GraphicUtils.dip2px(this, 0.5f));
                View divider = new View(this);
                divider.setBackgroundResource(R.color.color_DBE3E5);
                divider.setLayoutParams(dividerParams);
                view.setPadding(0, GraphicUtils.dip2px(this, 15), 0, GraphicUtils.dip2px(this, 15));
                ll_exchange_place.addView(view);
                ll_exchange_place.addView(divider);
            }
        }

        if (campaign.stores.isEmpty()) {
            btn_apply.setClickable(false);
            btn_apply.setBackgroundResource(R.color.color_999999);
            btn_apply.setText("本区域暂未开放");
        }
    }

    private String time2String(long start, long end) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
        Date date = new Date();

        date.setTime(start);
        String strStart = sdf.format(date).toString();

        date.setTime(end);
        String strEnd = sdf.format(date).toString();

        return strStart + "-" + strEnd;
    }

}
