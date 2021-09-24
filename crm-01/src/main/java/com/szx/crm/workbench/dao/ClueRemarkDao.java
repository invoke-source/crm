package com.szx.crm.workbench.dao;

import com.szx.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkDao {

    List<ClueRemark> getRemarkListByid(String id);

    Boolean saveRemark(ClueRemark clueRemark);

    ClueRemark getRemarkByid(String id);

    Boolean updateRemark(ClueRemark clueRemark);

    Boolean deleteRemark(String id);

    List<ClueRemark> getListByClueId(String id);
}
