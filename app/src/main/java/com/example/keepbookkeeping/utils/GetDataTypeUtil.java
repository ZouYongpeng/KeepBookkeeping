package com.example.keepbookkeeping.utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.keepbookkeeping.KBKApplication;
import com.example.keepbookkeeping.R;
import com.example.keepbookkeeping.bean.DataTypeBean;
import com.example.keepbookkeeping.db.KBKAllDataBaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 邹永鹏
 * @date 2019/1/31
 * @description :
 */
public class GetDataTypeUtil {

    public static final String FIRST_OUTCOME_DATA_TYPE="select * from AllDataType where type = 0 limit 0,1";

    public static final String FIRST_INCOME_DATA_TYPE="select * from AllDataType where type = 1 limit 0,1";

    public static final String QUERY_ALL_OUTCOME_DATA_TYPE="select * from AllDataType where type = 0";

    public static final String QUERY_ALL_INCOME_DATA_TYPE="select * from AllDataType where type = 1";

    public static final String GET_TYPE_NAME_IMAGE="select * from AllDataType where name = ?";

    public static DataTypeBean getFirstIncomeDataTypeBean(){
        DataTypeBean bean=new DataTypeBean(1,R.drawable.ic_income_gongzi,"工资");
        Cursor cursor=KBKAllDataBaseHelper.getInstance().getWritableDatabase().rawQuery(FIRST_INCOME_DATA_TYPE,null);
        if (cursor.moveToFirst()){
            bean=new DataTypeBean(
                    cursor.getInt(cursor.getColumnIndex("type")),
                    cursor.getInt(cursor.getColumnIndex("imageId")),
                    cursor.getString(cursor.getColumnIndex("name")));
        }
        cursor.close();
        return bean;
    }

    public static DataTypeBean getFirstOutcomeDataTypeBean(){
        DataTypeBean bean=new DataTypeBean(0,R.drawable.ic_outcome_default,"一般");
        Cursor cursor=KBKAllDataBaseHelper.getInstance().getWritableDatabase().rawQuery(FIRST_OUTCOME_DATA_TYPE,null);
        if (cursor.moveToFirst()){
            bean=new DataTypeBean(
                    cursor.getInt(cursor.getColumnIndex("type")),
                    cursor.getInt(cursor.getColumnIndex("imageId")),
                    cursor.getString(cursor.getColumnIndex("name")));
        }
        cursor.close();
        return bean;
    }

    public static List<DataTypeBean> getIncomeDataTypeBeanList(){
        List<DataTypeBean> list=new ArrayList<>();
        Cursor cursor=KBKAllDataBaseHelper.getInstance().getWritableDatabase().rawQuery(QUERY_ALL_INCOME_DATA_TYPE,null);
        if (cursor.moveToFirst()){
            do {
                list.add(new DataTypeBean(
                        cursor.getInt(cursor.getColumnIndex("type")),
                        cursor.getInt(cursor.getColumnIndex("imageId")),
                        cursor.getString(cursor.getColumnIndex("name"))));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public static List<DataTypeBean> getOutcomeDataTypeBeanList(){
        List<DataTypeBean> list=new ArrayList<>();
        Cursor cursor=KBKAllDataBaseHelper.getInstance().getWritableDatabase().rawQuery(QUERY_ALL_OUTCOME_DATA_TYPE,null);
        if (cursor.moveToFirst()){
            do {
                list.add(new DataTypeBean(
                        cursor.getInt(cursor.getColumnIndex("type")),
                        cursor.getInt(cursor.getColumnIndex("imageId")),
                        cursor.getString(cursor.getColumnIndex("name"))));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public static int getImageId(String typeName){
        int imageId=0;
        Cursor cursor=KBKAllDataBaseHelper.getInstance().getWritableDatabase().rawQuery(GET_TYPE_NAME_IMAGE,new String[]{typeName});
        if (cursor.moveToFirst()){
            imageId=cursor.getInt(cursor.getColumnIndex("imageId"));
        }
        cursor.close();
        return imageId;
    }
}
