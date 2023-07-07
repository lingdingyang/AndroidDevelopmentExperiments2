package com.ldy.ex2.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.ldy.ex2.POJO.Content;
import com.ldy.ex2.POJO.ImageContent;
import com.ldy.ex2.POJO.ParagraphContent;

import java.util.ArrayList;
import java.util.List;

public class ContentDao extends SQLiteOpenHelper {
    private static final String dbName = "Ex2";
    private static final String tbName = "Content";
    private static final int dbVersion = 1;
    private static ContentDao myHelper = null;


    public ContentDao(@Nullable Context context) {
        super(context, dbName, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE  IF NOT EXISTS content (cid INTEGER primary key AUTOINCREMENT NOT NULL ,belong INTEGER NOT NULL ,sort INTEGER NOT NULL ,is_image INTEGER  ,paragraph varchar(900) ,img_path varchar(200)  NOT NULL )";
        sqLiteDatabase.execSQL(sql);
    }

    public static ContentDao getInstance(Context context) {
        if (myHelper == null) {
            myHelper = new ContentDao(context);
        }
        return myHelper;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public List<Content> getArticleContents(int aid) {
        ArrayList<Content> contents = new ArrayList<>();
        String s = aid + "";
        Cursor query = myHelper.getReadableDatabase().query(tbName, null, "belong=?", new String[]{s}, null, null, null);
        while (query.moveToNext()) {
            int cid = query.getInt(0);
            int belong = query.getInt(1);
            int sort = query.getInt(2);
            int isImg = query.getInt(3);
            String paragraph = query.getString(4);
            String imgPath = query.getString(5);
            if (paragraph != null) {
                contents.add(new ParagraphContent(cid, belong, sort, paragraph));
            } else {
                contents.add(new ImageContent(cid, belong, sort, imgPath));
            }
        }
        return contents;
    }

    public long insert(Content content) {
        ContentValues values = new ContentValues();
        try {
            ImageContent imageContent = (ImageContent) content;
            values.put("belong", imageContent.getBelong());
            values.put("sort", imageContent.getSort());
            values.put("is_image", 1);
            values.put("img_path", imageContent.getImgPath());
            return myHelper.getWritableDatabase().insert(tbName, null, values);
        } catch (Exception e) {
            ParagraphContent paragraphContent = (ParagraphContent) content;
            values.put("belong", paragraphContent.getBelong());
            values.put("sort", paragraphContent.getSort());
            values.put("is_image", 0);
            values.put("paragraph", paragraphContent.getParagraph());
            return myHelper.getWritableDatabase().insert(tbName, null, values);
        }
    }

    public void delete(int cid) {
        myHelper.getWritableDatabase().delete(tbName, "cid=?", new String[]{String.valueOf(cid)});
    }
}
