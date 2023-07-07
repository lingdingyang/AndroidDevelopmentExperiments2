package com.ldy.ex2.POJO;


public class ImageContent extends Content {
    private String imgPath;

    public ImageContent(int belong, int sort, String imgPath) {
        super(belong, sort);
        this.imgPath = imgPath;
    }


    public ImageContent(int cid, int belong, int sort, String imgPath) {
        super(cid, belong, sort);
        this.imgPath = imgPath;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}
