package com.joint.jointpolice.model.CollectModels;

/**
 * Created by Joint229 on 2018/5/25.
 */

public class FlatParameter {
    int pageIndex;
    int pageSize;
    String searchInfo;
    int ID;
    int searchType;
    String btime;
    String etime;

    public int getSearchType() {
        return searchType;
    }

    public void setSearchType(int searchType) {
        this.searchType = searchType;
    }

    public String getBtime() {
        return btime;
    }

    public void setBtime(String btime) {
        this.btime = btime;
    }

    public String getEtime() {
        return etime;
    }

    public void setEtime(String etime) {
        this.etime = etime;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getSearchInfo() {
        return searchInfo;
    }

    public void setSearchInfo(String searchInfo) {
        this.searchInfo = searchInfo;
    }
}
