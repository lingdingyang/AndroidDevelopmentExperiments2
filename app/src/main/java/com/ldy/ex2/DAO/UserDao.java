package com.ldy.ex2.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class UserDao extends SQLiteOpenHelper {
    //    数据库名称
    private static final String dbName = "Ex2";
    //    数据表名
    private static final String tbName = "User";
    //    数据库版本
    private static final int dbVersion = 1;
    //    一个静态的UserDao对象，用于实现单实例模式
    private static UserDao myHelper = null;


    public UserDao(@Nullable Context context) {
        super(context, dbName, null, dbVersion);
    }

    //    执行创建数据库的语句
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE  IF NOT EXISTS user (uid INTEGER primary key AUTOINCREMENT NOT NULL ,user_name varchar(30)  NOT NULL, reg_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP )";
        sqLiteDatabase.execSQL(sql);
        sql = "CREATE TABLE  IF NOT EXISTS content (cid INTEGER primary key AUTOINCREMENT NOT NULL ,belong INTEGER NOT NULL ,sort INTEGER NOT NULL ,is_image INTEGER  ,paragraph varchar(900) ,img_path varchar(200)   )";
        sqLiteDatabase.execSQL(sql);
        sql = "CREATE TABLE  IF NOT EXISTS article (aid INTEGER primary key AUTOINCREMENT NOT NULL ,title varchar(80)  NOT NULL,author INTEGER NOT NULL , create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP )";
        sqLiteDatabase.execSQL(sql);
    }

    //获得UserDao实例，如果myHelper为null，则实例化myHelper否则直接返回myHelper。
    public static UserDao getInstance(Context context) {
        if (myHelper == null) {
            myHelper = new UserDao(context);
        }
        return myHelper;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    //添加用户的方法，参数为用户名
    public long insert(String userName) {
        ContentValues values = new ContentValues();
        values.put("user_name", userName);
        return myHelper.getWritableDatabase().insert(tbName, null, values);
    }

    //判断用户名是否存在的方法，参数为用户名
    public boolean checkExist(String userName) {
        Cursor res = myHelper.getReadableDatabase().query(tbName, null, "user_name=?", new String[]{userName}, null, null, null);
        return res.moveToNext();
    }

    //获取用户ID的方法，参数为用户名
    public int getID(String userName) {
        int id = -1;
        Cursor res = myHelper.getReadableDatabase().query(tbName, null, "user_name=?", new String[]{userName}, null, null, null);
        while (res.moveToNext()) {
            id = res.getInt(0);
            Log.d("getID", id + "");
        }
        return id;
    }

    //获取用户名的方法，参数为用户ID
    public String getUserName(int userID) {
        Cursor res = myHelper.getReadableDatabase().query(tbName, null, "uid=?", new String[]{String.valueOf(userID)}, null, null, null);
        while (res.moveToNext()) {
            return res.getString(1);
        }
        return "noName";
    }
}
