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

    public static final String INSERT_DATA_INTO_ALL_DATA_TYPE="insert into AllDataType(type,imageId,name) values( ? , ? , ? )";

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

    public static void initAllDateType(SQLiteDatabase db){
        db.execSQL(INSERT_DATA_INTO_ALL_DATA_TYPE,new String[]{"0",String.valueOf(R.drawable.ic_outcome_default),"一般"});
        db.execSQL(INSERT_DATA_INTO_ALL_DATA_TYPE,new String[]{"0",String.valueOf(R.drawable.ic_outcome_eat),"餐饮"});
        db.execSQL(INSERT_DATA_INTO_ALL_DATA_TYPE,new String[]{"0",String.valueOf(R.drawable.ic_outcome_car),"交通"});
        db.execSQL(INSERT_DATA_INTO_ALL_DATA_TYPE,new String[]{"0",String.valueOf(R.drawable.ic_outcome_drink),"饮料"});
        db.execSQL(INSERT_DATA_INTO_ALL_DATA_TYPE,new String[]{"0",String.valueOf(R.drawable.ic_outcome_fruit),"水果"});
        db.execSQL(INSERT_DATA_INTO_ALL_DATA_TYPE,new String[]{"0",String.valueOf(R.drawable.ic_outcome_snacks),"零食"});
        db.execSQL(INSERT_DATA_INTO_ALL_DATA_TYPE,new String[]{"0",String.valueOf(R.drawable.ic_outcome_vegetables),"买菜"});
        db.execSQL(INSERT_DATA_INTO_ALL_DATA_TYPE,new String[]{"0",String.valueOf(R.drawable.ic_outcome_clothes),"衣服"});
        db.execSQL(INSERT_DATA_INTO_ALL_DATA_TYPE,new String[]{"0",String.valueOf(R.drawable.ic_outcome_daily_necessities),"日用品"});
        db.execSQL(INSERT_DATA_INTO_ALL_DATA_TYPE,new String[]{"0",String.valueOf(R.drawable.ic_outcome_tellphone_cost),"话费"});
        db.execSQL(INSERT_DATA_INTO_ALL_DATA_TYPE,new String[]{"0",String.valueOf(R.drawable.ic_outcome_cosmetics),"护肤"});
        db.execSQL(INSERT_DATA_INTO_ALL_DATA_TYPE,new String[]{"0",String.valueOf(R.drawable.ic_outcome_rent),"房租"});
        db.execSQL(INSERT_DATA_INTO_ALL_DATA_TYPE,new String[]{"0",String.valueOf(R.drawable.ic_outcome_movie),"电影"});
        db.execSQL(INSERT_DATA_INTO_ALL_DATA_TYPE,new String[]{"0",String.valueOf(R.drawable.ic_outcome_taobao),"淘宝"});
        db.execSQL(INSERT_DATA_INTO_ALL_DATA_TYPE,new String[]{"0",String.valueOf(R.drawable.ic_outcome_red_packet),"红包"});
        db.execSQL(INSERT_DATA_INTO_ALL_DATA_TYPE,new String[]{"0",String.valueOf(R.drawable.ic_outcome_drugs),"药品"});

        db.execSQL(INSERT_DATA_INTO_ALL_DATA_TYPE,new String[]{"1",String.valueOf(R.drawable.ic_income_gongzi),"工资"});
        db.execSQL(INSERT_DATA_INTO_ALL_DATA_TYPE,new String[]{"1",String.valueOf(R.drawable.ic_income_shenghuofei),"生活费"});
        db.execSQL(INSERT_DATA_INTO_ALL_DATA_TYPE,new String[]{"1",String.valueOf(R.drawable.ic_income_hongbao),"红包"});
        db.execSQL(INSERT_DATA_INTO_ALL_DATA_TYPE,new String[]{"1",String.valueOf(R.drawable.ic_income_linghuaqian),"零花钱"});
        db.execSQL(INSERT_DATA_INTO_ALL_DATA_TYPE,new String[]{"1",String.valueOf(R.drawable.ic_income_jianzhi),"兼职"});
        db.execSQL(INSERT_DATA_INTO_ALL_DATA_TYPE,new String[]{"1",String.valueOf(R.drawable.ic_income_touzi),"投资"});
        db.execSQL(INSERT_DATA_INTO_ALL_DATA_TYPE,new String[]{"1",String.valueOf(R.drawable.ic_income_jiangjin),"奖金"});
        db.execSQL(INSERT_DATA_INTO_ALL_DATA_TYPE,new String[]{"1",String.valueOf(R.drawable.ic_income_baoxiao),"报销"});
        db.execSQL(INSERT_DATA_INTO_ALL_DATA_TYPE,new String[]{"1",String.valueOf(R.drawable.ic_income_xianjin),"现金"});
        db.execSQL(INSERT_DATA_INTO_ALL_DATA_TYPE,new String[]{"1",String.valueOf(R.drawable.ic_income_tuikuan),"退款"});
        db.execSQL(INSERT_DATA_INTO_ALL_DATA_TYPE,new String[]{"1",String.valueOf(R.drawable.ic_income_zhifubao),"支付宝"});
        db.execSQL(INSERT_DATA_INTO_ALL_DATA_TYPE,new String[]{"1",String.valueOf(R.drawable.ic_income_wechat),"微信"});
        db.execSQL(INSERT_DATA_INTO_ALL_DATA_TYPE,new String[]{"1",String.valueOf(R.drawable.ic_income_default),"一般"});
    }
}
