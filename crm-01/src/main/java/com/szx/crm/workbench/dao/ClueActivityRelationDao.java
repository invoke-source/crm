package com.szx.crm.workbench.dao;

import com.szx.crm.workbench.domain.Activity;
import com.szx.crm.workbench.domain.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationDao {


    List<Activity> getActivityListByClueId(String clueId);

    Boolean deleteRelation(ClueActivityRelation clueActivityRelation);

    Boolean Relation(ClueActivityRelation clueActivityRelation);
}
