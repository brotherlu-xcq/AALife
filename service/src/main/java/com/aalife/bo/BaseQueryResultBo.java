package com.aalife.bo;

import java.util.List;

/**
 * @author mosesc
 * @date 2018-06-11
 */
public class BaseQueryResultBo<T> {

    private List<T> data;

    private int page;

    private int size;

    private long totalElements;

    private int totalPage;

    public BaseQueryResultBo(List<T> data, int page, int size, long totalElements, int totalPage) {
        this.data = data;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPage = totalPage;
    }

    public BaseQueryResultBo() {
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
}
