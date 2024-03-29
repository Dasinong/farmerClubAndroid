package com.dasinong.farmerclub.database.task.dao.impl;

import android.content.Context;

import com.dasinong.farmerclub.database.common.dao.impl.DaoSupportImpl;
import com.dasinong.farmerclub.database.task.dao.StepsDao;
import com.dasinong.farmerclub.database.task.domain.Steps;
import com.dasinong.farmerclub.database.task.domain.TaskSpec;

import java.util.List;

/**
 * Created by liuningning on 15/6/6.
 */
public class StepsDaoImpl extends DaoSupportImpl<Steps> implements StepsDao {
    public StepsDaoImpl(Context context) {
        super(context);
    }

    /**
     * 根据taskSpecId 查询所有的steps
     * @param taskSpecId
     * @return
     */

    public List<Steps> queryStepsWithTaskSpecId(int taskSpecId){
        return  query("taskSpecId = ? ",new String[]{String.valueOf(taskSpecId)});

    }
}
