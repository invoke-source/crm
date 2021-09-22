package com.szx.crm.exceptionHandler;

import com.szx.crm.settings.exception.LoginException;
import com.szx.crm.workbench.exception.*;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 * @2021/6/17 @23:11
 */
@ControllerAdvice
public class CrmExceptionHandler {
    /*用户登录时的异常处理*/
    @ResponseBody
    @ExceptionHandler(value = LoginException.class)
    public Map<String,Object> loginError(Exception e, HttpServletRequest request, HttpServletResponse response){
        Map<String,Object> map = new HashMap<>(16);
        map.put("success",false);
        map.put("msg",e.getMessage());
        return map;
    }
    /*当对Activity进行save操作时的异常处理*/
    @ResponseBody
    @ExceptionHandler(value = ActivityException.class)
    public Map<String,Object> ActivitySaveError(Exception e, HttpServletRequest request, HttpServletResponse response){
        Map<String,Object> map = new HashMap<>(16);
        map.put("success",false);
        map.put("msg",e.getMessage());
        return map;
    }

    /*当对Activity进行delete操作时的异常处理*/
    @ResponseBody
    @ExceptionHandler(value = ActivityDeleteException.class)
    public Map<String,Object> ActivityDeleteError(Exception e, HttpServletRequest request, HttpServletResponse response){
        Map<String,Object> map = new HashMap<>(16);
        map.put("success",false);
        map.put("msg",e.getMessage());
        return map;
    }

    /*当对备注信息进行delete操作时的异常处理*/
    @ResponseBody
    @ExceptionHandler(value = ActivityRemarkDeleteException.class)
    public Map<String,Object> ActivityDeleteRemarkError(Exception e, HttpServletRequest request, HttpServletResponse response){
        Map<String,Object> map = new HashMap<>(16);
        map.put("success",false);
        map.put("msg",e.getMessage());
        return map;
    }

    /*当对备注信息进行save操作时的异常处理*/
    @ResponseBody
    @ExceptionHandler(value = ActivityRemarkSaveException.class)
    public Map<String,Object> saveRemarkError(Exception e, HttpServletRequest request, HttpServletResponse response){
        Map<String,Object> map = new HashMap<>(2);
        map.put("success",false);
        map.put("msg",e.getMessage());
        return map;
    }

    /*当对备注信息进行save操作时的异常处理*/
    @ResponseBody
    @ExceptionHandler(value = ActivityClueRelationException.class)
    public Map<String,Object> RelationError(Exception e, HttpServletRequest request, HttpServletResponse response){
        Map<String,Object> map = new HashMap<>(2);
        map.put("success",false);
        map.put("msg",e.getMessage());
        return map;
    }

}
