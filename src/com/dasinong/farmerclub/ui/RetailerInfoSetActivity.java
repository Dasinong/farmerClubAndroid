package com.dasinong.farmerclub.ui;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.ui.view.TopbarView;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Spinner;

public class RetailerInfoSetActivity extends BaseActivity {

	public static final int EDIT_RETAILER_PHONE = 100;
	public static final int EDIT_RETAILER_NAME = EDIT_RETAILER_PHONE + 1;
	public static final int EDIT_RETAILER_ADDRESS = EDIT_RETAILER_NAME + 1;

	private TopbarView mTopbarView;
	private EditText mEditText;
	private View mSelectAreaLayout;
	private Spinner mProvinceSp;
	private Spinner mCitySp;
	private Spinner mAreaSp;
	private Spinner mTownsSp;
	private Spinner mVillageSp;
	private int editType;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_retailer_info_set);

		editType = getIntent().getIntExtra("editType", EDIT_RETAILER_PHONE);

		initView();

		setupView();
	}

	private void initView() {
		mTopbarView = (TopbarView) this.findViewById(R.id.topbar);
		mEditText = (EditText) this.findViewById(R.id.et_retailer_info);

		mSelectAreaLayout = this.findViewById(R.id.layout_select_area);
		mProvinceSp = (Spinner) this.findViewById(R.id.spinner_province);
		mCitySp = (Spinner) this.findViewById(R.id.spinner_city);
		mAreaSp = (Spinner) this.findViewById(R.id.spinner_area);
		mTownsSp = (Spinner) this.findViewById(R.id.spinner_towns);
		mVillageSp = (Spinner) this.findViewById(R.id.spinner_village);
	}

	private void setupView() {
		switch (editType) {
		case EDIT_RETAILER_PHONE:
			mSelectAreaLayout.setVisibility(View.GONE);
			mEditText.setVisibility(View.VISIBLE);

			mTopbarView.setCenterText("联系方式");
			mEditText.setHint("11位手机号码");
			mEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
			break;
		case EDIT_RETAILER_NAME:
			mSelectAreaLayout.setVisibility(View.GONE);
			mEditText.setVisibility(View.VISIBLE);

			mTopbarView.setCenterText("店铺名称");
			mEditText.setHint("店铺名称");
			break;
		case EDIT_RETAILER_ADDRESS:
			mSelectAreaLayout.setVisibility(View.VISIBLE);
			mEditText.setVisibility(View.GONE);
			
			break;
		}
		
		mTopbarView.setLeftClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showToast("遍历，上传信息");
			}
		});
		
	}
}
