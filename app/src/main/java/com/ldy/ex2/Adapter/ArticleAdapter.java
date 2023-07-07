package com.ldy.ex2.Adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ldy.ex2.Activities.ListActivity;
import com.ldy.ex2.POJO.Article;
import com.ldy.ex2.POJO.Content;
import com.ldy.ex2.POJO.ImageContent;
import com.ldy.ex2.POJO.ParagraphContent;
import com.ldy.ex2.R;

import java.util.List;

public class ArticleAdapter extends ArrayAdapter<Article> {
    private int resourceId;
    private String userName;
    private ListActivity clickContext;

    public ArticleAdapter(@NonNull Context context, int resource, @NonNull List<Article> objects, String userName, ListActivity listActivity) {
        super(context, resource, objects);
        this.resourceId = resource;
        this.userName = userName;
        clickContext = listActivity;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Article article = getItem(position);
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        } else {
            view = convertView;
        }
        TextView text_title = view.findViewById(R.id.text_title);
        TextView text_author = view.findViewById(R.id.text_author);
        TextView text_time = view.findViewById(R.id.text_time);
        ImageView img_article = view.findViewById(R.id.img_article);
        TextView text_content = view.findViewById(R.id.text_content);
        TextView text_articleId = view.findViewById(R.id.text_articleId);
        Log.d("getView", article.getAid() + "");
        text_articleId.setText(article.getAid() + "");
        text_title.setText(article.getTitle());
        text_author.setText(userName);
        text_time.setText(article.getCreateTime());
        List<Content> contents = article.getContents();
        if (contents.size() > 0) {
            text_content.setText(((ParagraphContent) contents.get(0)).getParagraph());
        }
        String imagePath = getFirstImage(article.getContents());
        if (imagePath != null) {
            img_article.setImageURI(Uri.parse(imagePath));
        } else {
            img_article.setVisibility(View.GONE);
        }
        view.setOnClickListener(clickContext);
        view.setOnLongClickListener(clickContext);
        return view;
    }

    String getFirstImage(List<Content> contentList) {
        for (int i = 0; i < contentList.size(); i++) {
            Content content = contentList.get(i);
            try {
                ImageContent imageContent = (ImageContent) content;
                if (!imageContent.getImgPath().isEmpty()) {
                    return imageContent.getImgPath();
                }
            } catch (Exception ignored) {
            }
        }
        return null;
    }
}
