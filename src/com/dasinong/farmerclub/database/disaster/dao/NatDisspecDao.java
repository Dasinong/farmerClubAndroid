package com.dasinong.farmerclub.database.disaster.dao;

import com.dasinong.farmerclub.database.common.dao.DaoSupport;
import com.dasinong.farmerclub.database.disaster.domain.NatDisspec;

import java.util.List;


/**
 * Created by liuningning on 15/6/2.
 */
public interface NatDisspecDao extends DaoSupport<NatDisspec> {

    /**
     *
     * @return 返回所有的自然灾害
     */
    public List<NatDisspec> queryAllNatDisaster();
}
