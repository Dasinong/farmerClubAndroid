package com.dasinong.farmerclub.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.dasinong.farmerclub.R;
import com.dasinong.farmerclub.database.disaster.domain.NatDisspec;
import com.dasinong.farmerclub.database.disaster.domain.PetDisspec;
import com.dasinong.farmerclub.database.disaster.service.DisasterManager;
import com.dasinong.farmerclub.ui.HarmDetailsActivity;
import com.dasinong.farmerclub.ui.adapter.HarmAdapter;

/**
 * 
 * @author Ming 此类为病虫草害列表页每个Indicator对应的Fragment
 */

public class HarmFragment extends Fragment {

	private List<PetDisspec> list = new ArrayList<PetDisspec>();
	private int fragmentPosition;

	public static HarmFragment newInstance(int position) {

		HarmFragment myFragment = new HarmFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("position", position);
		myFragment.setArguments(bundle);

		return myFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		fragmentPosition = getArguments() != null ? getArguments().getInt("position") : 0;

		initdata(fragmentPosition);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = View.inflate(getActivity(), R.layout.fragment_harm, null);
		ListView lv_harm = (ListView) view.findViewById(R.id.lv_harm);

		lv_harm.setAdapter(new HarmAdapter<PetDisspec>(getActivity(), list, false));

		lv_harm.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(getActivity(), HarmDetailsActivity.class);

				PetDisspec pet = (PetDisspec) list.get(position);
				Bundle bundle = new Bundle();
				bundle.putString("type", HarmDetailsActivity.FLAG_ITEM);
				bundle.putString("id", pet.petDisSpecId+"");
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		return view;
	}

	// 加载数据
	private void initdata(int position) {
		String type = null;

		DisasterManager manager = DisasterManager.getInstance(getActivity());
		switch (position) {
		case 0:
			type = "病害";
			break;
		case 1:
			type = "虫害";
			break;
		case 2:
			type = "草害";
			break;
		}
		list = manager.getDisease(type, "水稻");
	}
}
