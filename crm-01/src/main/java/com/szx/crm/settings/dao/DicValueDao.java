package com.szx.crm.settings.dao;

import com.szx.crm.settings.domain.DicValue;

import java.util.List;

/**
 * @author Administrator
 * @2021/9/16 @14:54
 */
public interface DicValueDao {
    List<DicValue> getListByCode(String code);
}
