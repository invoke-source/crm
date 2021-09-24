package com.szx.crm.workbench.service.Impl;

import com.szx.crm.workbench.dao.ActivityDao;
import com.szx.crm.workbench.dao.ActivityRemarkDao;
import com.szx.crm.workbench.dao.ClueActivityRelationDao;
import com.szx.crm.workbench.domain.Activity;
import com.szx.crm.workbench.domain.ActivityRemark;
import com.szx.crm.workbench.exception.ActivityDeleteException;
import com.szx.crm.workbench.exception.ActivityException;
import com.szx.crm.workbench.exception.ActivityRemarkDeleteException;
import com.szx.crm.workbench.exception.ActivityRemarkSaveException;
import com.szx.crm.workbench.service.ActivityService;
import com.szx.crm.workbench.vo.Pagination;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @2021/6/19 @16:23
 */
@Service
public class ActivityServiceImpl implements ActivityService {
    @Resource
    private ActivityDao activityDao;
    @Resource
    private ActivityRemarkDao activityRemarkDao;
    @Resource
    private ClueActivityRelationDao clueActivityRelationDao;
    @Override
    public boolean saveActivity(Activity activity) throws ActivityException {
        if (activityDao.saveActivity(activity)!=1){
            throw new ActivityException("保存失败");
        }

        return true;
    }

    @Override
    public Pagination<Activity> PageList(Map<String, Object> map) {
        Pagination pagination = new Pagination<Activity>();
        Integer total = activityDao.selectBytotal(map);
        List<Activity> list = activityDao.selectByActivitys(map);
        pagination.setDataList(list);
        pagination.setTotal(total);
        return pagination;
    }

    @Override
    public boolean deleteActivity(String[] ids) throws ActivityDeleteException {
        //查询出要删除的备注的记录条数
        int selectCount = activityRemarkDao.selectByids(ids);
        //实际删除备注的记录条数
        int deleteCount = activityRemarkDao.deleteByids(ids);
        //查询出的记录条数和实际删除备注的记录条数不匹配，删除失败
        if (selectCount != deleteCount  ){
            //删除备注失败，抛出异常，回滚事务
            throw new ActivityDeleteException("删除失败");
        }
        //删除用户记录
        int count = activityDao.deleteByids(ids);
        if (count != ids.length){
            //用户记录删除失败，回滚事务
            throw new ActivityDeleteException("删除失败");
        }
        return true;
    }

    @Override
    public Activity selectById(String id) {
        Activity activity = activityDao.selectById(id);
        return activity;
    }

    @Override
    public boolean updateActivity(Activity activity) throws ActivityException{
        if (activityDao.updateActivity(activity)!=1){
            throw new ActivityException("修改失败");
        }

        return true;
    }

    @Override
    public Activity detail(String id) {
        Activity activity = activityDao.detail(id);
        return activity;
    }

    @Override
    public List<ActivityRemark> getRemarkListByid(String id) {
        List<ActivityRemark> list = activityRemarkDao.getRemarkListByid(id);
        return list;
    }

    @Override
    public Boolean deleteRemark(String id) throws ActivityRemarkDeleteException {
        Boolean flag = activityRemarkDao.deleteRemark(id);
        if (!flag){
            throw new ActivityRemarkDeleteException("备注信息删除失败");
        }
        return flag;
    }

    @Override
    public Boolean saveRemark(ActivityRemark activityRemark) throws ActivityRemarkSaveException {
        Boolean flag = activityRemarkDao.saveRemark(activityRemark);
        if (!flag){
            throw new ActivityRemarkSaveException("备注信息保存失败");
        }
        return flag;
    }

    @Override
    public ActivityRemark getRemarkByid(String id) {
        ActivityRemark activityRemark = activityRemarkDao.getRemarkByid(id);
        return activityRemark;
    }

    @Override
    public Boolean updateRemark(ActivityRemark activityRemark) {
        Boolean flag = activityRemarkDao.updateRemark(activityRemark);
        return flag;
    }

    @Override
    public List<Activity> getActivityListByClueId(String clueId) {
        List<Activity> list =clueActivityRelationDao.getActivityListByClueId(clueId);
        return list;
    }

    @Override
    public List<Activity> getActivityList() {
        List<Activity> list = activityDao.getActivityList();
        return list;
    }
    @Override
    public List<Activity> getActivityListByName(String name) {
        List<Activity> list = activityDao.getActivityListByName(name);
        return list;
    }



}
