package com.szx.crm.workbench.dao;

import com.szx.crm.workbench.domain.TranHistory;

import java.util.List;

public interface TranHistoryDao {

    Boolean save(TranHistory tranHistory);

    List<TranHistory> getHistoryListByTranId(String tranId);
}
