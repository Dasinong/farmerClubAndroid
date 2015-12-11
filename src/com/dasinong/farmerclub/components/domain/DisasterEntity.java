package com.dasinong.farmerclub.components.domain;

import java.util.List;

import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper;

/**
 * Created by liuningning on 15/7/14.
 */
public class DisasterEntity extends  BaseResponse {

    public List<FieldEntity.CurrentFieldEntity.Petdisspecws> data;

    public static  class Param{
        public String subStageId;
        public String varietyId;
        
    }
}
