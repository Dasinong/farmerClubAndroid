package com.dasinong.farmerclub.database.disaster.dao.impl;

import android.content.Context;

import com.dasinong.farmerclub.database.common.dao.impl.DaoSupportImpl;
import com.dasinong.farmerclub.database.disaster.dao.CPProductDao;
import com.dasinong.farmerclub.database.disaster.domain.CPProduct;
import com.dasinong.farmerclub.utils.Logger;

import java.util.List;

/**
 * Created by liuningning on 15/6/2.
 */
public class CPProductDaoImpl extends DaoSupportImpl<CPProduct> implements CPProductDao {

    public CPProductDaoImpl(Context context) {
        super(context);
    }

    /**
     * 根据防治方案的id所有的相关的药
     * @param petSoluId
     * @return
     */
    public List<CPProduct> queryAllCpProduct(int petSoluId){
        StringBuffer sql =new StringBuffer();
        sql.append("select cpproduct.* from ")
            .append("cpproduct,petsolu_cpproduct ")
            .append("where petsolu_cpproduct.petSoluId =")
            .append(String.valueOf(petSoluId))
            .append(" and ")
            .append("petsolu_cpproduct.cPProductId =cpproduct.cPProductId");
        Logger.d("CPProductDaoImpl",sql.toString());
        return query(sql.toString());
    }


}


