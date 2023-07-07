package com.ldy.ex2.POJO;

import java.util.Date;
import java.util.List;


public class Article {
    private int aid;
    private String title;
    private int author;
    private String createTime;
    private List<Content> contents;

    public Article(String title, int author, List<Content> contents) {
        this.title = title;
        this.author = author;
        this.contents = contents;
    }

    public Article(int aid, String title, int author, List<Content> contents) {
        this.aid = aid;
        this.title = title;
        this.author = author;
        this.contents = contents;
    }

    public Article(int aid, String title, int author, String createTime, List<Content> contents) {
        this.aid = aid;
        this.title = title;
        this.author = author;
        this.createTime = createTime;
        this.contents = contents;
    }

    @Override
    public String toString() {
        return "Article{" +
                "aid=" + aid +
                ", title='" + title + '\'' +
                ", author=" + author +
                ", createTime=" + createTime +
                ", contents=" + contents +
                '}';
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAuthor() {
        return author;
    }

    public void setAuthor(int author) {
        this.author = author;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public List<Content> getContents() {
        return contents;
    }

    public void setContents(List<Content> contents) {
        this.contents = contents;
    }
}
