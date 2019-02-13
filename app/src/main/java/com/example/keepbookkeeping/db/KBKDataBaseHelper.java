package com.example.keepbookkeeping.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.keepbookkeeping.utils.ToastUtil;

/**
 * @author 邹永鹏
 * @date 2019/2/12
 * @description :
 */
public class KBKDataBaseHelper extends SQLiteOpenHelper {

    private Context mContext;

    /**
     * 单笔花费类型——SingleDataBean
     */
    public static final String CREATE_ALL_DATA="create table AllData ("
            +" id integer primary key autoincrement not null,"
            +" type integer not null,"
            +" money real not null,"
            +" date text not null,"
            +" type_name text not null,"
            +" bill_name text not null ,"
            +" description text )";

    public static KBKDataBaseHelper getKBKDataBase(Context context){
        return new KBKDataBaseHelper(context,"KBKAllData.db",null,1);
    }

    public KBKDataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_ALL_DATA);
            ToastUtil.success("create table AllTable!");
        }catch (Exception e){
            ToastUtil.error("create table AllTable error!");
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
