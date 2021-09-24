package com.szx.crm.workbench.service;

import com.szx.crm.workbench.domain.*;
import com.szx.crm.workbench.exception.ActivityClueRelationException;
import com.szx.crm.workbench.exception.ClueConvertException;
import com.szx.crm.workbench.vo.Pagination;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @2021/9/16 @14:34
 */
public interface ClueService {
     Pagination<Clue> PageList(Map<String, Object> map);

    Boolean save(Clue clue);

    Clue getClueById(String id);

    List<ClueRemark> getRemarkListByid(String id);

    Boolean saveRemark(ClueRemark clueRemark);

    ClueRemark getRemarkByid(String id);

    Boolean updateRemark(ClueRemark clueRemark);

    Boolean deleteRemark(String id);



    Boolean deleteRelation(ClueActivityRelation clueActivityRelation);

    List<Activity> getActivityRelationListNotByClueId( Map<String,Object> map);

    Boolean Relation(List<ClueActivityRelation> list) throws ActivityClueRelationException;

    void convert(String id, Tran tran) throws ClueConvertException;
}
