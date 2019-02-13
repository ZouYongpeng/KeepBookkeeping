package com.example.keepbookkeeping.utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.widget.TextView;

import com.example.keepbookkeeping.bean.SingleDataBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 邹永鹏
 * @date 2019/2/12
 * @description :
 */
public class DataBaseUtil {

    public static final String TAG="db";

    public static final String QUERY_ALL_DATA_ORDER_BY_DATE="SELECT * FROM AllData ORDER BY DATE DESC";

    public static final String QUERY_DATE_LIST="SELECT DISTINCT DATE FROM AllData ORDER BY DATE DESC";

    public static final String QUERY_DATE_COUNT="SELECT COUNT(DISTINCT DATE) AS count FROM AllData";

    public static List<SingleDataBean> queryAllDataOrderByDate(SQLiteDatabase db){
        return queryData(db,QUERY_ALL_DATA_ORDER_BY_DATE);
    }

    public static List<SingleDataBean> queryData(SQLiteDatabase db,String sql){
        LogUtil.d(TAG,"---------queryData---------");
        List<SingleDataBean> singleDataList=new ArrayList<>();
        Cursor cursor=db.rawQuery(sql,null);
        if (cursor.moveToFirst()){
            do {
                int type=cursor.getInt(cursor.getColumnIndex("type"));
                float money=cursor.getFloat(cursor.getColumnIndex("money"));
                Date date=DateUtil.stringToDate(cursor.getString(cursor.getColumnIndex("date")));
                String typeName=cursor.getString(cursor.getColumnIndex("type_name"));
                String billName=cursor.getString(cursor.getColumnIndex("bill_name"));
                String description=cursor.getString(cursor.getColumnIndex("description"));
                SingleDataBean bean=new SingleDataBean(type,money,date,typeName,billName,description);
                LogUtil.d(TAG,bean.toString());
                singleDataList.add(bean);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return singleDataList;
    }

    public static void insertSingleDataToAllData(SingleDataBean bean, SQLiteDatabase db){
        LogUtil.d(TAG,bean.toString());
        ContentValues values=new ContentValues();
        values.put("type",bean.getType());
        values.put("money",bean.getMoney());
        values.put("date",DateUtil.dateToString(bean.getDate()));
        values.put("type_name",bean.getTypeName());
        values.put("bill_name",bean.getBillName());
        if (!TextUtils.isEmpty(bean.getDescription())){
            values.put("description",bean.getDescription());
        }
        db.insert("AllData",null,values);
        values.clear();
    }

    public static int getAllDataCount(SQLiteDatabase db){
        return db.rawQuery(QUERY_ALL_DATA_ORDER_BY_DATE,null).getCount();
    }

    public static List<String> getDifferentDateList(SQLiteDatabase db){
        List<String> list=new ArrayList<>();
        Cursor cursor=db.rawQuery(QUERY_DATE_LIST,null);
        if (cursor.moveToFirst()){
            do {
                list.add(cursor.getString(cursor.getColumnIndex("date")));
            }while (cursor.moveToNext());
        }
        cursor.close();
        LogUtil.d(TAG,"---getDifferentDateList---");
        for (int i=0;i<list.size();i++){
            LogUtil.d(TAG,list.get(i));
        }
        return list;
    }

    public static int getDifferentDateCount(SQLiteDatabase db){
        int count=0;
        Cursor cursor=db.rawQuery(QUERY_DATE_COUNT,null);
        if (cursor.moveToFirst()){
            count=cursor.getInt(cursor.getColumnIndex("count"));
        }
        cursor.close();
        return count;
    }

    public static List<String> getDifferentMonthList(SQLiteDatabase db){
        String previousMonth=null;
        String month;
        String[] monthSplit;
        String currentYear=String.valueOf(DateUtil.getCurrentYear());
        int currentMonth=DateUtil.getCurrentMonth();
        List<String> months=new ArrayList<>();
        Cursor cursor=db.rawQuery(QUERY_ALL_DATA_ORDER_BY_DATE,null);
        if (cursor.moveToFirst()) {
            do {
                monthSplit=cursor.getString(cursor.getColumnIndex("date")).split("-");
                //如果该数据不为本月数据
//                if (TextUtils.equals(monthSplit[0],currentYear) &&
//                        currentMonth==Integer.parseInt(monthSplit[1])){
//                }else {
                    month=monthSplit[0]+"-"+monthSplit[1];
                    if (!TextUtils.equals(previousMonth,month)) {
                        months.add(month);
                        previousMonth=month;
                    }
//                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        LogUtil.d(TAG,"---getDifferentMonthList---");
        for (int i=0;i<months.size();i++){
            LogUtil.d(TAG,months.get(i));
        }
        return months;
    }
}
