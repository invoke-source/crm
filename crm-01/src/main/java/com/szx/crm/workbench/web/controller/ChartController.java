package com.szx.crm.workbench.web.controller;

import com.szx.crm.workbench.service.ChartService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author Administrator
 * @2021/9/29 @12:22
 */
@Controller
public class ChartController {
    @Resource
    private ChartService chartService;

    @RequestMapping(value = "workbench/chart/getCharts.do")
    @ResponseBody
    public Map<String,Object>  getCharts(){
        Map<String,Object> map =  chartService.getCharts();
        return map;
    }
}
