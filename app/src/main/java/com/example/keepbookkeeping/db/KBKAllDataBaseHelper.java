package com.example.keepbookkeeping.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.keepbookkeeping.KBKApplication;
import com.example.keepbookkeeping.utils.BillTableUtil;
import com.example.keepbookkeeping.utils.DataTypeTableUtil;
import com.example.keepbookkeeping.utils.LogUtil;

/**
 * @author 邹永鹏
 * @date 2019/2/12
 * @description :
 */
public class KBKAllDataBaseHelper extends SQLiteOpenHelper {

    private Context mContext;

    private static volatile KBKAllDataBaseHelper dbHelper;

    public static KBKAllDataBaseHelper getInstance(){
        if (dbHelper == null) {
            synchronized (KBKAllDataBaseHelper.class){
                if (dbHelper==null){
                    dbHelper=new KBKAllDataBaseHelper("KBKAllData.db",null,1);
                }
            }
        }
        return dbHelper;
    }

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

    public static final String CREATE_ALL_DATA_TYPE="create table AllDataType ("
            +" id integer primary key autoincrement not null,"
            +" type integer not null,"
            +" imageId integer not null,"
            +" name text not null)";

    public static final String CREATE_ALL_BILL="create table AllBill ("
            +" id integer primary key autoincrement not null,"
            +" type integer not null,"//资产还是负债
            +" imageId integer not null,"
            +" name text not null,"
            +" description text ,"
            +" initial_count not null ,"
            +" canChange integer not null)";


    public KBKAllDataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    private KBKAllDataBaseHelper(String name, SQLiteDatabase.CursorFactory factory, int version) {
        this(KBKApplication.getContext(), name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_ALL_DATA);
            db.execSQL(CREATE_ALL_DATA_TYPE);
            DataTypeTableUtil.initAllDateType(db);

            db.execSQL(CREATE_ALL_BILL);
            BillTableUtil.initBill(db);
            LogUtil.d("db","create table AllTable!");
        }catch (Exception e){
            LogUtil.d("db","create table AllTable error!");
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
