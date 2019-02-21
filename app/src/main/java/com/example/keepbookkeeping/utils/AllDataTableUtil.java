package com.example.keepbookkeeping.utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.widget.TextView;

import com.example.keepbookkeeping.bean.FormApartBean;
import com.example.keepbookkeeping.bean.FormTrendBean;
import com.example.keepbookkeeping.bean.SingleDataBean;
import com.example.keepbookkeeping.db.KBKAllDataBaseHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author 邹永鹏
 * @date 2019/2/12
 * @description :
 */
public class AllDataTableUtil {

    public static final String TAG="db";

    public static final int TYPE_TOTAL=0;
    public static final int TYPE_OUTCOME=1;
    public static final int TYPE_INCOME=2;

    public static final String QUERY_ALL_DATA_ORDER_BY_DATE="SELECT * FROM AllData ORDER BY DATE DESC";

    public static final String QUERY_DATA_ORDER_BY_DATE_IN_BILL_NAME="SELECT * FROM AllData WHERE bill_name = ? ORDER BY DATE DESC";

    public static final String QUERY_DATE_LIST="SELECT DISTINCT DATE FROM AllData ORDER BY DATE DESC";

    public static final String QUERY_DATE_LIST_IN_BILL_NAME="SELECT DISTINCT DATE FROM AllData WHERE bill_name = ? ORDER BY DATE DESC";

    public static final String QUERY_DATE_COUNT="SELECT COUNT(DISTINCT DATE) AS count FROM AllData";

    public static final String SUM_OUTCOME_MONEY_BY_DATE="SELECT SUM(money) AS money_count FROM AllData WHERE date like ? and type = 0";

    public static final String SUM_INCOME_MONEY_BY_DATE="SELECT SUM(money) AS money_count FROM AllData WHERE date like ? and type = 1";

    public static final String FIRST_YEAR_DATE="SELECT * FROM AllData ORDER BY DATE DESC LIMIT 0,1";

    public static final String DELETE_DATA_BY_ID="DELETE FROM AllData WHERE id = ?";

    public static final String SELECT_SUM_BY_BILL_TYPE="select type , money , bill_name from AllData where bill_name in (select name from AllBill where AllBill.type = ? )";

    public static final String UPDATE_BILL_NAME="update AllData set bill_name = ? where bill_name = ? ";

    public static final String UPDATE_DATA_BY_ID="UPDATE AllData " +
            "SET type = ? " +
            ", money = ? " +
            ", date = ? " +
            ", type_name = ? " +
            ", bill_name = ? " +
            ", description = ? " +
            "where id = ?";

    public static final String QUERY_ALL_DATA_IN_SEARCH_WORD="select * from AllData " +
            "where money like ? " +
            "or type_name like ? " +
            "or bill_name like ? " +
            "or description like ? " +
            "ORDER BY DATE DESC";

    public static final String QUERY_DATE_LIST_IN_SEARCH_WORD="SELECT DISTINCT DATE FROM AllData " +
            "where money like ? " +
            "or type_name like ? " +
            "or bill_name like ? " +
            "or description like ? " +
            "ORDER BY DATE DESC";


    public static final String QUERY_MONEY_IN_BILL_NAME="SELECT * FROM AllData WHERE bill_name = ?";

    public static List<SingleDataBean> queryAllDataOrderByDate(){
        return queryData(QUERY_ALL_DATA_ORDER_BY_DATE,null);
    }

    public static List<SingleDataBean> queryDataOrderByDateByBillName(String billName){
        return queryData(QUERY_DATA_ORDER_BY_DATE_IN_BILL_NAME,new String[]{billName});
    }

    /**
     * 全局搜索
     */
    public static List<SingleDataBean> queryDataByWord(String word){
        LogUtil.d("db","全局搜索");
        word="%"+word+"%";
        return queryData(QUERY_ALL_DATA_IN_SEARCH_WORD,new String[]{word,word,word,word});
    }

