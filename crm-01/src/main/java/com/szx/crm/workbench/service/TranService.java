package com.szx.crm.workbench.service;

import com.szx.crm.workbench.domain.Activity;
import com.szx.crm.workbench.domain.Tran;
import com.szx.crm.workbench.domain.TranHistory;
import com.szx.crm.workbench.exception.ChangeStageExceeption;
import com.szx.crm.workbench.exception.TranSaveException;
import com.szx.crm.workbench.vo.Pagination;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @2021/9/26 @14:03
 */
public interface TranService {

    void savaTran(Tran tran, String customerName) throws TranSaveException;

    Pagination PageList(Map<String,Object> map);

    Tran getTranById(String id);

    List<TranHistory> showHistoryList(String tranId, Map<String,String> map);

    Boolean changeStage(String tranid, TranHistory tranHistory) throws ChangeStageExceeption;
}