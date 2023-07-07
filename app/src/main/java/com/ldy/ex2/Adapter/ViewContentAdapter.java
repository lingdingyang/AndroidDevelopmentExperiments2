package com.ldy.ex2.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ldy.ex2.POJO.Content;
import com.ldy.ex2.POJO.ImageContent;
import com.ldy.ex2.POJO.ParagraphContent;
import com.ldy.ex2.R;

import java.util.List;

public class ViewContentAdapter extends ArrayAdapter<Content> {
    private final int imageResourceId;
    private final int paragraphResourceId;

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Content content = getItem(position);
        View view;
        try {
            ImageContent imageContent = (ImageContent) content;
            view = LayoutInflater.from(getContext()).inflate(imageResourceId, null);
            ImageView image_viewContent = view.findViewById(R.id.image_viewContent);
            if (!imageContent.getImgPath().isEmpty()) {
                image_viewContent.setImageURI(Uri.parse(imageContent.getImgPath()));
            } else {
                image_viewContent.setImageResource(R.drawable.default_0);
            }

        } catch (Exception e) {
            ParagraphContent paragraphContent = (ParagraphContent) content;
            view = LayoutInflater.from(getContext()).inflate(paragraphResourceId, null);
            TextView text_viewContent = view.findViewById(R.id.text_viewContent);
            text_viewContent.setText(paragraphContent.getParagraph());
        }

        return view;
    }

    public ViewContentAdapter(@NonNull Context context, int imageResourceId, int paragraphResourceId, @NonNull List<Content> objects) {
        super(context, imageResourceId, objects);
        this.imageResourceId = imageResourceId;
        this.paragraphResourceId = paragraphResourceId;

    }
}
