package com.example.keepbookkeeping.utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.widget.TextView;

import com.example.keepbookkeeping.bean.SingleDataBean;
import com.example.keepbookkeeping.db.KBKAllDataBaseHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 邹永鹏
 * @date 2019/2/12
 * @description :
 */
public class AllDataTableUtil {

    public static final String TAG="db";

    public static final String QUERY_ALL_DATA_ORDER_BY_DATE="SELECT * FROM AllData ORDER BY DATE DESC";

    public static final String QUERY_DATE_LIST="SELECT DISTINCT DATE FROM AllData ORDER BY DATE DESC";

    public static final String QUERY_DATE_COUNT="SELECT COUNT(DISTINCT DATE) AS count FROM AllData";

    public static final String SUM_OUTCOME_MONEY="SELECT SUM(money) AS money_count FROM AllData WHERE date like ? and type = 0";

    public static final String SUM_INCOME_MONEY="SELECT SUM(money) AS money_count FROM AllData WHERE date like ? and type = 1";

    public static final String FIRST_YEAR_DATE="SELECT * FROM AllData ORDER BY DATE DESC LIMIT 0,1";

    public static final String DELETE_DATA_BY_ID="DELETE FROM AllData WHERE id = ?";

    public static final String UPDATE_DATA_BY_ID="UPDATE AllData " +
            "SET type = ? " +
            "and money = ? " +
            "and date = ? " +
            "and type_name = ? " +
            "and bill_name = ? " +
            "and description = ? " +
            "where id = ?";

    public static final String QUERY_MONEY_IN_BILL_NAME="SELECT * FROM AllData WHERE bill_name = ?";

    public static List<SingleDataBean> queryAllDataOrderByDate(){
        return queryData(QUERY_ALL_DATA_ORDER_BY_DATE);
    }

