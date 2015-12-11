package com.dasinong.farmerclub.database.encyclopedias;

import java.util.List;

import android.content.Context;

import com.dasinong.farmerclub.database.common.dao.impl.DaoSupportImpl;
import com.dasinong.farmerclub.database.encyclopedias.domain.Petdisspecbrowse;
import com.dasinong.farmerclub.database.encyclopedias.domain.Varietybrowse;

public class VarietybrowseDao extends DaoSupportImpl<Varietybrowse> {

	public VarietybrowseDao(Context context) {
		super(context);
	}

	public List<Varietybrowse> query(String cropId) {
		return query("cropId = ? ",new String[]{cropId+""});
	}
	
}