    public static List<SingleDataBean> queryData(String sql,String[] selectionArgs){
        LogUtil.d(TAG,"---------queryData---------");
        List<SingleDataBean> singleDataList=new ArrayList<>();
        Cursor cursor= KBKAllDataBaseHelper.getInstance().getWritableDatabase().rawQuery(sql,selectionArgs);
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

    public static List<String> getAllDifferentDateList(){
        return getDifferentDateList(QUERY_DATE_LIST,null);
    }

    public static List<String> getDifferentDateListInBillName(String billName){
        return getDifferentDateList(QUERY_DATE_LIST_IN_BILL_NAME,new String[]{billName});
    }

    public static List<String> getDifferentDateListInSearchWorld(String word){
        word="%"+word+"%";
        return getDifferentDateList(QUERY_DATE_LIST_IN_SEARCH_WORD,new String[]{word,word,word,word});
    }

    public static List<String> getDifferentDateList(String sql,String[] selectionArgs){
        List<String> list=new ArrayList<>();
        Cursor cursor=KBKAllDataBaseHelper.getInstance().getWritableDatabase().rawQuery(sql,selectionArgs);
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

    public static List<String> getAllDifferentMonthList(){
        return getDifferentMonthList(QUERY_ALL_DATA_ORDER_BY_DATE,null);
    }

    public static List<String> getDifferentMonthListInBillName(String billName){
        return getDifferentMonthList(QUERY_DATA_ORDER_BY_DATE_IN_BILL_NAME,new String[]{billName});
    }

    public static List<String> getDifferentMonthListInSearchWord(String word){
        word="%"+word+"%";
        return getDifferentMonthList(QUERY_ALL_DATA_IN_SEARCH_WORD,new String[]{word,word,word,word});
    }

    public static List<String> getDifferentMonthList(String sql,String[] selectionArgs){
        String previousMonth=null;
        String month;
        String[] monthSplit;
        List<String> months=new ArrayList<>();
        Cursor cursor=KBKAllDataBaseHelper.getInstance().getWritableDatabase().rawQuery(sql,selectionArgs);
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

    public static List<String> getAllDifferentYearList(){
        String previousYear=null;
        String year;
        List<String> months=new ArrayList<>();
        Cursor cursor=KBKAllDataBaseHelper.getInstance().getWritableDatabase().rawQuery(QUERY_ALL_DATA_ORDER_BY_DATE,null);
        if (cursor.moveToFirst()) {
            do {
                year=cursor.getString(cursor.getColumnIndex("date")).split("-")[0];
                if (!TextUtils.equals(previousYear,year)) {
                    months.add(year);
                    previousYear=year;
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
        return getSumMoneyByDate(date,TYPE_INCOME);
    }

    /**
     * 获取数据库中包含某date（如“2019-2-14”或“2019-2”)的总支出金额
     * @param date
     * @return
     */
    public static float getTotalOutcomeMoney(String date){
        return getSumMoneyByDate(date,TYPE_OUTCOME);
    }

    public static float getSumMoneyByDate(String date,int type){
        float count=0;
        String sql;
        if (type==TYPE_INCOME){
            sql=SUM_INCOME_MONEY_BY_DATE;
        }else {
            sql=SUM_OUTCOME_MONEY_BY_DATE;
        }
        if (!TextUtils.isEmpty(date) && !TextUtils.equals(date,"%%")){
            Cursor cursor=KBKAllDataBaseHelper.getInstance().getWritableDatabase().rawQuery(sql,new String[]{date});
            if (cursor.moveToFirst()){
                do {
                    count=cursor.getFloat(cursor.getColumnIndex("money_count"));
                }while(cursor.moveToNext());
            }
            cursor.close();
            LogUtil.d(TAG,"SumMoneyByDate in "+date+" = "+count+" , type = "+type);
        }
        return count;
    }

    /**
     * 获取数据库中某账户金额
     * @param billName
     * @return
     */
    public static float getMoneyByBillName(String billName,int type){
        SQLiteDatabase db=KBKAllDataBaseHelper.getInstance().getWritableDatabase();
        Cursor cursor=db.rawQuery(QUERY_MONEY_IN_BILL_NAME,new String[]{billName});
        float outcomeCount=0;
        float incomeCount=0;
        if (cursor.moveToFirst()){
            do {
                if (cursor.getInt(cursor.getColumnIndex("type"))==0){
                    outcomeCount+=cursor.getFloat(cursor.getColumnIndex("money"));
                }else {
                    incomeCount+=cursor.getFloat(cursor.getColumnIndex("money"));
                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        if (type==TYPE_OUTCOME){
            return outcomeCount;
        }else if (type==TYPE_INCOME){
            return incomeCount;
        }else {
            return incomeCount-outcomeCount;
        }
    }

    /**
     * 获取db第一条数据的年，如“2019”
     * @return
     */
    public static String getFirstYear(){
        Cursor cursor=KBKAllDataBaseHelper.getInstance().getWritableDatabase().rawQuery(FIRST_YEAR_DATE,null);
        String date=String.valueOf(DateUtil.getCurrentYear());
        if (cursor.moveToFirst()){
            date=DateUtil.getYearOfDate(cursor.getString(cursor.getColumnIndex("date")));
        }
        cursor.close();
        return date;
    }

    /**
     * 获取db第一条数据的年月，如“2019-02”
     * @return
     */
    public static String getFirstYearMonth(){
        Cursor cursor=KBKAllDataBaseHelper.getInstance().getWritableDatabase().rawQuery(FIRST_YEAR_DATE,null);
        String date=DateUtil.getCurrentYear()+"-"+DateUtil.getCurrentMonth();
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

    public static float getSumMoneyByBillType(int type){
        float outcomeCount=0;
        float incomeCount=0;
        Cursor cursor;
        if (type==BillTableUtil.TYPE_ASSETS){
            cursor=KBKAllDataBaseHelper.getInstance().getWritableDatabase().rawQuery(SELECT_SUM_BY_BILL_TYPE,new String[]{"0"});

        }else {
            cursor=KBKAllDataBaseHelper.getInstance().getWritableDatabase().rawQuery(SELECT_SUM_BY_BILL_TYPE,new String[]{"1"});
        }
        if (cursor.moveToFirst()){
            do {
                if (cursor.getInt(cursor.getColumnIndex("type"))==0){
                    outcomeCount+=cursor.getFloat(cursor.getColumnIndex("money"));
                }else {
                    incomeCount+=cursor.getFloat(cursor.getColumnIndex("money"));
                }
            }while (cursor.moveToNext());
        }
        return incomeCount-outcomeCount;
    }

    public static void updateBillName(String newBillName,String oldBillName){
        KBKAllDataBaseHelper.getInstance().getWritableDatabase().execSQL(UPDATE_BILL_NAME,new String[]{newBillName,oldBillName});
    }

    public static final String SELECT_SUM_MONEY_AND_TYPE_NAME_BY_DATE="select type_name , sum(money) as sum_money from AllData where date like ? and type = ? group by type_name order by sum_money desc";

    public static List<FormApartBean> getFormApartListByDateAndType(String date,int type){
        List<FormApartBean> list=new ArrayList<>();
        SQLiteDatabase db=KBKAllDataBaseHelper.getInstance().getWritableDatabase();
        Cursor cursor=db.rawQuery(SELECT_SUM_MONEY_AND_TYPE_NAME_BY_DATE,new String[]{date,String.valueOf(type)});
        if (cursor.moveToFirst()){
            do {
                list.add(new FormApartBean(
                        cursor.getString(cursor.getColumnIndex("type_name")),
                        cursor.getFloat(cursor.getColumnIndex("sum_money"))
                ));
            }while (cursor.moveToNext());
        }
        cursor.close();
        LogUtil.d(TAG,"---getFormApartListByDateAndType---");
        for (int i=0;i<list.size();i++){
            LogUtil.d(TAG,list.get(i).toString());
        }
        return list;
    }

    /**
     *
     * @param year (2019)
     * @return
     */
    public static List<FormTrendBean> getFormTrendListByYear(String year){
        String yearAndMonth;
        List<FormTrendBean> list=new ArrayList<>();
        for (int i=1;i<=12;i++){
            if (i<10){
                yearAndMonth="%"+year+"-0"+i+"%";
            }else {
                yearAndMonth="%"+year+"-"+i+"%";
            }
            list.add(new FormTrendBean(
                    i+"月",
                    getSumMoneyByDate(yearAndMonth,TYPE_INCOME),
                    getSumMoneyByDate(yearAndMonth,TYPE_OUTCOME)
                    ));
        }
        LogUtil.d(TAG,"---getFormTrendListByYear---");
        for (int i=0;i<list.size();i++){
            LogUtil.d(TAG,list.get(i).toString());
        }
        return list;
    }

}
