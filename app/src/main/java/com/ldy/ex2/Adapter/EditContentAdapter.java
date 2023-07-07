package com.ldy.ex2.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ldy.ex2.Activities.AddActivity;
import com.ldy.ex2.POJO.AddArticle;
import com.ldy.ex2.POJO.Content;
import com.ldy.ex2.POJO.EditImage;
import com.ldy.ex2.POJO.ImageContent;
import com.ldy.ex2.POJO.ParagraphContent;
import com.ldy.ex2.R;
import com.ldy.ex2.Utils.EditTextWatcher;

import java.util.List;

public class EditContentAdapter extends ArrayAdapter<Content> {
    //    imageContent对应的布局文件ID
    private final int imageResourceId;
    //    paragraphContent对应的布局文件ID
    private final int paragraphResourceId;
    //    使用AddActivity对象来设置监听点击事件
    private final AddActivity clickListener;

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Content content = getItem(position);
        View view;
//        通过try catch 捕获 content 强转成ImageContent是否出现异常来判断这个Content是ImageContent还是ParagraphContent
        try {
            ImageContent imageContent = (ImageContent) content;
//            如果是ImageContent，使用对应的布局
            view = LayoutInflater.from(getContext()).inflate(imageResourceId, null);
            Button btn_iBottomAddImage = view.findViewById(R.id.btn_iBottomAddImage);
            Button btn_iBottomAddParagraph = view.findViewById(R.id.btn_iBottomAddParagraph);
            ImageButton btn_iDelete = view.findViewById(R.id.btn_iDelete);
            ImageButton btn_iEditImage = view.findViewById(R.id.btn_iEditImage);
            ImageButton btn_iEditCamera = view.findViewById(R.id.btn_iEditCamera);
//            设置删除点击事件监听
            btn_iDelete.setOnClickListener(clickListener);
//            通过setTag传递数据，点击事件会调用可以获取content对象
            btn_iDelete.setTag(content);
            ImageView image_editContent = view.findViewById(R.id.image_editContent);
//            设置添加图片和段落的按钮点击事件监听
//            传递的数据包括是第几个content和添加的是否是ParagraphContent
            btn_iBottomAddImage.setTag(new AddArticle(position + 1, false));
            btn_iBottomAddParagraph.setTag(new AddArticle(position + 1, true));
            btn_iBottomAddImage.setOnClickListener(clickListener);
            btn_iBottomAddParagraph.setOnClickListener(clickListener);
            if (!imageContent.getImgPath().isEmpty()) {
//                加载图片
                image_editContent.setImageURI(Uri.parse(imageContent.getImgPath()));
            } else {
//                如果没有设置图片，使用默认的图片
                image_editContent.setImageResource(R.drawable.default_0);
            }
//            添加使用相机拍照和使用相册图片的点击事件监听
//            使用tag传递数据，传递的数据包括当前content和显示图片的ImageView
            btn_iEditCamera.setOnClickListener(clickListener);
            btn_iEditCamera.setTag(new EditImage(content, image_editContent));
            btn_iEditImage.setTag(new EditImage(content, image_editContent));
            btn_iEditImage.setOnClickListener(clickListener);
        } catch (Exception e) {
            ParagraphContent paragraphContent = (ParagraphContent) content;
//            使用ParagraphContent的布局文件
            view = LayoutInflater.from(getContext()).inflate(paragraphResourceId, null);
            Button btn_pBottomAddImage = view.findViewById(R.id.btn_pBottomAddImage);
            Button btn_pBottomAddParagraph = view.findViewById(R.id.btn_pBottomAddParagraph);
            ImageButton btn_pDelete = view.findViewById(R.id.btn_pDelete);
//            添加删除点击事件监听
            btn_pDelete.setOnClickListener(clickListener);
//            使用Tag传递数据
            btn_pDelete.setTag(content);
            EditText edit_editContent = view.findViewById(R.id.edit_editContent);
//            设置添加图片和段落的按钮点击事件监听
//            传递的数据包括是第几个content和添加的是否是ParagraphContent
            btn_pBottomAddImage.setTag(new AddArticle(position + 1, false));
            btn_pBottomAddParagraph.setTag(new AddArticle(position + 1, true));
            btn_pBottomAddImage.setOnClickListener(clickListener);
            btn_pBottomAddParagraph.setOnClickListener(clickListener);
//            设置文字
            edit_editContent.setText(paragraphContent.getParagraph());
            edit_editContent.setTag(content);
//            添加监听editContent内容变化，因为一般情况下EditTextWatcher的回调方法提供的参数没有该EditText对象，所以使用了自定义的EditTextWatcher,并需要传递EditText对象作为参数
            edit_editContent.addTextChangedListener(new EditTextWatcher(edit_editContent, clickListener));
        }

        return view;
    }

    //构造方法
    public EditContentAdapter(@NonNull Context context, int imageResourceId,
                              int paragraphResourceId, @NonNull List<Content> objects, AddActivity activity) {
        super(context, imageResourceId, objects);
        this.imageResourceId = imageResourceId;
        this.paragraphResourceId = paragraphResourceId;
        clickListener = activity;
    }


}
