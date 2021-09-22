package com.szx.crm.settings.service.Impl;

import com.szx.crm.settings.dao.DicTypeDao;
import com.szx.crm.settings.dao.DicValueDao;
import com.szx.crm.settings.domain.DicType;
import com.szx.crm.settings.domain.DicValue;
import com.szx.crm.settings.service.DicService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @2021/9/16 @14:57
 */
@Service(value = "DicServiceImpl")
public class DicServiceImpl implements DicService {
    @Resource
    private DicTypeDao dicTypeDao;
    @Resource
    private DicValueDao dicValueDao;

    @Override
    public Map<String, List<DicValue>> getAll() {
        Map<String,List<DicValue>> map = new HashMap<>();
        //先取出tbl_dic_type 表中的主键code
        List<DicType> typeList = dicTypeDao.getAll();
        //取出tbl_dic_value中的数据，typeCode分组
        for (DicType type : typeList) {
            String code = type.getCode();
            List<DicValue> valueList = dicValueDao.getListByCode(code);
            map.put(code,valueList);
        }
        return map;
    }
}
