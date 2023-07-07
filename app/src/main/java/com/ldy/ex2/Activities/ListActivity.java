package com.ldy.ex2.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ldy.ex2.Adapter.ArticleAdapter;
import com.ldy.ex2.DAO.ArticleDao;
import com.ldy.ex2.POJO.Article;
import com.ldy.ex2.POJO.Content;
import com.ldy.ex2.POJO.ImageContent;
import com.ldy.ex2.POJO.ParagraphContent;
import com.ldy.ex2.R;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private List<Article> articles;
    private int userID;
    private ArticleDao articleDao = null;
    private ArticleAdapter articleAdapter;
    private ListView list_article;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        userID = getIntent().getIntExtra("userID", -1);
        userName = getIntent().getStringExtra("userName");
        articleDao = ArticleDao.getInstance(this);
        articles = articleDao.getUserArticles(userID);
        articleAdapter = new ArticleAdapter(this, R.layout.list_item, articles, userName, this);
        list_article = findViewById(R.id.list_article);
        list_article.setAdapter(articleAdapter);
        Button btn_addArticle = findViewById(R.id.btn_addArticle);
        btn_addArticle.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        articleDao = ArticleDao.getInstance(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        articles = articleDao.getUserArticles(userID);
        Log.d("onResume", articles.toString());
        articleAdapter = new ArticleAdapter(this, R.layout.list_item, articles, userName, this);
        list_article.setAdapter(articleAdapter);
        articleAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_addArticle) {
            Intent intent = new Intent(this, AddActivity.class);
            intent.putExtra("userID", userID);
            startActivity(intent);
        } else {
            int articleID = Integer.parseInt(((TextView) view.findViewById(R.id.text_articleId)).getText().toString());
            Intent intent = new Intent(this, ViewActivity.class);
            intent.putExtra("userID", userID);
            intent.putExtra("userName", userName);
            intent.putExtra("articleID", articleID);
            startActivity(intent);
        }
    }

    @Override
    public boolean onLongClick(View view) {
        int articleID = Integer.parseInt(((TextView) view.findViewById(R.id.text_articleId)).getText().toString());
        Intent intent = new Intent(this, AddActivity.class);
        intent.putExtra("userID", userID);
        intent.putExtra("userName", userName);
        intent.putExtra("articleID", articleID);
        intent.putExtra("is_newArticle", false);
        startActivity(intent);
        return true;
    }
}