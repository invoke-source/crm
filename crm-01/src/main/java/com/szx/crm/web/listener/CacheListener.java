package com.szx.crm.web.listener;

import com.szx.crm.settings.domain.DicValue;
import com.szx.crm.settings.service.DicService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Administrator
 * @2021/9/18 @22:00
 */
public class CacheListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        WebApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
        System.out.println("服务器处理数据字典开始");
        DicService dicService = (DicService) applicationContext.getBean("DicServiceImpl");
       //向service 索要Map<String,List<DicValue>>,取出数据字典
        Map<String, List<DicValue>> dicMap =  dicService.getAll();
        Set<String> set = dicMap.keySet();
        for (String s : set) {
            servletContext.setAttribute(s,dicMap.get(s));
        }
        System.out.println("服务器处理数据字典结束");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
