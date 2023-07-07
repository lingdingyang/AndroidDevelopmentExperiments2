package com.ldy.ex2.Activities;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ldy.ex2.Adapter.ViewContentAdapter;
import com.ldy.ex2.DAO.ArticleDao;
import com.ldy.ex2.POJO.Article;
import com.ldy.ex2.R;

public class ViewActivity extends AppCompatActivity {

    private ArticleDao articleDao;
    private int articleID;
    private int userID;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        userName = getIntent().getStringExtra("userName");
        userID = getIntent().getIntExtra("userID", -1);
        articleID = getIntent().getIntExtra("articleID", -1);
        articleDao = ArticleDao.getInstance(this);
        ListView list_viewContent = findViewById(R.id.list_viewContent);
        TextView text_viewTitle = findViewById(R.id.text_viewTitle);
        Article article = articleDao.getArticle(articleID);
        text_viewTitle.setText(article.getTitle());
        ViewContentAdapter adapter = new ViewContentAdapter(this, R.layout.view_image_content_item, R.layout.view_paragraph_content_item, article.getContents());
        list_viewContent.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        articleDao = ArticleDao.getInstance(this);
    }
}