    public static List<SingleDataBean> queryData(String sql){
        LogUtil.d(TAG,"---------queryData---------");
        List<SingleDataBean> singleDataList=new ArrayList<>();
        Cursor cursor= KBKAllDataBaseHelper.getInstance().getWritableDatabase().rawQuery(sql,null);
        if (cursor.moveToFirst()){
            do {
                int type=cursor.getInt(cursor.getColumnIndex("type"));
                float money=cursor.getFloat(cursor.getColumnIndex("money"));
                Date date=DateUtil.stringToDate(cursor.getString(cursor.getColumnIndex("date")));
                String typeName=cursor.getString(cursor.getColumnIndex("type_name"));
                String billName=cursor.getString(cursor.getColumnIndex("bill_name"));
                String description=cursor.getString(cursor.getColumnIndex("description"));
                SingleDataBean bean=new SingleDataBean(type,money,date,typeName,billName,description);
                bean.setId(cursor.getInt(cursor.getColumnIndex("id")));
                LogUtil.d(TAG,bean.toString());
                singleDataList.add(bean);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return singleDataList;
    }

    public static void insertSingleDataToAllData(SingleDataBean bean){
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
        KBKAllDataBaseHelper.getInstance().getWritableDatabase().insert("AllData",null,values);
        values.clear();
    }

    public static int getAllDataCount(){
        return KBKAllDataBaseHelper.getInstance().getWritableDatabase().rawQuery(QUERY_ALL_DATA_ORDER_BY_DATE,null).getCount();
    }

    public static List<String> getDifferentDateList(){
        List<String> list=new ArrayList<>();
        Cursor cursor=KBKAllDataBaseHelper.getInstance().getWritableDatabase().rawQuery(QUERY_DATE_LIST,null);
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

    public static int getDifferentDateCount(){
        int count=0;
        Cursor cursor=KBKAllDataBaseHelper.getInstance().getWritableDatabase().rawQuery(QUERY_DATE_COUNT,null);
        if (cursor.moveToFirst()){
            count=cursor.getInt(cursor.getColumnIndex("count"));
        }
        cursor.close();
        return count;
    }

    public static List<String> getDifferentMonthList(){
        String previousMonth=null;
        String month;
        String[] monthSplit;
        String currentYear=String.valueOf(DateUtil.getCurrentYear());
        int currentMonth=DateUtil.getCurrentMonth();
        List<String> months=new ArrayList<>();
        Cursor cursor=KBKAllDataBaseHelper.getInstance().getWritableDatabase().rawQuery(QUERY_ALL_DATA_ORDER_BY_DATE,null);
        if (cursor.moveToFirst()) {
            do {
                monthSplit=cursor.getString(cursor.getColumnIndex("date")).split("-");
                month=monthSplit[0]+"-"+monthSplit[1];
                if (!TextUtils.equals(previousMonth,month)) {
                    months.add(month);
                    previousMonth=month;
                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        LogUtil.d(TAG,"---getDifferentMonthList---");
        for (int i=0;i<months.size();i++){
            LogUtil.d(TAG,months.get(i));
        }
        return months;
    }

    /**
     * 获取数据库中包含某date（如“2019-2-14”或“2019-2”)的总收入金额
     * @param date
     * @return
     */
    public static float getTotalIncomeMoney(String date){
        float count=0;
        if (!TextUtils.isEmpty(date) && !TextUtils.equals(date,"%%")){
            Cursor cursor=KBKAllDataBaseHelper.getInstance().getWritableDatabase().rawQuery(SUM_INCOME_MONEY,new String[]{date});
            if (cursor.moveToFirst()){
                do {
                    count=cursor.getFloat(cursor.getColumnIndex("money_count"));
                }while(cursor.moveToNext());
            }
            cursor.close();
            LogUtil.d(TAG,"TotalIncomeMoney in "+date+" = "+count);
        }

        return count;
    }

    /**
     * 获取数据库中包含某date（如“2019-2-14”或“2019-2”)的总支出金额
     * @param date
     * @return
     */
    public static float getTotalOutcomeMoney(String date){
        float count=0;
        if (!TextUtils.isEmpty(date) && !TextUtils.equals(date,"%%")){
            Cursor cursor=KBKAllDataBaseHelper.getInstance().getWritableDatabase().rawQuery(SUM_OUTCOME_MONEY,new String[]{date});
            if (cursor.moveToFirst()){
                do {
                    count=cursor.getFloat(cursor.getColumnIndex("money_count"));
                }while(cursor.moveToNext());
            }
            cursor.close();
            LogUtil.d(TAG,"TotalOutcomeMoney in "+date+" = "+count);
        }
        return count;
    }

    /**
     * 获取数据库中某账户金额
     * @param billNmae
     * @return
     */
    public static float getTotalMoneyByBillName(String billNmae){
        SQLiteDatabase db=KBKAllDataBaseHelper.getInstance().getWritableDatabase();
        Cursor cursor=db.rawQuery(QUERY_MONEY_IN_BILL_NAME,new String[]{billNmae});
        float count=0;
        if (cursor.moveToFirst()){
            do {
                if (cursor.getInt(cursor.getColumnIndex("type"))==0){
                    count-=cursor.getFloat(cursor.getColumnIndex("money"));
                }else {
                    count+=cursor.getFloat(cursor.getColumnIndex("money"));
                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        return count;
    }

    /**
     * 获取db第一条数据的年月，如“2019-02”
     * @return
     */
    public static String getFirstYearMonth(){
        Cursor cursor=KBKAllDataBaseHelper.getInstance().getWritableDatabase().rawQuery(FIRST_YEAR_DATE,null);
        String date=null;
        if (cursor.moveToFirst()){
            date=DateUtil.getYearMonthOfDate(cursor.getString(cursor.getColumnIndex("date")));
        }
        cursor.close();
        return date;
    }

    public static void deleteDataById(int id){
        KBKAllDataBaseHelper.getInstance().getWritableDatabase().execSQL(DELETE_DATA_BY_ID,new String[]{String.valueOf(id)});
    }

    public static void updateDataById(SingleDataBean bean,int id){
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
        KBKAllDataBaseHelper.getInstance().getWritableDatabase().update("AllData",values,"id = ?",new String[]{String.valueOf(id)});
        values.clear();
    }
}
