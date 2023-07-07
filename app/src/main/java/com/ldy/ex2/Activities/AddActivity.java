package com.ldy.ex2.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.ldy.ex2.Adapter.EditContentAdapter;
import com.ldy.ex2.DAO.ArticleDao;
import com.ldy.ex2.DAO.ContentDao;
import com.ldy.ex2.POJO.AddArticle;
import com.ldy.ex2.POJO.Article;
import com.ldy.ex2.POJO.Content;
import com.ldy.ex2.POJO.EditImage;
import com.ldy.ex2.POJO.ImageContent;
import com.ldy.ex2.POJO.ParagraphContent;
import com.ldy.ex2.R;
import com.ldy.ex2.Utils.CopyFileByUri;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddActivity extends AppCompatActivity implements View.OnClickListener {

    private List<Content> contents;
    private EditContentAdapter editContentAdapter;
    private EditText edit_title;
    private int userId;
    private ArticleDao articleDao = null;
    private ContentDao contentDao = null;
    private boolean is_newArticle;
    private int articleID;
    private Article article;
    private ImageView imageView;
    private ActivityResultLauncher<Intent> resultLauncher;
    private AddActivity that;
    private Content editContent;
    private Uri imageUri;
    static final int TAKE_PHOTO = 123;

    public List<Content> getContents() {
        return contents;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add);
        that = this;
        userId = getIntent().getIntExtra("userID", -1);
        is_newArticle = getIntent().getBooleanExtra("is_newArticle", true);
        articleDao = ArticleDao.getInstance(this);
        if (is_newArticle) {
            contents = new ArrayList<>();
            contents.add(new ParagraphContent(userId, 0, ""));
        } else {
            articleID = getIntent().getIntExtra("articleID", -1);
            article = articleDao.getArticle(articleID);
            contents = article.getContents();
        }
        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    Intent intent = result.getData();
                    Uri data = intent.getData();
                    if (data != null) {
                        Log.d("registerForActivityResult", data.toString());
                        String res = CopyFileByUri.getPath(that, data);
                        imageView.setImageURI(Uri.parse(res));
                        for (int i = 0; i < contents.size(); i++) {
                            if (contents.get(i).equals(editContent)) {
                                ((ImageContent) contents.get(i)).setImgPath(res);
                                editContentAdapter.notifyDataSetChanged();
                                break;
                            }
                        }
                    }
                }
            }
        });
        editContentAdapter = new EditContentAdapter(this, R.layout.edit_image_content_item, R.layout.edit_paragraph_content_item, contents, this);
        ListView list_addContent = findViewById(R.id.list_addContent);
        list_addContent.setAdapter(editContentAdapter);
        Button btn_addBack = findViewById(R.id.btn_addBack);
        Button btn_addCommit = findViewById(R.id.btn_addCommit);
        Button btn_deleteArticle = findViewById(R.id.btn_deleteArticle);
        if (is_newArticle) {
            btn_deleteArticle.setVisibility(View.GONE);
        }
        btn_deleteArticle.setOnClickListener(this);
        edit_title = findViewById(R.id.edit_title);
        if (!is_newArticle) {
            edit_title.setText(article.getTitle());
        }
        btn_addCommit.setOnClickListener(this);
        btn_addBack.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        articleDao = ArticleDao.getInstance(this);
        contentDao = ContentDao.getInstance(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_addBack) {
            finish();
        } else if (id == R.id.btn_deleteArticle) {
            articleDao.delete(articleID);
            finish();
        } else if (id == R.id.btn_addCommit) {
            String title = edit_title.getText().toString();
            if (title.isEmpty()) {
                Toast.makeText(this, "保存失败，标题不能为空", Toast.LENGTH_SHORT).show();
            } else {
                for (int i = 0; i < contents.size(); i++) {
                    contents.get(i).setSort(i);
                }
                if (is_newArticle) {
                    articleDao.insert(new Article(title, userId, contents));
                    Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
                } else {
                    articleDao.updata(new Article(articleID, title, userId, contents));
                    Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
                }

                finish();
            }
        } else if (id == R.id.btn_iDelete || id == R.id.btn_pDelete) {
            Content content = (Content) view.getTag();
            for (int i = 0; i < contents.size(); i++) {
                if (contents.get(i).equals(content)) {
                    contents.remove(i);
                    break;
                }
            }
            editContentAdapter.notifyDataSetChanged();
        } else if (id == R.id.btn_iEditImage) {
            imageView = ((EditImage) view.getTag()).getImageView();
            editContent = ((EditImage) view.getTag()).getContent();
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            resultLauncher.launch(intent);
        } else if (id == R.id.btn_iEditCamera) {
            imageView = ((EditImage) view.getTag()).getImageView();
            editContent = ((EditImage) view.getTag()).getContent();
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, TAKE_PHOTO);
            }

        } else {
            AddArticle tag = (AddArticle) view.getTag();
            addItem(tag);
            editContentAdapter.notifyDataSetChanged();
        }
    }

    private void addItem(AddArticle tag) {
        int position = tag.getPosition();
        Log.d("addItem", tag.isParagraph() + "");
        if (position == contents.size()) {
            if (tag.isParagraph()) {
                contents.add(new ParagraphContent(userId, 0, ""));
            } else {
                contents.add(new ImageContent(userId, 0, ""));
            }
        } else {
            List<Content> content = new ArrayList<>();
            for (int i = 0; i < contents.size(); i++) {
                try {
                    ImageContent imageContent = (ImageContent) contents.get(i);
                    ImageContent newContent = new ImageContent(imageContent.getBelong(), imageContent.getSort(), imageContent.getImgPath());
                    content.add(newContent);
                } catch (Exception e) {
                    ParagraphContent paragraphContent = (ParagraphContent) contents.get(i);
                    ParagraphContent newContent = new ParagraphContent(paragraphContent.getBelong(), paragraphContent.getSort(), paragraphContent.getParagraph());
                    content.add(newContent);
                }
            }
            contents.clear();
            for (int i = 0; i < position; i++) {
                try {
                    ImageContent imageContent = (ImageContent) content.get(i);
                    ImageContent newContent = new ImageContent(imageContent.getBelong(), imageContent.getSort(), imageContent.getImgPath());
                    contents.add(newContent);
                } catch (Exception e) {
                    ParagraphContent paragraphContent = (ParagraphContent) content.get(i);
                    ParagraphContent newContent = new ParagraphContent(paragraphContent.getBelong(), paragraphContent.getSort(), paragraphContent.getParagraph());
                    contents.add(newContent);
                }
            }
            if (tag.isParagraph()) {
                contents.add(new ParagraphContent(userId, 0, ""));
            } else {
                contents.add(new ImageContent(userId, 0, ""));
            }
            for (int i = position; i < content.size(); i++) {
                try {
                    ImageContent imageContent = (ImageContent) content.get(i);
                    ImageContent newContent = new ImageContent(imageContent.getBelong(), imageContent.getSort(), imageContent.getImgPath());
                    contents.add(newContent);
                } catch (Exception e) {
                    ParagraphContent paragraphContent = (ParagraphContent) content.get(i);
                    ParagraphContent newContent = new ParagraphContent(paragraphContent.getBelong(), paragraphContent.getSort(), paragraphContent.getParagraph());
                    contents.add(newContent);
                }
            }
        }
        Log.d("addItem", contents.toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PHOTO && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            savebitmap(imageBitmap);
        }
    }

    private void savebitmap(Bitmap bitmap) {
        SharedPreferences config = getSharedPreferences("config", MODE_PRIVATE);
        SharedPreferences.Editor editor = config.edit();
        int imageCount = config.getInt("imageCount", 0);
        imageCount++;
        editor.putInt("imageCount", imageCount);
        editor.commit();
        String path = this.getFilesDir() + "/" + imageCount + ".png";
        File file = new File(path);
        try {
            //文件输出流
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            //压缩图片，如果要保存png，就用Bitmap.CompressFormat.PNG，要保存jpg就用Bitmap.CompressFormat.JPEG,质量是100%，表示不压缩
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            //写入，这里会卡顿，因为图片较大
            fileOutputStream.flush();
            //记得要关闭写入流
            fileOutputStream.close();
            for (int i = 0; i < contents.size(); i++) {
                if (contents.get(i).equals(editContent)) {
                    ((ImageContent) editContent).setImgPath(path);
                    editContentAdapter.notifyDataSetChanged();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            //失败的提示
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


}