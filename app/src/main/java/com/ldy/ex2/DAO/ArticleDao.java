package com.ldy.ex2.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.ldy.ex2.POJO.Article;
import com.ldy.ex2.POJO.Content;
import com.ldy.ex2.POJO.ParagraphContent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ArticleDao extends SQLiteOpenHelper {
    private static final String dbName = "Ex2";
    private static final String tbName = "article";
    private static final int dbVersion = 1;
    private static ArticleDao myHelper = null;
    private final ContentDao contentDao;


    public ArticleDao(@Nullable Context context) {
        super(context, dbName, null, dbVersion);
        this.contentDao = ContentDao.getInstance(context);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE  IF NOT EXISTS article (aid INTEGER primary key AUTOINCREMENT NOT NULL ,title varchar(80)  NOT NULL,author INTEGER NOT NULL , create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP )";
        sqLiteDatabase.execSQL(sql);
    }

    public static ArticleDao getInstance(Context context) {
        if (myHelper == null) {
            myHelper = new ArticleDao(context);
        }
        return myHelper;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public List<Article> getUserArticles(int userID) {
        ArrayList<Article> articles = new ArrayList<>();
        String s = userID + "";
        Cursor query = myHelper.getReadableDatabase().query(tbName, null, "author=?", new String[]{s}, null, null, null);
        while (query.moveToNext()) {
            int aid = query.getInt(0);
            String title = query.getString(1);
            int author = query.getInt(2);
            String createTime = query.getString(3);
            List<Content> contents = contentDao.getArticleContents(aid);
            articles.add(new Article(aid, title, author, createTime, contents));
        }
        return articles;
    }

    public Article getArticle(int articleID) {
        Cursor query = myHelper.getReadableDatabase().query(tbName, null, "aid=?", new String[]{String.valueOf(articleID)}, null, null, null);
        Article article = null;
        while (query.moveToNext()) {
            int aid = query.getInt(0);
            String title = query.getString(1);
            int author = query.getInt(2);
            String createTime = query.getString(3);
            List<Content> contents = contentDao.getArticleContents(aid);
            article = new Article(aid, title, author, createTime, contents);
        }
        return article;
    }

    public long insert(Article article) {
        ContentValues values = new ContentValues();
        values.put("title", article.getTitle());
        values.put("author", article.getAuthor());
        long insert = myHelper.getWritableDatabase().insert(tbName, null, values);
        List<Content> contents = article.getContents();
        for (int i = 0; i < contents.size(); i++) {
            contents.get(i).setBelong((int) insert);
            try {
                ParagraphContent content = (ParagraphContent) contents.get(i);
                content.setParagraph("  " + content.getParagraph().trim());
            } catch (Exception e) {

            }
        }
        for (int i = 0; i < contents.size(); i++) {
            contentDao.insert(contents.get(i));
        }
        return insert;
    }

    public void updata(Article article) {
        List<Content> contents = contentDao.getArticleContents(article.getAid());
        for (int i = 0; i < contents.size(); i++) {
            contentDao.delete(contents.get(i).getCid());
        }
        ContentValues values = new ContentValues();
        values.put("title", article.getTitle());
        myHelper.getWritableDatabase().update(tbName, values, "aid=?", new String[]{String.valueOf(article.getAid())});
        contents = article.getContents();
        for (int i = 0; i < contents.size(); i++) {
            contents.get(i).setBelong((int) article.getAid());
            try {
                ParagraphContent content = (ParagraphContent) contents.get(i);
                content.setParagraph("  " + content.getParagraph().trim());
            } catch (Exception e) {

            }
        }
        for (int i = 0; i < contents.size(); i++) {
            contentDao.insert(contents.get(i));
        }
    }

    public void delete(int articleID) {
        myHelper.getWritableDatabase().delete(tbName, "aid=?", new String[]{String.valueOf(articleID)});
    }
}
