package com.dasinong.farmerclub.database.task.dao.impl;

import android.content.Context;
import android.database.Cursor;

import com.dasinong.farmerclub.database.common.dao.impl.DaoSupportImpl;
import com.dasinong.farmerclub.database.task.domain.SubStage;
import com.dasinong.farmerclub.database.task.domain.TaskSpec;
import com.dasinong.farmerclub.utils.Logger;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuningning on 15/6/6.
 */
public class SubStageDaoImpl extends DaoSupportImpl<SubStage> {
	public SubStageDaoImpl(Context context) {
		super(context);
	}

	/**
	 * 
	 * @return
	 */
	public List<SubStage> queryStageSubCategory(String stageName) {

		return query("stageName = ? ", new String[] { stageName }, "subStageId");
	}

	/**
	 * 
	 * @return
	 */
	public List<String> queryStageCategory() {

		StringBuffer sb = new StringBuffer();
		sb.append("select distinct stageName ").append("from ").append("substage ");
		return querySingleColumn(sb.toString());
	}

	/**
	 * 
	 * @return
	 */
	public List<String> queryStageCategory(int min, int max) {

		StringBuffer sb = new StringBuffer();
		sb.append("select distinct stageName ").append("from ").append("substage ")
				.append("where substageId>" + (min - 1) + " and substageId<" + (max + 1));

		return querySingleColumn(sb.toString());
	}

	/**
	 * 
	 * @return
	 */
	public SubStage queryStageByID(String id) {
		if (query("subStageId = ? ", new String[] { id }, "subStageId").isEmpty()) {
			return null;
		}
		return query("subStageId = ? ", new String[] { id }, "subStageId").get(0);
	}

	/**
	 * 
	 * @return
	 */
	public List<SubStage> queryAllOderBy() {

		return query(null, null, "subStageId");
	}

	public List<SubStage> queryByCropName(int min, int max) {
		String[] selectionArgs = { (min - 1) + "", (max + 1) + "" };
		return query("substageId>?and substageId<?", selectionArgs, "subStageId");
	}

}
