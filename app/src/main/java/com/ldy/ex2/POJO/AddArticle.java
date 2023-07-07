package com.ldy.ex2.POJO;

public class AddArticle {
    private final int position;
    boolean isParagraph;

    public int getPosition() {
        return position;
    }

    public boolean isParagraph() {
        return isParagraph;
    }

    public AddArticle(int position, boolean isParagraph) {
        this.position = position;
        this.isParagraph = isParagraph;
    }
}
