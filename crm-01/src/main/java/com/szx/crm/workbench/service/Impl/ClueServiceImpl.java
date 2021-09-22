package com.szx.crm.workbench.service.Impl;

import com.szx.crm.workbench.dao.ActivityDao;
import com.szx.crm.workbench.dao.ClueActivityRelationDao;
import com.szx.crm.workbench.dao.ClueDao;
import com.szx.crm.workbench.dao.ClueRemarkDao;
import com.szx.crm.workbench.domain.Activity;
import com.szx.crm.workbench.domain.Clue;
import com.szx.crm.workbench.domain.ClueActivityRelation;
import com.szx.crm.workbench.domain.ClueRemark;
import com.szx.crm.workbench.exception.ActivityClueRelationException;
import com.szx.crm.workbench.service.ClueService;
import com.szx.crm.workbench.vo.Pagination;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @2021/9/16 @14:34
 */
@Service
public class ClueServiceImpl implements ClueService {
    @Resource
    private ActivityDao activityDao;
    @Resource
    private ClueDao clueDao;
    @Resource
    private ClueRemarkDao clueRemarkDao;
    @Resource
    private ClueActivityRelationDao clueActivityRelationDao;

    @Override
    public Pagination<Clue> PageList(Map<String, Object> map) {
        Pagination pagination = new Pagination<Activity>();
        Integer total = clueDao.selectBytotal(map);
        List<Activity> list = clueDao.selectByActivitys(map);
        pagination.setDataList(list);
        pagination.setTotal(total);
        return pagination;
    }

    @Override
    public Boolean save(Clue clue) {
        Boolean flag = clueDao.save(clue);
        return flag;
    }

    @Override
    public Clue getClueById(String id) {
        Clue clue = clueDao.getClueById(id);
        return clue;
    }

    @Override
    public List<ClueRemark> getRemarkListByid(String id) {
        List<ClueRemark> list = clueRemarkDao.getRemarkListByid(id);
        return list;
    }

    @Override
    public Boolean saveRemark(ClueRemark clueRemark) {
        Boolean success = clueRemarkDao.saveRemark(clueRemark);
        return success;
    }

    @Override
    public ClueRemark getRemarkByid(String id) {
        ClueRemark clueRemark = clueRemarkDao.getRemarkByid(id);
        return clueRemark;
    }

    @Override
    public Boolean updateRemark(ClueRemark clueRemark) {
        Boolean success = clueRemarkDao.updateRemark(clueRemark);
        return success;
    }

    @Override
    public Boolean deleteRemark(String id) {
        Boolean success = clueRemarkDao.deleteRemark(id);
        return success;
    }



    @Override
    public Boolean deleteRelation(ClueActivityRelation clueActivityRelation) {
        Boolean success = clueActivityRelationDao.deleteRelation(clueActivityRelation);
        return success;
    }

    @Override
    public List<Activity> getActivityRelationListNotByClueId( Map<String,Object> map) {
        List<Activity> list = activityDao.getActivityRelationListNotByClueId(map);
        return list;
    }

    @Override
    public Boolean Relation(List<ClueActivityRelation> list) throws ActivityClueRelationException {
        Integer flag = 0;
        for (ClueActivityRelation clueActivityRelation : list) {
            Boolean success = clueActivityRelationDao.Relation(clueActivityRelation);
            if (success){
                flag++;
            }
        }
        if (list.size() != flag) {
            throw new ActivityClueRelationException("市场活动关联失败");
        }
        return true;
    }
}
