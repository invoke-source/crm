package com.szx.crm.workbench.vo;

import java.util.List;

/**
 * @author Administrator
 * @2021/6/20 @12:25
 */
public class Pagination<T> {
    private Integer total;
    private List<T> dataList;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }
}
