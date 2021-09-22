package com.szx.crm.workbench.dao;

import com.szx.crm.workbench.domain.ActivityRemark;

import java.util.List;

/**
 * @author Administrator
 * @2021/9/11 @8:11
 */
public interface ActivityRemarkDao {
     Integer selectByids(String[] ids);


     Integer deleteByids(String[] ids) ;

    List<ActivityRemark> getRemarkListByid(String id);

    Boolean deleteRemark(String id);

    Boolean saveRemark(ActivityRemark activityRemark);

    ActivityRemark getRemarkByid(String id);

    Boolean updateRemark(ActivityRemark activityRemark);
}
