package com.szx.crm.settings.service;

import com.szx.crm.settings.domain.DicValue;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @2021/9/16 @14:57
 */
public interface DicService {
    Map<String, List<DicValue>> getAll();
}
