package com.dasinong.farmerclub.database.encyclopedias;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

import com.dasinong.farmerclub.database.common.dao.impl.DaoSupportImpl;
import com.dasinong.farmerclub.database.encyclopedias.domain.Crop;

/**
 * @ClassName EncyclopediasDao
 * @author linmu
 * @Decription 品种
 * @2015-7-22 上午12:06:22
 */
public class EncyclopediasDao extends DaoSupportImpl<Crop> {

	public EncyclopediasDao(Context context) {
		super(context);
	}

	public List<Crop> queryStageCategory(String type) {
		return query("type LIKE ? ", new String[] { "%" + type + "%" });
	}

	public List<Crop> queryCropId(String cropName) {
		return query("cropName = ?", new String[] { cropName });
	}
}
