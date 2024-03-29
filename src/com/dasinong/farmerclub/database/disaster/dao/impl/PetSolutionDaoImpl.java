package com.dasinong.farmerclub.database.disaster.dao.impl;

import android.content.Context;

import com.dasinong.farmerclub.database.common.dao.impl.DaoSupportImpl;
import com.dasinong.farmerclub.database.disaster.dao.PetSolutionDao;
import com.dasinong.farmerclub.database.disaster.domain.PetSolu;

import java.util.List;

/**
 * Created by liuningning on 15/6/2.
 */
public class PetSolutionDaoImpl extends DaoSupportImpl<PetSolu> implements PetSolutionDao {


    public PetSolutionDaoImpl(Context context) {
        super(context);
    }

    /**
     * 病虫草的id所有的治疗方案
     *
     * @param petDisSpecId 病虫草的id
     * @return
     */
    public List<PetSolu> QuerySolutionIsCure(int petDisSpecId) {

        return query("petDisSpecId= ? and isRemedy = ?", new String[]{String.valueOf(petDisSpecId), "1"});
    }


    /**
     * 病虫草的id所有的预防方案
     *
     * @param petDisSpecId 病虫草的id
     * @return
     */
    public List<PetSolu> QuerySolutionIsPrevent(int petDisSpecId) {

        return query("petDisSpecId= ? and isRemedy=?", new String[]{String.valueOf(petDisSpecId), "0"});
    }

}


