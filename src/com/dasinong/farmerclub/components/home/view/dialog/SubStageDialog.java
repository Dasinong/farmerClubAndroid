package com.dasinong.farmerclub.components.home.view.dialog;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.entity.FieldDetailEntity.StageEntity;
import com.dasinong.farmerclub.ui.soil.adapter.CommonAdapter;
import com.dasinong.farmerclub.ui.soil.adapter.ViewHolder.ViewHolder;

import java.util.List;

/**
 * @author
 */
public class SubStageDialog extends BaseDialog implements AdapterView.OnItemClickListener, View.OnClickListener {

	private ViewGroup contentView;

	private ListView mListView;

	private List<StageEntity> mSubStageList;

	private int mCurrentPosition;

	public OnItemClickListener mOnItemClickLisenter;

	public SubStageDialog(Context context) {
		super(context);
		this.mContext = context;
	}

	@Override
	protected View onAddConentView() {
		contentView = (ViewGroup) inflateView(R.layout.dialog_substage);
		return contentView;
	}

	@Override
	protected void initView() {
		mListView = (ListView) contentView.findViewById(R.id.substage_listview);

	}

	@Override
	protected void initData() {

	}

	public void setDataSource(List<StageEntity> subStages, int position) {
		this.mSubStageList = subStages;
		mCurrentPosition = position;
		mListView.setAdapter(new CommonAdapter<StageEntity>(mSubStageList) {
			@Override
			protected int getResourceId() {
				return R.layout.substage_item;
			}

			@Override
			protected void updateView(StageEntity result, ViewHolder viewHolder, int position) {
				if (result.stageName.equals(result.subStageName)) {
					viewHolder.setTextValue(R.id.substage_item_text, result.stageName);
				} else {
					viewHolder.setTextValue(R.id.substage_item_text, result.stageName + "-" + result.subStageName);
				}
				RadioButton radioButton = viewHolder.getView(R.id.rb_check);
				if (mCurrentPosition == position) {
					radioButton.setChecked(true);
				} else {
					radioButton.setChecked(false);
				}

			}
		});
	}

	@Override
	protected void setListener() {
		mListView.setOnItemClickListener(this);
		findViewById(R.id.substage_ok).setOnClickListener(this);
		findViewById(R.id.substage_cancel).setOnClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		CommonAdapter adapter = (CommonAdapter) parent.getAdapter();
		mCurrentPosition = position;
		adapter.notifyDataSetChanged();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.substage_ok:

			if (mOnItemClickLisenter != null) {
				mOnItemClickLisenter.onItemClick(mCurrentPosition);
			}

			this.dismiss();
			break;
		case R.id.substage_cancel:
			this.dismiss();
			break;
		default:
			break;
		}
	}

	public void setOnItemClickListener(OnItemClickListener listener) {
		this.mOnItemClickLisenter = listener;

	}

	public interface OnItemClickListener {

		void onItemClick(int position);
	}

}