package com.szx.crm.workbench.service.Impl;

import com.szx.crm.workbench.dao.TranDao;
import com.szx.crm.workbench.service.ChartService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @2021/9/29 @12:23
 */
@Service
public class ChartServiceImpl implements ChartService {
    @Resource
    private TranDao tranDao;
    @Override
    public Map<String, Object> getCharts() {
        //获取所有阶段数量totalSize
        Integer total = tranDao.getStageTotal();
        //获取阶段与其数量的列表dataLists
        List<Map<String,Object>> dataList = tranDao.dataLists();
        Map<String,Object> map = new HashMap(3);
        map.put("total",total);
        map.put("dataList",dataList);
        return map;
    }
}
