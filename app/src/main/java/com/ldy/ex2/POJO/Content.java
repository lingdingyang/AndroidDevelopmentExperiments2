package com.ldy.ex2.POJO;



public abstract class Content {
    private int cid;
    private int belong;
    private int sort;

    public Content() {
    }

    public Content(int cid, int belong, int sort) {
        this.cid = cid;
        this.belong = belong;
        this.sort = sort;
    }

    public Content(int belong, int sort) {
        this.belong = belong;
        this.sort = sort;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getBelong() {
        return belong;
    }

    public void setBelong(int belong) {
        this.belong = belong;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "Content{" +
                "cid=" + cid +
                ", belong=" + belong +
                ", sort=" + sort +
                '}';
    }
}
