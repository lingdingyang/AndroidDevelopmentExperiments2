package com.ldy.ex2.POJO;


public class ParagraphContent extends Content {
    private String paragraph;

    public String getParagraph() {
        return paragraph;
    }

    public void setParagraph(String paragraph) {
        this.paragraph = paragraph;
    }

    public ParagraphContent(int belong, int sort, String paragraph) {
        super(belong, sort);
        this.paragraph = paragraph;
    }


    public ParagraphContent(int cid, int belong, int sort, String paragraph) {
        super(cid, belong, sort);
        this.paragraph = paragraph;
    }
}
