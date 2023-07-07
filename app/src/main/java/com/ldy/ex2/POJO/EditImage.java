package com.ldy.ex2.POJO;

import android.widget.ImageView;

public class EditImage {
    Content content;
    ImageView imageView;

    public Content getContent() {
        return content;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public EditImage(Content content, ImageView imageView) {
        this.content = content;
        this.imageView = imageView;
    }
}
