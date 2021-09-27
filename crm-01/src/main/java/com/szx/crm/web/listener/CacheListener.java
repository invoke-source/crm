package com.szx.crm.web.listener;

import com.szx.crm.settings.domain.DicValue;
import com.szx.crm.settings.service.DicService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

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

        //处理阶段与可能性的对应关系解析Stage2Possibility.properties配置文件并存入到上下文对象中
        Map<String,String> pmap = new HashMap<>();
        ResourceBundle rb = ResourceBundle.getBundle("conf/Stage2Possibility");
        Enumeration<String> e = rb.getKeys();
        while (e.hasMoreElements()){
            String key = e.nextElement();
            String value = rb.getString(key);
            pmap.put(key,value);
        }
        servletContext.setAttribute("pmap",pmap);

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
