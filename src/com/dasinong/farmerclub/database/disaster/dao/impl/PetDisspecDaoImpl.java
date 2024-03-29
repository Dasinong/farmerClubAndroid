package com.dasinong.farmerclub.database.disaster.dao.impl;

import android.content.Context;

import com.dasinong.farmerclub.database.common.dao.impl.DaoSupportImpl;
import com.dasinong.farmerclub.database.disaster.dao.PetDisspecDao;
import com.dasinong.farmerclub.database.disaster.domain.PetDisspec;

import java.util.List;

/**
 * Created by liuningning on 15/6/2.
 */
public class PetDisspecDaoImpl extends DaoSupportImpl<PetDisspec>  implements PetDisspecDao {


    public PetDisspecDaoImpl(Context context) {
        super(context);
    }

    /**
     * 根据类型得所有的数据
     * @param typeValue 类型的值
     * @return 根据类型得所有的数据
     */
    public  List<PetDisspec> queryDisasterByType(String typeValue){

        return  query("type = ? ",new String[]{typeValue});
    }
    
    /**
     * xiyao
     */
    public  List<PetDisspec> queryDisasterByTypeandCropName(String[] value){

        return  query("type = ? and cropName = ? ",value);
    }

    @Override
    public PetDisspec queryDisasterById(int id) {
        List<PetDisspec> result = query("petDisSpecId= ?", new String[]{String.valueOf(id)});
        if(result.isEmpty()){
            return  new PetDisspec();
        }
        return result.get(0);
    }

}


