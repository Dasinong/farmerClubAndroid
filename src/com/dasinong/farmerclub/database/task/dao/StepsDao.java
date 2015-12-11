package com.dasinong.farmerclub.database.task.dao;

import com.dasinong.farmerclub.database.common.dao.DaoSupport;
import com.dasinong.farmerclub.database.task.domain.Steps;

import java.util.List;

/**
 * Created by liuningning on 15/6/6.
 */
public interface StepsDao extends DaoSupport<Steps>  {
    /**
     * 根据taskSpecId 查询所有的steps
     * @param taskSpecId
     * @return
     */

    public List<Steps> queryStepsWithTaskSpecId(int taskSpecId);
}
